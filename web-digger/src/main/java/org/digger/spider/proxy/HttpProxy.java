package org.digger.spider.proxy;

import org.digger.spider.tools.NetworkUtils;


/**
 * 
 * @class HttpProxy
 * @author linghf
 * @version 1.0
 * @since 2016年7月15日
 */
public class HttpProxy {

    private String host;

    private int port;

    public HttpProxy(){

    }

    public HttpProxy(String host, int port){
        this.host = host;
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public boolean isVaild() {
        return NetworkUtils.isValidIPPort(host, port);
    }
}
