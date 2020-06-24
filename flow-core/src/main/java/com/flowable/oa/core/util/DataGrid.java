package com.flowable.oa.core.util;

import java.io.Serializable;
import java.util.List;

/**
 * 表格组件
 *
 * @author lukew
 * @eamil 13507615840@163.com
 * @create 2018-09-06 20:06
 **/

public class DataGrid<T> implements Serializable {

    private static final long serialVersionUID = -6368482161778607423L;

    private List<T> rows;

    private long total;

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public static <T> DataGrid<T> getGrid(List<T> list, long total){

        DataGrid<T> dataGrid = new DataGrid<>();
        dataGrid.setRows(list);
        dataGrid.setTotal(total);
        return dataGrid;
    }

}
