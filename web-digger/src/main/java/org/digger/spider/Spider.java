package org.digger.spider;

import java.util.ArrayList;
import java.util.List;

import org.digger.spider.download.Downloader;
import org.digger.spider.download.HttpClientDownloader;
import org.digger.spider.entity.Response;

/**
 * 
 * 
 * @class Spider
 * @author linghf
 * @version 1.0
 * @since 2016年4月11日
 */
public abstract class Spider extends BaseSpider {

    /**
     * 爬虫的名字，用于区别Spider
     */
    private String name;

    /**
     * spider启动的时候需要爬取的url列表
     */
    private List<String> startUrls = new ArrayList<String>();

    private Downloader downloader;

    public String getName() {
        return name;
    }

    public Spider setName(String name) {
        this.name = name;
        return this;
    }

    public List<String> getStartUrls() {
        return startUrls;
    }

    public Spider setStartUrls(List<String> startUrls) {
        if (startUrls != null && startUrls.size() > 0) {
            this.startUrls.addAll(startUrls);
        }

        return this;
    }

    public void addStartUrls(String... urls) {
        if (urls != null && urls.length > 0) {
            if (startUrls == null) {
                startUrls = new ArrayList<String>();
            }

            for (String s: urls) {
                startUrls.add(s);
            }
        }
    }

    public Spider setDownloader(Downloader downloader) {
        this.downloader = downloader;
        return this;
    }

    public Downloader getDownloader() {
        if (downloader == null) {
            downloader = new HttpClientDownloader();
        }

        return downloader;
    }

    public void parser(Response response) {

    }

    public Response download(String requestUrl) {
        return getDownloader().download(requestUrl);
    }
}
