package com.crawler.core.util;

import com.crawler.core.modal.Chapter;
import com.crawler.core.modal.Note;
import com.mysql.jdbc.exceptions.MySQLSyntaxErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

/**
 * Created by Administrator on 2017/8/28.
 */
public class MySQLAccess {
    private static Logger logger = LoggerFactory.getLogger(MySQLAccess.class);
    private static final MySQLAccess access = new MySQLAccess();

    public static MySQLAccess getAccess() {
        return access;
    }

    private static Statement statement = null;
    private static Connection connection = getConnection();

    private MySQLAccess() {
        ConnectToDatabase(MySqlValues.DATABASE);
//        //在构造时连接数据库，
//        ConnectToDatabase(MySqlValues.getDATABASE());
//        //构造时初始化表
//        init();
    }


    private static Connection conn;

    public static Connection getConnection(){
        //获取数据库连接
        try {
            if(conn == null || conn.isClosed()){
                conn =createConnection();
            }else{
                return  conn;
            }
        }catch (SQLException e){
            logger.error("SQL Exception",e);
        }
        return conn;
    }


    private static Connection createConnection(){
        String url="jdbc:mysql://localhost:3306/" + MySqlValues.DATABASE + "?characterEncoding=utf8";

        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url,MySqlValues.USER,MySqlValues.PASSWORD);
        }catch (MySQLSyntaxErrorException e){
            logger.error("数据库不存在..请先手动创建创建数据库:" + MySqlValues.DATABASE);
            e.printStackTrace();
        }
        catch (SQLException e) {
            logger.error("SQL Exception",e);
        }

        return conn;
    }


    public static void close(){
        if(conn != null){
            try {
                conn.close();
            } catch (SQLException e) {
                logger.error("SQL CLOSE Exception",e);
            }
        }
    }


    /**
     * 连接数据库
     *
     * @param database 数据库名
     * @return
     */
    private void ConnectToDatabase(String database) {
        try {
            Class.forName(MySqlValues.DRIVER_MYSQL);
            connection = DriverManager.getConnection(MySqlValues.URL + database + MySqlValues.SETTINGS,
                    MySqlValues.USER, MySqlValues.PASSWORD);
        } catch (ClassNotFoundException e) {
        } catch (SQLException e) {
                logger.info("获取数据库连接失败");

        }
    }

    /**
     * 创建数据库
     *
     * @param database 数据库名
     * @return
     */
    public void CrestDatabase(String database) {
        try {
            connection = DriverManager.getConnection(MySqlValues.URL + MySqlValues.SETTINGS,
                    MySqlValues.USER, MySqlValues.PASSWORD);
        } catch (SQLException e) {
            logger.info("创建数据库" + database + "失败");
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
//    public boolean init(){
//        CreateTable(MySqlValues.g());
//        return true;
//    }

    public  int insertNote(Note note){
        int id = 0;
        try {
            String sql = "INSERT INTO note (name, auth, time, remark, source,icon,size)" +
                    " VALUES (?, ?, ?, ?,?,?,?);";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1,note.getName());
            stmt.setString(2,note.getAuth());
            stmt.setTimestamp(3,new Timestamp(note.getTime().getTime()));
            stmt.setString(4,note.getRemark());
            stmt.setString(5,note.getSource());
            stmt.setString(6,note.getIcon());
            stmt.setInt(7,note.getSize());
            stmt.executeUpdate();
            // 检索由于执行此 Statement 对象而创建的所有自动生成的键
            ResultSet rs = stmt.getGeneratedKeys();
            if(rs.next()){
                id = rs.getInt(1);
            }
            stmt.close();
            logger.info("插入：" + note.getName() + "完成");
        }catch (SQLException e){
            e.printStackTrace();
        }
        return id;
    }

    public  void insertChapter(Chapter chapter){
        try {
            String sql = "INSERT INTO chapter (noteId, title, content, source,chapter.order) " +
                    "VALUES (?,?, ?, ?,?); ";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1,chapter.getNoteId());
            stmt.setString(2,chapter.getTitle());
            stmt.setString(3,chapter.getContent());
            stmt.setString(4,chapter.getSource());
            stmt.setInt(5,chapter.getOrder());
            stmt.executeUpdate();
            stmt.close();
            logger.info("插入：" + chapter.getNoteId() + "-" + chapter.getTitle() + "完成");
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}
