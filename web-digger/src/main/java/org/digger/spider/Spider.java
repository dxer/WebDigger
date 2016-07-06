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
import org.digger.spider.tools.LinkFilter;

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
	protected String name;

	protected boolean followed = false;

	/**
	 * spider启动的时候需要爬取的url列表
	 */
	protected List<String> startUrls = new ArrayList<String>();

	private LinkFilter filter = new LinkFilter();

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

	public boolean isFollowed() {
		return followed;
	}

	public void setFollowed(boolean followed) {
		this.followed = followed;
	}

	public Spider addAllows(String... allows) {
		if (allows != null && allows.length > 0) {
			for (String allow : allows) {
				filter.getAllows().add(allow);
			}
		}
		return this;
	}

	public Spider addAllowDomains(String... allowDomains) {
		if (allowDomains != null && allowDomains.length > 0) {
			for (String allowDomain : allowDomains) {
				filter.getAllowDomains().add(allowDomain);
			}
		}
		return this;
	}

	public LinkFilter getFilter() {
		return filter;
	}

	public void setFilter(LinkFilter filter) {
		this.filter = filter;
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

			for (String s : urls) {
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

	/**
	 * 开始运行该爬虫
	 */
	public void start() {
		Digger.getInstance().register(this).start();
	}

}
