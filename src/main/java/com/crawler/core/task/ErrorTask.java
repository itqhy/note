package com.crawler.core.task;

import com.crawler.core.NoteHttpClient;
import com.crawler.core.modal.Chapter;
import com.crawler.core.modal.Page;
import com.crawler.core.parser.ChapterParser;
import com.crawler.core.util.HttpClientUtil;
import com.crawler.core.util.MySQLAccess;

/**
 * Created by Administrator on 2017/9/4.
 */
public class ErrorTask  implements Runnable{

    private String source;
    private int noteId;
    private int order;

    public ErrorTask (String source,int noteId,int order){
        this.source = source;
        this.noteId = noteId;
        this.order = order;
    }

    @Override
    public void run() {
        try {
            String content ="";
            if(source != null){
                content = HttpClientUtil.getWebPage(source);
            }
            if(content != null){
                Chapter chapter = ChapterParser.getInstance().parser(content, source, noteId,order++);
                MySQLAccess.getAccess().insertChapter(chapter);
            }
        }catch (Exception e) {
            try {
                NoteHttpClient.errorList.put(new Page(source,noteId,order));
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
        }
    }
}
