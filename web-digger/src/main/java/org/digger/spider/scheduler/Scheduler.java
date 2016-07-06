/**
 * Copyright (c) 2016 21CN.COM . All rights reserved.
 * 
 * Description: digger
 * 
 * <pre>
 * Modified log:
 * ------------------------------------------------------
 * Ver.		Date		Author			Description
 * ------------------------------------------------------
 * 1.0		2016年3月22日	linghf		created.
 * </pre>
 */
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
