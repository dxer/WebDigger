package org.digger.spider.download;

import org.digger.spider.entity.Request;
import org.digger.spider.entity.Response;

/**
 * 
 * @class Downloader
 * @author linghf
 * @version 1.0
 * @since 2016年3月22日
 */
public interface Downloader {

	public Response download(Request request);
}
