package com.crawler.core.modal;

import java.util.Date;

/**
 * Created by Administrator on 2017/8/29.
 */
public class Note {

    private int id;
    private String name;
    private String auth;
    private Date time;
    private String remark;
    private String source;
    private String icon;
    private int size;

    public Note(String name, String auth, Date time, String remark, String source, String icon, int size) {
        this.name = name;
        this.auth = auth;
        this.time = time;
        this.remark = remark;
        this.source = source;
        this.icon = icon;
        this.size = size;
    }

    @Override
    public String toString() {
        return "Note{" +
                "name='" + name + '\'' +
                ", auth='" + auth + '\'' +
                ", icon='" + icon + '\'' +
                ", size=" + size +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
