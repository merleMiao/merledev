//package com.miao.quartz.utils;
//
//import com.alibaba.druid.pool.DruidDataSource;
//import com.alibaba.druid.pool.DruidDataSourceFactory;
//import com.merle.basic.Config;
//import com.merle.io.JsonUtils;
//import org.apache.commons.lang.StringUtils;
//import org.apache.ibatis.io.Resources;
//import org.apache.ibatis.session.SqlSessionFactory;
//import org.apache.ibatis.session.SqlSessionFactoryBuilder;
//
//import javax.sql.DataSource;
//import java.io.*;
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.Properties;
//
//
//public class DbFactory {
//
//    private static DataSource dataSource = null;
//
//    private static SqlSessionFactory sessionFactory = null;
//
//    private static class SingletonHolder {
//        static final DbFactory instance = new DbFactory();
//    }
//
//    private DbFactory() {
//        System.out.println(Config.getLocalProperty("db.prop"));
//        Properties properties = loadPropertyFile(Config.getLocalProperty("db.prop"));
//        System.out.println(JsonUtils.toJson(properties));
//        try {
//            dataSource = (DruidDataSource) DruidDataSourceFactory.createDataSource(properties);
//            sessionFactory = buildSessionFactory();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public static DbFactory getInstance() {
//        return SingletonHolder.instance;
//    }
//
//    public DataSource getDataSource() {
//        return dataSource;
//    }
//
//    public SqlSessionFactory getSessionFactory() {
//        return sessionFactory;
//    }
//
//    public static Properties loadPropertyFile(String fullFile) {
//        String webRootPath = null;
//        if (StringUtils.isBlank(fullFile)) {
//            throw new IllegalArgumentException("Properties file path can not be null : " + fullFile);
//        }
//        webRootPath = DruidQuartzProvider.class.getClassLoader().getResource("").getPath();
//        webRootPath = new File(webRootPath).getPath();
//        InputStream is = null;
//        Properties p = null;
//        try {
//            String path = webRootPath + File.separator + fullFile;
//            System.out.println(path);
//            is = new FileInputStream(new File(path));
//            p = new Properties();
//            p.load(is);
//        } catch (FileNotFoundException e) {
//            throw new IllegalArgumentException("Properties file not found: " + fullFile);
//        } catch (IOException e) {
//            throw new IllegalArgumentException("Properties file can not be loading: " + fullFile);
//        } finally {
//            IoUtils.close(is);
//        }
//        return p;
//    }
//
//    private static SqlSessionFactory buildSessionFactory() {
//        SqlSessionFactory sessionFactory = null;
//        try {
//            String resource = Config.getLocalProperty("db.mybatis");
//            System.out.println(resource);
//            Reader reader = Resources.getResourceAsReader(resource);
//            BufferedReader br = new BufferedReader(reader);
//            Reader res = Resources.getResourceAsReader(resource);
//            sessionFactory = new SqlSessionFactoryBuilder().build(res);
//            System.out.println("sessionFactory:" + (sessionFactory==null));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return sessionFactory;
//    }
//
//    public static void main(String[] args) throws SQLException {
//        DbFactory df = DbFactory.getInstance();
//        Connection ds = df.getSessionFactory().openSession().getConnection();
//        PreparedStatement ps = ds.prepareStatement("SELECT 1 FROM DUAL");
//        ResultSet rs = ps.executeQuery();
//        while (rs.next()) {
//            System.out.println(rs.getString(1));
//        }
//
//    }
//}
