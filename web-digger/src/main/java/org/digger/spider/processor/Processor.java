package org.digger.spider.processor;

import org.digger.spider.entity.Response;

/**
 * 
 * 
 * @class Parser
 * @author linghf
 * @version 1.0
 * @since 2016年4月11日
 */
public interface Processor {

    public void parser(Response response);
}
