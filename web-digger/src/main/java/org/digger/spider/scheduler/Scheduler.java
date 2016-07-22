package org.digger.spider.scheduler;


/**
 * 
 * @class Scheduler
 * @author linghf
 * @version 1.0
 * @since 2016年3月22日
 */
public interface Scheduler<T> {

    public T get();

    public void put(T request);

    public void finished(T request);

    public int size();
}
