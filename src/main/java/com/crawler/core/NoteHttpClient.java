package com.crawler.core;

import com.crawler.core.parser.AllPageParser;
import com.crawler.core.task.NoteTask;
import com.crawler.core.util.HttpClientUtil;
import com.crawler.core.util.MySQLAccess;
import com.crawler.core.util.SimpleThreadPoolExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2017/8/24.
 */
public class NoteHttpClient {

    private static Logger logger = LoggerFactory.getLogger(NoteHttpClient.class);

    private volatile static NoteHttpClient instance;
    private static ThreadPoolExecutor listPageThreadPool;
    private static  ThreadPoolExecutor detailPageThreadPool;
    private static  ThreadPoolExecutor downloadThreadPool;
    public final static  ArrayBlockingQueue<String> URLS = new ArrayBlockingQueue<String>(9999);
    public final static   ArrayBlockingQueue<String> list =new ArrayBlockingQueue<String>(10000);
    public final static   ArrayBlockingQueue<String> chapterUrls =new ArrayBlockingQueue<String>(10000);
    private static long startTime = System.currentTimeMillis();
    public static int taskCount = 0;
    public static NoteHttpClient getInstance(){
        if(instance == null){
            synchronized (NoteHttpClient.class){
                if(instance == null){
                    instance = new NoteHttpClient();
                }
            }
        }
        return instance;
    }

    private NoteHttpClient(){
        //初始化线程池
        initThreadPool();
    }

    /**
     * 初始化线程池
     */
    private void initThreadPool(){
        listPageThreadPool = new SimpleThreadPoolExecutor(1000, 1000,
                20L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(5000),
                new ThreadPoolExecutor.DiscardPolicy(), "listPageThreadPool");
        detailPageThreadPool= new SimpleThreadPoolExecutor(1000, 1000,
                20L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(5000),
                new ThreadPoolExecutor.DiscardPolicy(), "detailPageThreadPool");
        downloadThreadPool= new SimpleThreadPoolExecutor(1000, 1000,
                20L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(5000),
                new ThreadPoolExecutor.DiscardPolicy(), "downloadThreadPool");
    }

    public  void startCrawler()throws Exception{
        String url = "http://www.37zw.net/xiaoshuodaquan/";
        try {

            String content = HttpClientUtil.getWebPage(url);
            AllPageParser.getInstance().parser(content);
            if(list != null && list.size() > 0){
                listPageThreadPool.execute(new NoteTask(list.take()));
            }
            while (true){
                if(taskCount == list.size() && !listPageThreadPool.isTerminated()){
                    long time = (System.currentTimeMillis() - startTime ) / 1000;
                    logger.info("耗时：" + time );
                    logger.info("抓取完成");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ThreadPoolExecutor getListPageThreadPool() {
        return listPageThreadPool;
    }

    public static ThreadPoolExecutor getDetailPageThreadPool() {
        return detailPageThreadPool;
    }

    public static ThreadPoolExecutor getDownloadThreadPool() {
        return downloadThreadPool;
    }
}
