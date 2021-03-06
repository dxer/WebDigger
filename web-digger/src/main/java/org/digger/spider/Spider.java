package org.digger.spider;

import java.util.ArrayList;
import java.util.List;

import org.digger.spider.download.Downloader;
import org.digger.spider.download.HttpClientDownloader;
import org.digger.spider.entity.CrawlerModel;
import org.digger.spider.entity.Item;
import org.digger.spider.entity.Request;
import org.digger.spider.entity.Response;
import org.digger.spider.processor.Processor;
import org.digger.spider.storage.ConsoleStorage;
import org.digger.spider.storage.Storage;
import org.digger.spider.tools.LinkFilter;

import com.google.common.base.Strings;

/**
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
    protected String name;

    /**
     * 是否爬取更多的url
     */
    protected boolean followed = false;

    /**
     * spider启动的时候需要爬取的url列表
     */
    protected List<String> startUrls = new ArrayList<String>();

    /**
     * url过滤器
     */
    protected LinkFilter filter = new LinkFilter();

    /**
     * 输出model
     */
    protected Class<? extends CrawlerModel> crawlClass;

    /**
     * 使用的下载器
     */
    private Downloader downloader;

    private Storage storage;

    private Processor processor;

    public String getName() {
        if (Strings.isNullOrEmpty(name)) {
            name = getClass().getCanonicalName();
        }
        return name;
    }

    public Spider setName(String name) {
        this.name = name;
        return this;
    }

    public boolean isFollowed() {
        return followed;
    }

    public Spider setFollowed(boolean followed) {
        this.followed = followed;
        return this;
    }

    public Spider addAllows(String... allows) {
        filter.addAllows(allows);
        return this;
    }

    public Spider addAllowDomains(String... allowDomains) {
        filter.addAllowDomains(allowDomains);
        return this;
    }

    public LinkFilter getFilter() {
        return filter;
    }

    public Spider setFilter(LinkFilter filter) {
        this.filter = filter;
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

    public Spider addStartUrls(String... urls) {
        if (urls != null && urls.length > 0) {
            if (startUrls == null) {
                startUrls = new ArrayList<String>();
            }

            for (String s: urls) {
                startUrls.add(s);
            }
        }
        return this;
    }

    public Spider setCrawlModel(Class<? extends CrawlerModel> claz) {
        this.crawlClass = claz;
        return this;
    }

    public Class<? extends CrawlerModel> getCrawlClass() {
        return this.crawlClass;
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

    public void process(Response response) {
        if (response != null) {
            response.put("url", response.getRequest().getUrl());
            response.put("title", response.getJXDoc().getSelector().getTitle());
        }
    }

    public Spider setProcessor(Processor processor) {
        this.processor = processor;
        return this;
    }

    public Processor getProcessor() {
        if (processor == null) {
            processor = null;
        }
        return processor;
    }

    public void persist(Item item) {
        getStorage().persist(item);
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

    /**
     * 开始运行该爬虫
     */
    public void start() {
        Digger.getInstance().register(this).start();
    }

}
