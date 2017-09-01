package com.crawler.core.task;

import com.crawler.core.NoteHttpClient;
import com.crawler.core.modal.Note;
import com.crawler.core.parser.NoteParser;
import com.crawler.core.util.HttpClientUtil;
import com.crawler.core.util.MySQLAccess;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.rmi.runtime.Log;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/8/30.
 */
public class NoteTask implements Runnable {

    private static Logger logger = LoggerFactory.getLogger(NoteTask.class);
    private String source;

    public NoteTask(String source) {
        this.source = source;
    }

    @Override
    public void run() {
        try {
            String content = "";
            if (source != null) {
                content = HttpClientUtil.getWebPage(source);
            }
            if (content != null) {
                Map<String, Object> map = NoteParser.getInstance().parser(content, source);
                Note note = (Note) map.get("note");
//                System.out.println(note.getIcon());
//                NoteHttpClient.getDownloadThreadPool().execute(new DowloadTask(note.getIcon()));
                int id = MySQLAccess.getAccess().insertNote(note);
                Integer count = 1;
                while (NoteHttpClient.chapterUrls.size() > 0){
                    NoteHttpClient.getDetailPageThreadPool().execute(new ChapterTask(NoteHttpClient.chapterUrls.take(), id, count++));
                }
                if (isTask()){
                    logger.info("录入书籍"+ note.getName() +"完成");
                    NoteHttpClient.getListPageThreadPool().execute(new NoteTask(NoteHttpClient.list.take()));
                    NoteHttpClient.taskCount ++;
                }

            }

        } catch (Exception e) {
//            e.printStackTrace();
            NoteHttpClient.getListPageThreadPool().execute(new NoteTask(source));
        }
    }


    public boolean isTask(){
        while (true){
            if (NoteHttpClient.getDetailPageThreadPool().getActiveCount() == 0) {
                return true;
            }
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
