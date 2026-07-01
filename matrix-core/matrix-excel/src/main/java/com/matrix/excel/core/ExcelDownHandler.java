package com.matrix.excel.core;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.EnumUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import org.apache.fesod.sheet.annotation.ExcelProperty;
import org.apache.fesod.sheet.write.handler.SheetWriteHandler;
import org.apache.fesod.sheet.write.metadata.holder.WriteSheetHolder;
import org.apache.fesod.sheet.write.metadata.holder.WriteWorkbookHolder;
import com.matrix.common.exception.ServiceException;
import com.matrix.common.service.IDictService;
import com.matrix.common.util.collection.StreamUtils;
import com.matrix.common.util.reflect.ReflectUtils;
import com.matrix.common.util.spring.SpringUtils;
import com.matrix.common.util.string.StringUtils;
import com.matrix.excel.annotation.ExcelDictFormat;
import com.matrix.excel.annotation.ExcelDynamicOptions;
import com.matrix.excel.annotation.ExcelEnumFormat;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.DataValidationHelper;
import org.apache.poi.ss.usermodel.Name;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.ss.util.WorkbookUtil;
import org.apache.poi.xssf.usermodel.XSSFDataValidation;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;

/**
 * Excel 下拉框处理器。
 *
 * <p>考虑到下拉选过多可能导致 Excel 打开缓慢的问题，只校验前 1000 行。
 * 即只有前 1000 行的数据可以用下拉框，超出的自行通过限制数据量或分 Sheet 解决。</p>
 *
 * <p>支持的注解：</p>
 * <ul>
 *   <li>{@link ExcelDictFormat} — 字典下拉</li>
 *   <li>{@link ExcelEnumFormat} — 枚举下拉</li>
 *   <li>{@link ExcelDynamicOptions} — 动态下拉（通过 {@link ExcelOptionsProvider} SPI）</li>
 * </ul>
 *
 * @author matrix
 */
@Slf4j
public class ExcelDownHandler implements SheetWriteHandler {

    private static final int FIRST_DATA_ROW_INDEX = 1;
    private static final int LAST_DATA_ROW_INDEX = 1000;

    /** 单选数据 Sheet 名 */
    private static final String OPTIONS_SHEET_NAME = "options";
    /** 联动选择数据 Sheet 名的头 */
    private static final String LINKED_OPTIONS_SHEET_NAME = "linkedOptions";

    /** 外部指定的下拉可选项 */
    private final List<DropDownOptions> dropDownOptions;
    private IDictService dictService;
    /** 当前单选进度 */
    private int currentOptionsColumnIndex;
    /** 当前联动选择进度 */
    private int currentLinkedOptionsSheetIndex;

    /**
     * 构造 Excel 下拉选处理器。
     *
     * @param options 外部指定的下拉选项
     */
    public ExcelDownHandler(List<DropDownOptions> options) {
        this.dropDownOptions = options;
        this.currentOptionsColumnIndex = 0;
        this.currentLinkedOptionsSheetIndex = 0;
    }

