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

    private final LinkedBlockingQueue<Request> queue = new LinkedBlockingQueue<Request>();

    private Set<String> visited = new HashSet<String>();

    private Set<String> unVisited = new HashSet<String>();

    public Request take() {
        try {
            return queue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void put(Request request) {
        try {
            String url = request.getUrl();
            if (visited != null && !visited.contains(url) && unVisited != null && !unVisited.contains(url)) {
                queue.put(request);
            }
        } catch (InterruptedException e) {
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
