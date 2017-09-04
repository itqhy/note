package com.crawler.core.modal;

/**
 * Created by Administrator on 2017/9/4.
 */
public class Page {

    private String source;
    private int noteId;
    private int order;

    public Page(String source, int noteId, int order) {
        this.source = source;
        this.noteId = noteId;
        this.order = order;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public int getNoteId() {
        return noteId;
    }

    public void setNoteId(int noteId) {
        this.noteId = noteId;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}