    @Override
    public void afterSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {
        Sheet sheet = writeSheetHolder.getSheet();
        DataValidationHelper helper = sheet.getDataValidationHelper();
        Workbook workbook = sheet.getWorkbook();
        Class<?> clazz = writeWorkbookHolder.getClazz();

        // 获取字段与列索引的映射
        Map<Integer, Field> sortedFieldMap = getSortedFieldMap(clazz);
        for (Map.Entry<Integer, Field> entry : sortedFieldMap.entrySet()) {
            Integer index = entry.getKey();
            Field field = entry.getValue();
            List<String> options = new ArrayList<>();

            if (field.isAnnotationPresent(ExcelDictFormat.class)) {
                // 字典下拉
                ExcelDictFormat format = field.getDeclaredAnnotation(ExcelDictFormat.class);
                String dictType = format.dictType();
                String converterExp = format.readConverterExp();
                if (StringUtils.isNotBlank(dictType)) {
                    Map<String, String> dictMap = getDictService().getAllDictByDictType(dictType);
                    if (CollUtil.isNotEmpty(dictMap)) {
                        options = new ArrayList<>(dictMap.values());
                    } else {
                        log.warn("字典类型 {} 无数据，跳过该列下拉", dictType);
                    }
                } else if (StringUtils.isNotBlank(converterExp)) {
                    List<String> strList = StringUtils.splitList(converterExp, StringUtils.SEPARATOR);
                    options = StreamUtils.toList(strList, s -> {
                        String[] itemArray = s.split("=", 2);
                        if (itemArray.length != 2) {
                            throw new ServiceException(500, "Excel转换表达式格式错误: " + s);
                        }
                        return itemArray[1];
                    });
                }
            } else if (field.isAnnotationPresent(ExcelEnumFormat.class)) {
                // 枚举下拉
                ExcelEnumFormat format = field.getDeclaredAnnotation(ExcelEnumFormat.class);
                List<Object> values = EnumUtil.getFieldValues(format.enumClass(), format.textField());
                options = StreamUtils.toList(values, Convert::toStr);
            } else if (field.isAnnotationPresent(ExcelDynamicOptions.class)) {
                // 动态下拉
                ExcelDynamicOptions dynamicOptions = field.getDeclaredAnnotation(ExcelDynamicOptions.class);
                @SuppressWarnings("unchecked")
                Class<? extends ExcelOptionsProvider> providerClass =
                    (Class<? extends ExcelOptionsProvider>) dynamicOptions.provider();
                ExcelOptionsProvider provider = SpringUtils.getBean(providerClass);
                if (provider != null) {
                    List<String> providerOptions = provider.getOptions();
                    if (CollUtil.isNotEmpty(providerOptions)) {
                        options = new ArrayList<>(providerOptions);
                    }
                }
            }

            if (ObjectUtil.isNotEmpty(options)) {
                int totalCharacter = options.stream().mapToInt(String::length).sum() + options.size();
                if (options.size() > 20 || totalCharacter > 255) {
                    dropDownWithSheet(helper, workbook, sheet, index, options);
                } else {
                    dropDownWithSimple(helper, sheet, index, options);
                }
            }
        }

        // 处理外部传入的级联下拉
        if (CollUtil.isEmpty(dropDownOptions)) {
            return;
        }
        dropDownOptions.forEach(everyOptions -> {
            if (CollUtil.isNotEmpty(everyOptions.getNextOptions())) {
                dropDownLinkedOptions(helper, workbook, sheet, everyOptions);
            } else if (CollUtil.isNotEmpty(everyOptions.getOptions()) && everyOptions.getOptions().size() > 10) {
                dropDownWithSheet(helper, workbook, sheet, everyOptions.getIndex(), everyOptions.getOptions());
            } else if (CollUtil.isNotEmpty(everyOptions.getOptions())) {
                dropDownWithSimple(helper, sheet, everyOptions.getIndex(), everyOptions.getOptions());
            }
        });
    }

    /**
     * 获取字段与列索引的映射（按 ExcelProperty.index 排序）。
     */
    private Map<Integer, Field> getSortedFieldMap(Class<?> clazz) {
        Map<Integer, Field> fieldMap = new TreeMap<>();
        Field[] fields = ReflectUtils.getFields(clazz);
        int defaultIndex = 0;
        for (Field field : fields) {
            ExcelProperty excelProperty = field.getAnnotation(ExcelProperty.class);
            if (excelProperty != null) {
                int index = excelProperty.index() != -1 ? excelProperty.index() : defaultIndex;
                fieldMap.putIfAbsent(index, field);
                defaultIndex++;
            }
        }
        return fieldMap;
    }

    /**
     * 简单下拉框 —— 直接将可选项拼接为数据校验值。
     */
    private void dropDownWithSimple(DataValidationHelper helper, Sheet sheet, Integer celIndex, List<String> value) {
        if (ObjectUtil.isEmpty(value)) {
            return;
        }
        markOptionsToSheet(helper, sheet, celIndex,
            helper.createExplicitListConstraint(ArrayUtil.toArray(value, String.class)));
    }

