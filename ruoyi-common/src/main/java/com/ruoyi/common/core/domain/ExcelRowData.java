package com.ruoyi.common.core.domain;

import java.io.Serializable;

/**
 * @description:
 * @author: junmamba
 * @create: 2024-05-04 15:09
 */
public class ExcelRowData implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer rowIndex;

    public Integer getRowIndex() {
        return rowIndex;
    }

    public void setRowIndex(Integer rowIndex) {
        this.rowIndex = rowIndex;
    }
}
