//package com.miao.quartz.utils;
//
//import org.apache.ibatis.datasource.DataSourceFactory;
//
//import javax.sql.DataSource;
//import java.util.Properties;
//
//public class DruidToMybetisProxy implements DataSourceFactory {
//
//    DataSource ds = null;
//
//    @Override
//    public DataSource getDataSource() {
//        return ds;
//    }
//
//    @Override
//    public void setProperties(final Properties props) {
//        try {
//            ds = com.alibaba.druid.pool.DruidDataSourceFactory.createDataSource(props);
//        } catch (final RuntimeException e) {
//            throw e;
//        } catch (final Exception e) {
//            throw new RuntimeException("init datasource error", e);
//        }
//    }
//}