    /**
     * 级联下拉框 —— 使用额外 Sheet 存储联动数据。
     */
    private void dropDownLinkedOptions(DataValidationHelper helper, Workbook workbook, Sheet sheet,
                                        DropDownOptions options) {
        String linkedOptionsSheetName = String.format("%s_%d", LINKED_OPTIONS_SHEET_NAME, currentLinkedOptionsSheetIndex);
        Sheet linkedOptionsDataSheet = workbook.createSheet(WorkbookUtil.createSafeSheetName(linkedOptionsSheetName));
        workbook.setSheetHidden(workbook.getSheetIndex(linkedOptionsDataSheet), true);

        List<String> firstOptions = new ArrayList<>(options.getOptions());
        if (CollUtil.isEmpty(firstOptions)) {
            return;
        }
        validateLinkedOptionNames(firstOptions);
        Map<String, List<String>> secoundOptionsMap = new HashMap<>();
        options.getNextOptions().forEach((k, v) -> secoundOptionsMap.put(k, new ArrayList<>(v)));

        List<String> columnNames = new ArrayList<>();
        Row firstRow = linkedOptionsDataSheet.createRow(0);
        for (int columnIndex = 0; columnIndex < firstOptions.size(); columnIndex++) {
            String columnName = firstOptions.get(columnIndex);
            firstRow.createCell(columnIndex).setCellValue(columnName);
            columnNames.add(columnName);
        }

        // 创建一级名称管理器
        Name name = workbook.createName();
        name.setNameName(linkedOptionsSheetName);
        String firstOptionsFunction = String.format("%s!$%s$1:$%s$1",
            linkedOptionsSheetName,
            getExcelColumnName(0),
            getExcelColumnName(firstOptions.size() - 1));
        name.setRefersToFormula(firstOptionsFunction);
        markOptionsToSheet(helper, sheet, options.getIndex(),
            helper.createFormulaListConstraint(linkedOptionsSheetName));

        // 创建二级名称管理器
        for (int columIndex = 0; columIndex < columnNames.size(); columIndex++) {
            String firstOptionsColumnName = getExcelColumnName(columIndex);
            String thisFirstOptionsValue = columnNames.get(columIndex);

            Name sonName = workbook.createName();
            sonName.setNameName(thisFirstOptionsValue);
            String sonFunction = String.format("%s!$%s$2:$%s$%d",
                linkedOptionsSheetName,
                firstOptionsColumnName,
                firstOptionsColumnName,
                Math.max(Optional.ofNullable(secoundOptionsMap.get(thisFirstOptionsValue))
                    .orElseGet(ArrayList::new).size(), 1) + 1);
            sonName.setRefersToFormula(sonFunction);

            String mainSheetFirstOptionsColumnName = getExcelColumnName(options.getIndex());
            for (int i = FIRST_DATA_ROW_INDEX; i <= LAST_DATA_ROW_INDEX; i++) {
                String secondOptionsFunction = String.format("=INDIRECT(%s%d)", mainSheetFirstOptionsColumnName, i + 1);
                markLinkedOptionsToSheet(helper, sheet, i, options.getNextIndex(),
                    helper.createFormulaListConstraint(secondOptionsFunction));
            }
        }

        // 将二级数据处理为按行填充
        Map<Integer, List<String>> columnValueMap = new HashMap<>();
        int currentRow = 1;
        while (currentRow >= 0) {
            boolean flag = false;
            List<String> rowData = new ArrayList<>();
            for (String columnName : columnNames) {
                List<String> data = secoundOptionsMap.get(columnName);
                if (CollUtil.isEmpty(data)) {
                    rowData.add(" ");
                    continue;
                }
                String str = data.removeFirst();
                rowData.add(str);
                flag = true;
            }
            columnValueMap.put(currentRow, rowData);
            if (flag) {
                currentRow++;
            } else {
                currentRow = -1;
            }
        }

        columnValueMap.forEach((rowIndex, rowValues) -> {
            Row row = linkedOptionsDataSheet.createRow(rowIndex);
            for (int columnIndex = 0; columnIndex < rowValues.size(); columnIndex++) {
                String rowValue = rowValues.get(columnIndex);
                if (StrUtil.isNotBlank(rowValue)) {
                    row.createCell(columnIndex).setCellValue(rowValue);
                }
            }
        });

        currentLinkedOptionsSheetIndex++;
    }

    private void validateLinkedOptionNames(List<String> firstOptions) {
        Set<String> names = new HashSet<>();
        for (String firstOption : firstOptions) {
            DropDownOptions.validateOptionValue(firstOption);
            if (!names.add(firstOption)) {
                throw new ServiceException(500, "级联下拉一级选项重复：" + firstOption);
            }
        }
    }

