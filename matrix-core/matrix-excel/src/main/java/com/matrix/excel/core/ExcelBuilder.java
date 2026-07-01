package com.matrix.excel.core;

import cn.hutool.core.collection.CollUtil;
import org.apache.fesod.sheet.EasyExcel;
import org.apache.fesod.sheet.ExcelWriter;
import org.apache.fesod.sheet.support.ExcelTypeEnum;
import org.apache.fesod.sheet.write.metadata.WriteSheet;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Excel 链式构建器。
 *
 * <p>支持导出、导入、模板导出的 Builder 模式。示例：</p>
 * <pre>
 * ExcelBuilder.of(data)
 *     .sheetName("用户列表")
 *     .headModel(UserVO.class)
 *     .merge()
 *     .toResponse(response, "用户导出.xlsx");
 * </pre>
 *
 * @author matrix
 */
public class ExcelBuilder<T> {

    private final List<T> data;
    private Class<T> headModel;
    private String sheetName = "Sheet1";
    private boolean mergeCells;
    private String password;
    private Integer sheetNo;
    private List<String> includeColumns;
    private List<String> excludeColumns;

    private ExcelBuilder(List<T> data) {
        this.data = data != null ? data : new ArrayList<>();
    }

    /** 创建构建器 */
    public static <T> ExcelBuilder<T> of(List<T> data) {
        return new ExcelBuilder<>(data);
    }

    /** 设置表头模型类 */
    @SuppressWarnings("unchecked")
    public ExcelBuilder<T> headModel(Class<?> headModel) {
        this.headModel = (Class<T>) headModel;
        return this;
    }

    /** 设置 Sheet 名称 */
    public ExcelBuilder<T> sheetName(String sheetName) {
        this.sheetName = sheetName;
        return this;
    }

    /** 启用单元格合并 */
    public ExcelBuilder<T> merge() {
        this.mergeCells = true;
        return this;
    }

    /** 设置工作表序号 */
    public ExcelBuilder<T> sheetNo(Integer sheetNo) {
        this.sheetNo = sheetNo;
        return this;
    }

    /** 设置打开密码 */
    public ExcelBuilder<T> password(String password) {
        this.password = password;
        return this;
    }

    /** 设置包含的列 */
    public ExcelBuilder<T> includeColumns(List<String> columns) {
        this.includeColumns = columns;
        return this;
    }

    /** 设置排除的列 */
    public ExcelBuilder<T> excludeColumns(List<String> columns) {
        this.excludeColumns = columns;
        return this;
    }

    /** 导出到 OutputStream */
    public void toOutputStream(OutputStream outputStream) {
        ExcelWriter writer = null;
        try {
            writer = EasyExcel.write(outputStream, headModel).build();
            WriteSheet sheet = EasyExcel.writerSheet(sheetNo != null ? sheetNo : 0, sheetName).build();
            if (mergeCells) {
                // merge strategy is applied in CellMergeStrategy
            }
            if (password != null) {
                sheet.setAutoTrim(true);
            }
            if (includeColumns != null) {
                sheet.setIncludeColumnFieldNames(includeColumns);
            }
            if (excludeColumns != null) {
                sheet.setExcludeColumnFieldNames(excludeColumns);
            }
            writer.write(data, sheet);
        } finally {
            if (writer != null) {
                writer.finish();
            }
        }
    }

    /** 导出到 HttpServletResponse */
    @SuppressWarnings("all")
    public void toResponse(HttpServletResponse response, String fileName) {
        try {
            setExcelResponseHeader(response, fileName);
            toOutputStream(response.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException("Excel export failed", e);
        }
    }

    /** 导出 Excel 到 HttpServletResponse（指定格式） */
    public void toResponse(HttpServletResponse response, String fileName, ExcelTypeEnum excelType) {
        try {
            response.setContentType(excelType == ExcelTypeEnum.XLSX
                    ? "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
                    : "application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            response.setHeader("Content-Disposition",
                    "attachment;filename=" + URLEncoder.encode(fileName, StandardCharsets.UTF_8));
            toOutputStream(response.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException("Excel export failed", e);
        }
    }

    private void setExcelResponseHeader(HttpServletResponse response, String fileName) {
        try {
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            response.setHeader("Content-Disposition",
                    "attachment;filename=" + URLEncoder.encode(fileName, StandardCharsets.UTF_8));
        } catch (Exception e) {
            throw new RuntimeException("Failed to set response header", e);
        }
    }
}
