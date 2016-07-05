package org.digger.spider;

import java.util.ArrayList;
import java.util.List;

import org.digger.spider.download.Downloader;
import org.digger.spider.download.HttpClientDownloader;
import org.digger.spider.entity.Item;
import org.digger.spider.entity.Request;
import org.digger.spider.entity.Response;
import org.digger.spider.parser.Parser;
import org.digger.spider.storage.ConsoleStorage;
import org.digger.spider.storage.Storage;

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

    /**
     * 包含了spider允许爬去的域名的列表
     */
    private List<String> allowedDomains = new ArrayList<String>();

    /**
     * 使用的下载器
     */
    private Downloader downloader;

    private Storage storage;

    private Parser parser;

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

    public Spider setAllowedDomains(String... domains) {
        if (domains != null && domains.length > 0) {
            for (String domain: domains) {
                allowedDomains.add(domain);
            }
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

    private Downloader getDownloader() {
        if (downloader == null) {
            downloader = new HttpClientDownloader(); // 使用默认的下载器进行下载
        }
        return downloader;
    }

    public Response download(Request request) {
        return getDownloader().download(request);
    }

    public void parser(Response response) {
        if (response != null) {
            response.put("title", response.css("title"));
        }
    }

    public Spider setParser(Parser parser) {
        this.parser = parser;
        return this;
    }

    private Parser getParser() {
        if (parser == null) {
            parser = null;
        }
        return parser;
    }

    public void processItem(Item item) {
        getStorage().processItem(item);
    }

    public Spider setStorage(Storage storage) {
        this.storage = storage;
        return this;
    }

    private Storage getStorage() {
        if (storage == null) {
            storage = new ConsoleStorage();
        }
        return storage;
    }

}