    /**
     * 额外 Sheet 形式的普通下拉框 —— 用于选项过多时提升 Excel 打开效率。
     */
    private void dropDownWithSheet(DataValidationHelper helper, Workbook workbook, Sheet sheet,
                                    Integer celIndex, List<String> value) {
        String tmpOptionsSheetName = OPTIONS_SHEET_NAME + "_" + currentOptionsColumnIndex;
        Sheet simpleDataSheet = Optional.ofNullable(workbook.getSheet(WorkbookUtil.createSafeSheetName(tmpOptionsSheetName)))
            .orElseGet(() -> workbook.createSheet(WorkbookUtil.createSafeSheetName(tmpOptionsSheetName)));
        workbook.setSheetHidden(workbook.getSheetIndex(simpleDataSheet), true);

        for (int i = 0; i < value.size(); i++) {
            int finalI = i;
            Row row = Optional.ofNullable(simpleDataSheet.getRow(i))
                .orElseGet(() -> simpleDataSheet.createRow(finalI));
            Cell cell = Optional.ofNullable(row.getCell(0))
                .orElseGet(() -> row.createCell(0));
            cell.setCellValue(value.get(i));
        }

        Name name = workbook.createName();
        String nameName = String.format("%s_%d", tmpOptionsSheetName, celIndex);
        name.setNameName(nameName);
        String function = String.format("%s!$%s$1:$%s$%d",
            tmpOptionsSheetName,
            getExcelColumnName(0),
            getExcelColumnName(0),
            value.size());
        name.setRefersToFormula(function);
        markOptionsToSheet(helper, sheet, celIndex, helper.createFormulaListConstraint(nameName));
        currentOptionsColumnIndex++;
    }

    /**
     * 挂载一级下拉验证到列。
     */
    private void markOptionsToSheet(DataValidationHelper helper, Sheet sheet, Integer celIndex,
                                    DataValidationConstraint constraint) {
        CellRangeAddressList addressList = new CellRangeAddressList(FIRST_DATA_ROW_INDEX, LAST_DATA_ROW_INDEX,
            celIndex, celIndex);
        markDataValidationToSheet(helper, sheet, constraint, addressList);
    }

    /**
     * 挂载二级下拉验证到单元格。
     */
    private void markLinkedOptionsToSheet(DataValidationHelper helper, Sheet sheet, Integer rowIndex,
                                          Integer celIndex, DataValidationConstraint constraint) {
        CellRangeAddressList addressList = new CellRangeAddressList(rowIndex, rowIndex, celIndex, celIndex);
        markDataValidationToSheet(helper, sheet, constraint, addressList);
    }

    /**
     * 应用数据校验。
     */
    private void markDataValidationToSheet(DataValidationHelper helper, Sheet sheet,
                                           DataValidationConstraint constraint, CellRangeAddressList addressList) {
        DataValidation dataValidation = helper.createValidation(constraint, addressList);
        if (dataValidation instanceof XSSFDataValidation) {
            dataValidation.setSuppressDropDownArrow(true);
            dataValidation.setErrorStyle(DataValidation.ErrorStyle.STOP);
            dataValidation.createErrorBox("提示", "此值与单元格定义数据不一致");
            dataValidation.setShowErrorBox(true);
            dataValidation.createPromptBox("填写说明：", "填写内容只能为下拉中数据，其他数据将导致导入失败");
            dataValidation.setShowPromptBox(true);
        } else {
            dataValidation.setSuppressDropDownArrow(false);
        }
        sheet.addValidationData(dataValidation);
    }

    /**
     * 依据列 index 转换为 Excel 列名（A、B、...、AA、AB...）。
     */
    private String getExcelColumnName(int columnIndex) {
        return CellReference.convertNumToColString(columnIndex);
    }

    private IDictService getDictService() {
        if (dictService == null) {
            try {
                dictService = SpringUtils.getBean(IDictService.class);
            } catch (Exception e) {
                log.debug("未找到 IDictService 实现，字典下拉不可用");
                dictService = new IDictService() {
                    @Override
                    public String getDictLabel(String dictType, String dictValue, String separator) {
                        return null;
                    }

                    @Override
                    public String getDictValue(String dictType, String dictLabel, String separator) {
                        return null;
                    }
                };
            }
        }
        return dictService;
    }
}
