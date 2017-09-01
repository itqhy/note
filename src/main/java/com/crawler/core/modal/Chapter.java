package com.crawler.core.modal;

/**
 * Created by Administrator on 2017/8/30.
 */
public class Chapter {
    private int id;
    private int noteId;
    private String title;
    private String content;
    private String source;
    private int order;

    public Chapter() {
    }

    public Chapter(int noteId, String title, String content, String source, int order) {
        this.noteId = noteId;
        this.title = title;
        this.content = content;
        this.source = source;
        this.order = order;
    }

    @Override
    public String toString() {
        return "Chapter{" +
                "noteId=" + noteId +
                ", title='" + title + '\'' +
                ", source='" + source + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNoteId() {
        return noteId;
    }

    public void setNoteId(int noteId) {
        this.noteId = noteId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}
