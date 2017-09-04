package com.crawler.core;

import com.crawler.core.modal.Page;
import com.crawler.core.task.DowloadTask;
import com.crawler.core.task.ErrorTask;

/**
 * Created by Administrator on 2017/8/30.
 */
public class Main {

    public static void main(String[] args)throws Exception {

//        Thread thread = new Thread(new DowloadTask());
//        thread.start();

        NoteHttpClient.getInstance().startCrawler();
//        try {
//            NoteHttpClient.URLS.put("/d/image/1/1243/1243s.jpg");
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        while (true){
            while (NoteHttpClient.errorList.size() > 0) {
                Page pa = NoteHttpClient.errorList.take();
                NoteHttpClient.getErrorThreadPool().execute(new ErrorTask(pa.getSource(),pa.getNoteId(),pa.getOrder()));
            }
            Thread.sleep(30000);
        }


    }
}
