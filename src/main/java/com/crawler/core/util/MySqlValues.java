package com.crawler.core.util;

/**
 * Created by Administrator on 2017/8/28.
 */
public class MySqlValues {

    public final static   String DRIVER_MYSQL = "com.mysql.jdbc.Driver";
    public final static String URL = "jdbc:mysql://localhost:3306/";
    public final static String SETTINGS = "?useUnicode=true&characterEncoding=utf-8&useSSL=false";
    public final static String DATABASE="sniffer02";
    public final static  String USER = "root";
    public final static String PASSWORD="123456";
    public final static String TABLENAME="note";

    public final  static  String NOTE_SQL=  "CREATE TABLE IF NOT EXISTS note" +
                                            "( 'id' INT NOT NULL AUTO_INCREMENT," +
                                            " 'name' VARCHAR(100) NOT NULL, " +
                                            "'auth' VARCHAR(200) NOT NULL," +
                                            " 'time' TIMESTAMP, " +
                                            "'remark' VARCHAR(2000)," +
                                            " 'source' VARCHAR(500)," +
                                            " PRIMARY KEY ('id') ); ";

    public final static String chapterSQL= " CREATE TABLE  IF NOT EXISTS chapter"  +
                                "( 'id' INT NOT NULL AUTO_INCREMENT," +
                                " 'noteId' INT NOT NULL, " +
                                "'title' VARCHAR(250) NOT NULL," +
                                " 'content' TEXT NOT NULL," +
                                " 'source' VARCHAR(200) NOT NULL," +
                                " PRIMARY KEY ('id') );";

    public final static String insertChapterSQL ="INSERT INTO chapter ( 'title', 'content', 'source') " +
                                                    "VALUES (?, ?, ?); ";

    public final static String insertNoteSQL ="INSERT INTO note ('name', 'auth', 'time', 'remark', 'source')" +
                                                    " VALUES (?, ?, ?, ?,?);";
}
