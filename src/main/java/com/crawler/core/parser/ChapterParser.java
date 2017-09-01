package com.crawler.core.parser;

import com.crawler.core.modal.Chapter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.*;

/**
 * Created by Administrator on 2017/8/30.
 */
public class ChapterParser {

    private volatile static ChapterParser instance;

    public static ChapterParser getInstance() {
        if (instance == null) {
            synchronized (ChapterParser.class) {
                if (instance == null) {
                    instance = new ChapterParser();
                }
            }
        }
        return instance;
    }

    private ChapterParser() {

    }

    public Chapter parser(String content, String source, int noteId,int order) {
        Document doc = Jsoup.parse(content);
        List<String> urls = new ArrayList<>();
        String title = doc.select("div.bookname h1").text();
        String chContent = doc.select("div#content").html();
        Chapter chapter=  new Chapter(noteId,title,chContent,source, order);
        return chapter;
    }
}
