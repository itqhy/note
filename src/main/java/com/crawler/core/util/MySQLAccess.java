package com.crawler.core.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by Administrator on 2017/8/28.
 */
public class MySQLAccess {
    private static Logger logger = LoggerFactory.getLogger(MySQLAccess.class);
    private static final MySQLAccess access = new MySQLAccess();

    public static MySQLAccess getAccess() {
        return access;
    }

    private Statement statement = null;
    private Connection connection = null;

    private MySQLAccess() {

    }

    /**
     * 连接数据库
     *
     * @param database 数据库名
     * @return
     */
    private boolean ConnectToDatabase(String database) {
        try {
            Class.forName(MySqlValues.DRIVER_MYSQL);
            connection = DriverManager.getConnection(MySqlValues.URL + database + MySqlValues.SETTINGS,
                    MySqlValues.USER, MySqlValues.PASSWORD);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        } catch (SQLException e) {
            CrestDatabase(database);
            try {
                connection = DriverManager.getConnection(MySqlValues.URL + database + MySqlValues.SETTINGS,
                        MySqlValues.USER, MySqlValues.PASSWORD);
                return true;
            } catch (SQLException ex) {
                logger.info("获取数据库连接失败");
                return false;
            }

        }
    }

    /**
     * 创建数据库
     *
     * @param database 数据库名
     * @return
     */
    public boolean CrestDatabase(String database) {
        try {
            connection = DriverManager.getConnection(MySqlValues.URL + MySqlValues.SETTINGS,
                    MySqlValues.USER, MySqlValues.PASSWORD);
            statement = connection.createStatement();
            String sql = "CREATE DATABASE " + database;
            statement.executeUpdate(sql);
            return true;
        } catch (SQLException e) {
            logger.info("创建数据库" + database + "失败");
            return false;
        }
    }

    public boolean CreateTable(String sql){
        try {
            statement = connection.createStatement();
            statement.executeUpdate(sql);
            return true;
        } catch (SQLException e1) {
            e1.printStackTrace();
            return false;
        }
    }

    public void insertNote(){

    }
}
