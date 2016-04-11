package org.digger.spider.scheduler;

import java.util.concurrent.LinkedBlockingQueue;

import org.digger.spider.entity.Request;

/**
 * 
 * @class QueueWithBFScheduler
 * @author linghf
 * @version 1.0
 * @since 2016年4月11日
 */
public class QueueWithBFScheduler implements Scheduler<Request> {
    private final LinkedBlockingQueue<Request> queue = new LinkedBlockingQueue<Request>();

    private static DBloomFilter bloomFilter = null;

    private static DBloomFilter finishedBloomFilter = null;

    private static int BF_SIZE = 2 << 20;

    public QueueWithBFScheduler(){
        bloomFilter = finishedBloomFilter = new DBloomFilter(BF_SIZE);
    }

    public QueueWithBFScheduler(int size){
        bloomFilter = new DBloomFilter(size);
        finishedBloomFilter = new DBloomFilter(size);
    }

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
            if (request != null) {
                String requestURL = request.getUrl();
                if (!bloomFilter.contains(requestURL) && !finishedBloomFilter.contains(requestURL)) {
                    queue.put(request);
                    bloomFilter.add(requestURL);
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void finished(Request request) {
        if (request != null) {
            String requestURL = request.getUrl();
            finishedBloomFilter.add(requestURL);
        }
    }

    public int size() {
        return queue.size();
    }
}
