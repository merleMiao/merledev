package com.merle.dbCompare;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by merle on 2016/8/10.
 */
public class DBCompareUtilV2 {

    public static final String name = "com.mysql.jdbc.Driver";
    public static Connection conn = null;

    public static void main(String[] args) throws Exception {
        String url1 = "jdbc:mysql://42.62.32.184:3306/db_fbus";
        String url2 = "jdbc:mysql://42.62.32.179:3306/db_fbus";
        String userName = "fbus";
        String password = "fbus";

        Connection connection1 = getConnection(url1, userName, password);
        Connection connection2 = getConnection(url2, userName, password);
        Set<String> db1Tables = getTablesV2(connection1);
        Set<String> db2Tables = getTablesV2(connection2);
        System.out.println("=======================diff tables=======================");
        System.out.println(url1 + " yes,"+ url2 +" no:");
        for (String name : diff(db1Tables, db2Tables)){
            System.out.println(name);
        }
        System.out.println("-----------------------");
        System.out.println(url1 + " no,"+ url2 +" yes:");
        for(String name : diff(db2Tables, db1Tables)){
            System.out.println(name);
        }
        System.out.println("=======================diff columns=======================");
        for(String name : db1Tables){
            if(diff(db1Tables, db2Tables) != null){
                if(!diff(db1Tables, db2Tables).contains(name)){
                    Set<String> db1Columns = getColumnsV2(connection1, name);
                    Set<String> db2Columns = getColumnsV2(connection2, name);
                    boolean flag = false;
                    for(String column : diff(db1Columns, db2Columns)){
                        System.out.println("diff columns for:" + name);
                        System.out.println(url1 + " yes,"+ url2 +" no:" + column);
                        flag = true;
                    }
                    for(String column : diff(db2Columns, db1Columns)){
                        flag = true;
                        System.out.println("diff columns for:" + name);
                        System.out.println(url1 + " no,"+ url2 +" yes:" + column);
                    }
                    if(flag){
                        System.out.println("------------------------------");
                    }
                }
            }else{
                boolean flag = false;
                Set<String> db1Columns = getColumnsV2(connection1, name);
                Set<String> db2Columns = getColumnsV2(connection2, name);
                for(String column : diff(db1Columns, db2Columns)){
                    System.out.println("diff columns for:" + name);
                    System.out.println(url1 + " yes,"+ url2 +" no:" + column);
                    flag = true;
                }
                for(String column : diff(db2Columns, db1Columns)){
                    System.out.println("diff columns for:" + name);
                    System.out.println(url1 + " no,"+ url2 +" yes:" + column);
                    flag = true;
                }
                if(flag){
                    System.out.println("------------------------------");
                }
            }
        }

        System.out.println("=======================diff procedures=======================");
        Set<String> db1Procedures = getProceduresV2(connection1);
        Set<String> db2Procedures = getProceduresV2(connection2);
        System.out.println(url1 + " yes,"+ url2 +" no:");
        for (String name : diff(db1Procedures, db2Procedures)){
            System.out.println(name);
        }
        System.out.println("-----------------------");
        System.out.println(url1 + " no,"+ url2 +" yes:");
        for(String name : diff(db2Procedures, db1Procedures)){
            System.out.println(name);
        }

        System.out.println("=======================diff functions=======================");
        Set<String> db1Functions = getFunctionsV2(connection1);
        Set<String> db2Functions = getFunctionsV2(connection2);
        System.out.println(url1 + " yes,"+ url2 +" no:");
        for (String name : diff(db1Functions, db2Functions)){
            System.out.println(name);
        }
        System.out.println("-----------------------");
        System.out.println(url1 + " no,"+ url2 +" yes:");
        for(String name : diff(db2Functions, db1Functions)){
            System.out.println(name);
        }

    }

    public static Set<String> diff(Set<String> set1, Set<String> set2) {
        if (set1 == null) {
            set1 = new HashSet();
        }
        if (set2 == null) {
            set2 = new HashSet();
        }
        Set result = new HashSet();
        result.addAll(set1);
        result.removeAll(set2);
        return result;
    }

    public static Connection getConnection(String url, String userName, String password){
        try {
            Class.forName(name);
            conn = DriverManager.getConnection(url, userName, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }


    public static Set<String> getTablesV2(Connection connection) throws SQLException {
        Set<String> tbls = new HashSet<String>();
        DatabaseMetaData dbmd= connection.getMetaData();
        ResultSet tables = dbmd.getTables(null, null, null, new String[]{"table", "view"});
        while (tables.next()){
            tbls.add(tables.getString("TABLE_NAME"));
        }
        return tbls;
    }

    public static Set<String> getColumnsV2(Connection connection, String tableName) throws SQLException {
        Set<String> cols = new HashSet<String>();
        DatabaseMetaData dbmd= connection.getMetaData();
        ResultSet columns = dbmd.getColumns(null, null, tableName, "%");
        while (columns.next()){
            cols.add(columns.getString("COLUMN_NAME") + ":" + columns.getString("TYPE_NAME"));
        }
        return cols;
    }

    public static Set<String> getProceduresV2(Connection connection) throws SQLException {
        Set<String> pros = new HashSet<String>();
        DatabaseMetaData dbmd= connection.getMetaData();
        ResultSet procedures = dbmd.getProcedures(null, null, "%");
        while (procedures.next()){
            pros.add(procedures.getString("PROCEDURE_NAME"));
        }
        return pros;
    }

    public static Set<String> getFunctionsV2(Connection connection) throws SQLException {
        Set<String> funcs = new HashSet<String>();
        DatabaseMetaData dbmd= connection.getMetaData();
        ResultSet functions = dbmd.getFunctions(null, null, "%");
        while (functions.next()){
            funcs.add(functions.getString("FUNCTION_NAME"));
        }
        return funcs;
    }
}
