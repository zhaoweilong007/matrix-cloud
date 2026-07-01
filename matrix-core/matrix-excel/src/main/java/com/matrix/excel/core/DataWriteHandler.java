package com.matrix.excel.core;

import cn.hutool.core.collection.CollUtil;
import org.apache.fesod.sheet.annotation.ExcelProperty;
import org.apache.fesod.sheet.metadata.data.DataFormatData;
import org.apache.fesod.sheet.metadata.data.WriteCellData;
import org.apache.fesod.sheet.util.StyleUtil;
import org.apache.fesod.sheet.write.handler.CellWriteHandler;
import org.apache.fesod.sheet.write.handler.SheetWriteHandler;
import org.apache.fesod.sheet.write.handler.context.CellWriteHandlerContext;
import org.apache.fesod.sheet.write.metadata.holder.WriteSheetHolder;
import org.apache.fesod.sheet.write.metadata.style.WriteCellStyle;
import org.apache.fesod.sheet.write.metadata.style.WriteFont;
import com.matrix.common.util.reflect.ReflectUtils;
import com.matrix.excel.annotation.ExcelNotation;
import com.matrix.excel.annotation.ExcelRequired;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Comment;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Excel 批注与必填样式处理器。
 *
 * <p>处理 {@link ExcelNotation} 和 {@link ExcelRequired} 注解：</p>
 * <ul>
 *   <li>{@code @ExcelNotation("说明文字")} — 在表头添加批注</li>
 *   <li>{@code @ExcelRequired} — 表头字体标红（默认红色）</li>
 * </ul>
 *
 * @author matrix
 */
public class DataWriteHandler implements SheetWriteHandler, CellWriteHandler {

    /** 批注映射（表头名称 → 批注内容） */
    private final Map<String, String> notationMap;

    /** 必填列字体颜色映射（表头名称 → 颜色索引） */
    private final Map<String, Short> headColumnMap;

    /**
     * 构造批注与必填样式处理器。
     *
     * @param clazz 表头模型类
     */
    public DataWriteHandler(Class<?> clazz) {
        notationMap = getNotationMap(clazz);
        headColumnMap = getRequiredMap(clazz);
    }

    @Override
    public void afterCellDispose(CellWriteHandlerContext context) {
        if (CollUtil.isEmpty(notationMap) && CollUtil.isEmpty(headColumnMap)) {
            return;
        }
        WriteCellData<?> cellData = context.getFirstCellData();
        if (cellData == null) {
            return;
        }
        WriteCellStyle writeCellStyle = cellData.getOrCreateStyle();

        if (context.getHead()) {
            // 单元格设置为文本格式
            DataFormatData dataFormatData = new DataFormatData();
            dataFormatData.setIndex((short) 49);
            writeCellStyle.setDataFormatData(dataFormatData);

            Cell cell = context.getCell();
            WriteSheetHolder writeSheetHolder = context.getWriteSheetHolder();
            Sheet sheet = writeSheetHolder.getSheet();
            Workbook workbook = sheet.getWorkbook();
            Drawing<?> drawing = sheet.createDrawingPatriarch();

            // 设置标题字体样式
            WriteFont headWriteFont = new WriteFont();
            headWriteFont.setBold(true);
            if (CollUtil.isNotEmpty(headColumnMap) && headColumnMap.containsKey(cell.getStringCellValue())) {
                headWriteFont.setColor(headColumnMap.get(cell.getStringCellValue()));
            }
            writeCellStyle.setWriteFont(headWriteFont);
            CellStyle cellStyle = StyleUtil.buildCellStyle(workbook, null, writeCellStyle);
            cell.setCellStyle(cellStyle);

            // 添加批注
            if (CollUtil.isNotEmpty(notationMap) && notationMap.containsKey(cell.getStringCellValue())) {
                String notationContext = notationMap.get(cell.getStringCellValue());
                Comment comment = drawing.createCellComment(
                    new XSSFClientAnchor(0, 0, 0, 0,
                        (short) cell.getColumnIndex(), 0, (short) 5, 5));
                comment.setString(new XSSFRichTextString(notationContext));
                cell.setCellComment(comment);
            }
        }
    }

    /**
     * 获取必填列的字体颜色映射。
     */
    private static Map<String, Short> getRequiredMap(Class<?> clazz) {
        Map<String, Short> requiredMap = new HashMap<>();
        Field[] fields = ReflectUtils.getFields(clazz);
        for (Field field : fields) {
            if (!field.isAnnotationPresent(ExcelRequired.class)) {
                continue;
            }
            ExcelRequired excelRequired = field.getAnnotation(ExcelRequired.class);
            ExcelProperty excelProperty = field.getAnnotation(ExcelProperty.class);
            if (excelProperty == null || excelProperty.value().length == 0) {
                continue;
            }
            requiredMap.put(excelProperty.value()[0], excelRequired.fontColor().getIndex());
        }
        return requiredMap;
    }

    /**
     * 获取批注映射。
     */
    private static Map<String, String> getNotationMap(Class<?> clazz) {
        Map<String, String> notationMap = new HashMap<>();
        Field[] fields = ReflectUtils.getFields(clazz);
        for (Field field : fields) {
            if (!field.isAnnotationPresent(ExcelNotation.class)) {
                continue;
            }
            ExcelNotation excelNotation = field.getAnnotation(ExcelNotation.class);
            ExcelProperty excelProperty = field.getAnnotation(ExcelProperty.class);
            if (excelProperty == null || excelProperty.value().length == 0) {
                continue;
            }
            notationMap.put(excelProperty.value()[0], excelNotation.value());
        }
        return notationMap;
    }
}
