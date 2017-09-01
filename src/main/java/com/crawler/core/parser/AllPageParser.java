package com.crawler.core.parser;

import com.crawler.core.NoteHttpClient;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/8/30.
 */
public class AllPageParser {
    private volatile static AllPageParser instance;

    public static AllPageParser getInstance() {
        if (instance == null) {
            synchronized (AllPageParser.class) {
                if (instance == null) {
                    instance = new AllPageParser();
                }
            }
        }
        return instance;
    }

    private AllPageParser() {

    }

    public List<String> parser(String content){
        Document doc = Jsoup.parse(content);
        Elements elements = doc.select("div#main ul li");
        List<String> urls = new ArrayList<>();
        for (Element e:elements) {
            String url = e.select("a").attr("href").toString();
            String name = e.select("a").text();
            System.out.println(name);
            if(StringUtils.isNotBlank(url)){
                urls.add(url);
                NoteHttpClient.list.add(url);
            }
        }
        return urls;
    }
}
