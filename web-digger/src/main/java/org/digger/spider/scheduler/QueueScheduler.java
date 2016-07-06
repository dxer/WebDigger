package org.digger.spider.scheduler;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;

import org.digger.spider.entity.Request;

/**
 * 
 * 
 * @class QueueScheduler
 * @author linghf
 * @version 1.0
 * @since 2016年4月11日
 */
public class QueueScheduler implements Scheduler<Request> {

    /**
     * 爬虫队列
     */
    private final LinkedBlockingQueue<Request> queue = new LinkedBlockingQueue<Request>();

    /**
     * 记录已经访问过的url链接
     */
    private Set<String> visited = new HashSet<String>();

    public Request get() {
        try {
            return queue.poll();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void put(Request request) {
        try {
            String url = request.getUrl();
            if (visited != null && !visited.contains(url) && queue != null && !queue.contains(url)) {
                queue.add(request);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void finished(Request request) {
        if (request != null) {
            visited.add(request.getUrl());
        }
    }

    public int size() {
        return queue.size();
    }

}
