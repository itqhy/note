package com.crawler.core.parser;

import com.crawler.core.NoteHttpClient;
import com.crawler.core.modal.Note;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.*;

/**
 * Created by Administrator on 2017/8/30.
 */
public class NoteParser {
    private volatile static NoteParser instance;
    private static int count = 0;

    public static NoteParser getInstance() {
        if (instance == null) {
            synchronized (NoteParser.class) {
                if (instance == null) {
                    instance = new NoteParser();
                }
            }
        }
        return instance;
    }

    private NoteParser() {

    }

    public Map<String, Object> parser(String content, String source) {
        Map<String, Object> map = new HashMap<>();
        Document doc = Jsoup.parse(content);
        List<String> urls = new ArrayList<>();
        String name = doc.select("div#info h1").text();
        String remark = doc.select("div#intro").html();
        String icon = doc.select("div#fmimg img").attr("src");
        String auth = doc.select("div#info p").first().text().substring("作    者：".length());



//        System.out.println(count++);
        Elements elements = doc.select("div#list dd a");
        elements.forEach(e -> {
            try {
                NoteHttpClient.chapterUrls.put(source + e.attr("href"));
                urls.add(source + e.attr("href"));
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
        });
        Note note = new Note(name, auth, new Date(), remark, source, icon, urls.size());
        map.put("urls",urls);
        map.put("note",note);
        return  map;
    }
}
