package com.merle.dbCompare;

import java.sql.*;
import java.util.*;

/**
 * Created by merle on 2016/8/10.
 */
public class DBCompareUtil {

    public static final String name = "com.mysql.jdbc.Driver";
    public static Connection conn = null;

    public static void main(String[] args) throws Exception {
        String url1 = "jdbc:mysql://42.62.32.184:3306/db_fbus";
        String url2 = "jdbc:mysql://42.62.32.179:3306/db_fbus";
        String userName = "fbus";
        String password = "fbus";

        Connection connection = getConnection(url1, userName, password);
        Set<String> db1 = getTables(connection);
        Connection connection1 = getConnection(url2, userName, password);
        Set<String> db2 = getTables(connection1);
        System.out.println("------------------------------------------------");
        System.out.println(url1 + " yes,"+ url2 +" no:");

        for (String name : diff(db1, db2)){
            System.out.println(name);
        }
        System.out.println("------------------------------------------------");
        System.out.println(url1 + " no,"+ url2 +" yes:");
        for(String name : diff(db2, db1)){
            System.out.println(name);
        }
        System.out.println("------------------------------------------------");
        for(String name : db1){
            if(diff(db1, db2) != null){
                if(!diff(db1, db2).contains(name)){
                    Set<String> columns1 = getColunms(connection, name);
                    Set<String> columns2 = getColunms(connection1, name);
                    for(String column : diff(columns1, columns2)){
                        System.out.println("diff columns for:" + name);
                        System.out.println(url1 + " yes,"+ url2 +" no:" + column);
                    }
                    for(String column : diff(columns2, columns1)){
                        System.out.println("diff columns for:" + name);
                        System.out.println(url1 + " no,"+ url2 +" yes:" + column);
                    }
                }
            }else{
                Set<String> columns1 = getColunms(connection, name);
                Set<String> columns2 = getColunms(connection1, name);
                for(String column : diff(columns1, columns2)){
                    System.out.println("diff columns for:" + name);
                    System.out.println(url1 + " yes,"+ url2 +" no:" + column);
                }
                for(String column : diff(columns2, columns1)){
                    System.out.println("diff columns for:" + name);
                    System.out.println(url1 + " no,"+ url2 +" yes:" + column);
                }
            }

        }
    }

    public static Set<String> getTables(Connection connection) throws Exception{
        String sSql = "show tables";
        PreparedStatement preparedStatement = connection.prepareStatement(sSql);
        ResultSet resultSet = preparedStatement.executeQuery();
        ResultSetMetaData metaData = resultSet.getMetaData();
        Set<String> list = new HashSet<String>();
        while (resultSet.next()){
            list.add(resultSet.getString(1));
        }
        return list;
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

    public static Set<String> getColunms(Connection connection, String tableName) throws SQLException {
        Set<String> columns = new HashSet<String>();
        String sql = "select * from " + tableName + " limit 1";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();
        ResultSetMetaData metaData = resultSet.getMetaData();
        for(int i = 1; i <= metaData.getColumnCount(); i ++){
            columns.add(metaData.getColumnName(i) + ":" + metaData.getColumnTypeName(i));
        }
        return columns;
    }

    public static Set<String> getIndexes(Connection connection, String tableName) throws SQLException {
        Set<String> indexes = new HashSet<String>();
        DatabaseMetaData metaData1 = connection.getMetaData();
        ResultSet indexInfo = metaData1.getIndexInfo(null, null, tableName, false, false);
        while (indexInfo.next()){
            System.out.println("index name:" + indexInfo.getString("INDEX_NAME"));
        }
        return indexes;
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


    public static void getTablesV2(Connection connection) throws SQLException {
        DatabaseMetaData dbmd= connection.getMetaData();
        ResultSet tables = dbmd.getTables(null, null, null, new String[]{"table", "view"});
        while (tables.next()){
            System.out.println(tables.getString("TABLE_NAME"));
        }
    }
}
