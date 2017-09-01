package com.crawler.core.task;

import com.crawler.core.NoteHttpClient;
import com.crawler.core.util.HttpClientUtil;

/**
 * Created by Administrator on 2017/8/30.
 */
public class DowloadTask implements Runnable {

    private static String fileUrl = "http://www.37zw.net";
    private static Boolean isReplaceFile = false;
    private static String path = "E:\\notebook\\";
    private String source;

    public DowloadTask(String source) {
        this.source = source;
    }

    @Override
    public void run() {

            try {
                    String url = fileUrl +  source;
                    String pathUrl =path + source.substring(0, source.lastIndexOf("/")) + "/";

                    String saveFileName = source.substring(source.lastIndexOf("/") + 1);

                    HttpClientUtil.downloadFile(url, pathUrl, saveFileName, isReplaceFile);
            } catch (Exception e) {
                e.printStackTrace();
            }

    }
}
