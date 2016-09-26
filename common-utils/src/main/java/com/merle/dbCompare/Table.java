package com.merle.dbCompare;

import java.util.HashMap;

/**
 * Created by merle on 2016/8/10.
 */
public class Table {
    public String tableName;
    public HashMap<String,Column> columns = new HashMap<String, Column>();

    public Table(String tableName) {
        this.tableName = tableName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public HashMap<String, Column> getColumns() {
        return columns;
    }

    public void setColumns(HashMap<String, Column> columns) {
        this.columns = columns;
    }
}
