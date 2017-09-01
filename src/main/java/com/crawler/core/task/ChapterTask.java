package com.crawler.core.task;

import com.crawler.core.NoteHttpClient;
import com.crawler.core.modal.Chapter;
import com.crawler.core.modal.Note;
import com.crawler.core.parser.ChapterParser;
import com.crawler.core.parser.NoteParser;
import com.crawler.core.util.HttpClientUtil;
import com.crawler.core.util.MySQLAccess;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/8/30.
 */
public class ChapterTask implements Runnable {

    private String source;
    private int noteId;
    private int order;

    public ChapterTask(String source, int noteId,int order) {
        this.source = source;
        this.noteId = noteId;
        this.order = order;
    }

    @Override
    public void run() {
        try {
            String content ="";
            if(source != null){
//                System.out.println(source);
                content = HttpClientUtil.getWebPage(source);
            }
            if(content != null){
                Chapter chapter = ChapterParser.getInstance().parser(content, source, noteId,order++);
                MySQLAccess.getAccess().insertChapter(chapter);

            }

        }catch (Exception e) {
//            e.printStackTrace();
            NoteHttpClient.getDetailPageThreadPool().execute(new ChapterTask(source,noteId,order));
        }
    }
}
