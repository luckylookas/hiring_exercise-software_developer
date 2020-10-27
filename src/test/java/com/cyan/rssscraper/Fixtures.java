package com.cyan.rssscraper;

import com.cyan.rssscraper.client.model.Channel;
import com.cyan.rssscraper.client.model.Item;
import com.cyan.rssscraper.client.model.Rss;
import com.cyan.rssscraper.persistence.model.Topic;
import com.cyan.rssscraper.persistence.model.TopicItem;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class Fixtures {

    public static final Topic WORKER_TOPIC = Topic.builder().tag("worker").item(TopicItem.builder().build()).build();
    public static final Topic JAKARTA_TOPIC = Topic.builder().tag("jakarta").item(TopicItem.builder().build()).build();
    public static URL FEED_URL_1;
    public static URL FEED_URL_2;
    public static URL FEED_URL_100;
    public static URL FEED_URL_500;
    public static Rss MOCKRSS1 = new Rss(new Channel(
            List.of(
                    Item.builder().title("Workers of Vienna unionize in a new way").link("http://news.at/news1").build(),
                    Item.builder().title("Jakarta again fastest growing city").link("http://news.at/news2").build()),
            "feed 1", 1, "http://feed1.com")
    );
    public static Rss MOCKRSS2 = new Rss(new Channel(
            List.of(Item.builder().title("Workers of Jakarta on strike").link("http://news.com/news1").build()),
            "feed 2", 1, "http://feed2.com")
    );

    static {
        try {
            FEED_URL_1 = new URL("http://uri1.com");
            FEED_URL_2 = new URL("http://uri2.com");
            FEED_URL_100 = new URL("http://uri100.com");
            FEED_URL_500 = new URL("http://uri500.com");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
}
