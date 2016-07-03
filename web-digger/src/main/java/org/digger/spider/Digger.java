/**
 * Copyright (c) 2016 21CN.COM . All rights reserved.
 * 
 * Description: web-digger
 * 
 * <pre>
 * Modified log:
 * ------------------------------------------------------
 * Ver.		Date		Author			Description
 * ------------------------------------------------------
 * 1.0		2016年4月11日	linghf		created.
 * </pre>
 */
package org.digger.spider;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.digger.spider.entity.Request;
import org.digger.spider.entity.Response;
import org.digger.spider.scheduler.QueueScheduler;
import org.digger.spider.scheduler.Scheduler;

/**
 * 
 * @class Digger
 * @author linghf
 * @version 1.0
 * @since 2016年4月11日
 */
public class Digger implements Runnable {

	private List<String> urls = new ArrayList<String>();

	private Scheduler<Request> scheduler = new QueueScheduler();

	private int threadNum = 4;

	private boolean isRunning = true;

	private static ThreadPoolExecutor threadPoolExecutor;

	public Digger() {
		init();
	}

	private void init() {
		threadPoolExecutor = new ThreadPoolExecutor(threadNum, threadNum, 3, TimeUnit.SECONDS,
				new LinkedBlockingQueue<Runnable>(3));
	}

	/**
	 * 
	 */
	public Digger register(Spider spider) {
		if (spider != null) {
			List<String> startUrls = spider.getStartUrls();

			if (startUrls != null && !startUrls.isEmpty()) {
				for (String url : startUrls) {
					Request request = new Request();
					request.setUrl(url);
					request.setSpider(spider);

					scheduler.put(request);
				}
			}
		}
		return this;
	}

	/**
	 * 启动线程
	 */
	public void start() {
		try {
			this.isRunning = true;

			ExecutorService executors = Executors.newFixedThreadPool(threadNum);

			for (int i = 0; i < threadNum; i++) {
				executors.execute(new Worker());
			}
			executors.shutdown();
			System.out.println("--------------------------");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void stop() {
		this.isRunning = false;
	}

	/**
	 * 爬虫线程
	 * 
	 * @class Worker
	 * @author linghf
	 * @version 1.0
	 * @since 2016年4月11日
	 */
	class Worker implements Runnable {
		public void run() {
			while (isRunning) {
				try {
					final Request request = scheduler.take();

					threadPoolExecutor.execute(new Runnable() {

						public void run() {
							process(request);
						}
					});

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void run() {

	}

	public void process(Request request) {
		Spider spider = request.getSpider();

		if (!request.vertify()) {

		}

		Response response = spider.download(request);
		spider.parser(response);

	}

}
