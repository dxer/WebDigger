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

import org.digger.spider.entity.Request;
import org.digger.spider.scheduler.QueueScheduler;
import org.digger.spider.scheduler.Scheduler;

import com.google.common.base.Strings;

/**
 * 
 * @class Digger
 * @author linghf
 * @version 1.0
 * @since 2016年4月11日
 */
public class Digger {

    private List<String> urls = new ArrayList<String>();

    private Scheduler<Request> scheduler = new QueueScheduler();

    private int threadNum = 4;

    private boolean isRunning = true;

    /**
     * 
     */
    public Digger register(Spider spider) {
        if (spider != null) {
            List<String> startUrls = spider.getStartUrls();

            if (startUrls != null && !startUrls.isEmpty()) {
                for (String url: startUrls) {
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
                    Request request = scheduler.take();
                    String requestURL = request.getUrl();
                    System.out.println(requestURL);

                    Spider spider = request.getSpider();

                    if (!Strings.isNullOrEmpty(requestURL) && spider != null) {
                        spider.download(requestURL);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
