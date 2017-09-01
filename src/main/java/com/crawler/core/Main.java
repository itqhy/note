package com.crawler.core;

import com.crawler.core.task.DowloadTask;

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


    }
}
