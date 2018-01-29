package com.flowable.common.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 26223
 * @time 2016年10月10日
 * @email lukw@eastcom-sw.com
 */
public class PageHelper<T> {

    private int page = -1;
    private int rows = -1;
    private long count;
    private String sort;
    private String order = "ASC";

    private List<T> list = new ArrayList<T>();

    public PageHelper(int page, int rows) {
        this.page = page;
        this.rows = rows;
    }

    public PageHelper() {
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public int getFirstRow() {
        return (this.page - 1) * this.rows > 0 ? (this.page - 1) * this.rows : -1;
    }

    public int getMaxRow() {
        return this.page * this.rows + 1 > 0 ? this.page * this.rows + 1 : -1;
    }
}
