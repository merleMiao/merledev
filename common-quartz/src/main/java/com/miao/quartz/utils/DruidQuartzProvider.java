//package com.miao.quartz.utils;
//
//import org.quartz.utils.ConnectionProvider;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import javax.sql.DataSource;
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//
//public class DruidQuartzProvider implements ConnectionProvider {
//
//    private DataSource ds = null;
//
//    static Logger logger = LoggerFactory.getLogger(DruidQuartzProvider.class);
//
//    public static void main(String[] args) {
//        ConnectionProvider connectionProvider = new DruidQuartzProvider();
//        try {
//            Connection sqlSession = connectionProvider.getConnection();
//            PreparedStatement ps = sqlSession.prepareStatement("show tables;");
//            ResultSet rs = ps.executeQuery();
//            while (rs.next()) {
//                System.out.println(rs.getString(1));
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    public Connection getConnection() throws SQLException {
//        if (ds == null) {
//            ds = DbFactory.getInstance().getDataSource();
//        }
//        Connection connection = ds.getConnection();
//        logger.info("connection is null[{}]", connection == null);
//        return connection;
//    }
//
//    @Override
//    public void shutdown() throws SQLException {
//
//    }
//
//    @Override
//    public void initialize() throws SQLException {
//        ds = DbFactory.getInstance().getDataSource();
//    }
//}