package org.digger.spider.download;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.NoHttpResponseException;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.digger.spider.constant.HttpConstant;
import org.digger.spider.entity.Request;
import org.digger.spider.entity.Response;

import com.google.common.base.Strings;

/**
 * 
 * @class HttpClientDownloader
 * @author linghf
 * @version 1.0
 * @since 2016年4月11日
 */
public class HttpClientDownloader implements Downloader {

    private static CloseableHttpClient httpClient;

    private static void config(HttpRequestBase httpRequestBase, Map<String, String> headers) {
        if (headers != null && !headers.isEmpty()) {
            for (String name: headers.keySet()) {
                String value = headers.get(name);
                if (!Strings.isNullOrEmpty(name)) {
                    httpRequestBase.setHeader(name, value);
                }
            }
        }
        httpRequestBase.setHeader(HttpConstant.USER_AGENT, "Mozilla/5.0");
        httpRequestBase.setHeader(HttpConstant.ACCEPT,
            "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        httpRequestBase.setHeader(HttpConstant.ACCEPT_LANGUAGE, "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");// "en-US,en;q=0.5");
        httpRequestBase.setHeader(HttpConstant.ACCEPT_CHARSET, "ISO-8859-1,utf-8,gbk,gb2312;q=0.7,*;q=0.7");

        // 配置请求的超时设置
        RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(3000).setConnectTimeout(3000)
                        .setSocketTimeout(3000).build();
        httpRequestBase.setConfig(requestConfig);
    }

    private CloseableHttpClient getHttpClient() {
        ConnectionSocketFactory plainsf = PlainConnectionSocketFactory.getSocketFactory();
        LayeredConnectionSocketFactory sslsf = SSLConnectionSocketFactory.getSocketFactory();
        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory> create()
                        .register("http", plainsf).register("https", sslsf).build();

        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(registry);
        // 将最大连接数增加到200
        cm.setMaxTotal(200);
        // 将每个路由基础的连接增加到20
        cm.setDefaultMaxPerRoute(20);

        // 将目标主机的最大连接数增加到50
        // HttpHost localhost = new HttpHost("", 80);
        // cm.setMaxPerRoute(new HttpRoute(localhost), 50);

        // 请求重试处理
        HttpRequestRetryHandler httpRequestRetryHandler = new HttpRequestRetryHandler(){
            public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
                if (executionCount >= 5) {// 如果已经重试了5次，就放弃
                    return false;
                }
                if (exception instanceof NoHttpResponseException) {// 如果服务器丢掉了连接，那么就重试
                    return true;
                }
                if (exception instanceof SSLHandshakeException) {// 不要重试SSL握手异常
                    return false;
                }
                if (exception instanceof InterruptedIOException) {// 超时
                    return false;
                }
                if (exception instanceof UnknownHostException) {// 目标服务器不可达
                    return false;
                }
                if (exception instanceof ConnectTimeoutException) {// 连接被拒绝
                    return false;
                }
                if (exception instanceof SSLException) {// ssl握手异常
                    return false;
                }

                HttpClientContext clientContext = HttpClientContext.adapt(context);
                HttpRequest request = clientContext.getRequest();
                // 如果请求是幂等的，就再次尝试
                if (!(request instanceof HttpEntityEnclosingRequest)) {
                    return true;
                }
                return false;
            }
        };

        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(cm)
                        .setRetryHandler(httpRequestRetryHandler).build();
        return httpClient;
    }

    public Response download(Request request) {
        if (httpClient == null) {
            httpClient = getHttpClient();
        }

        Response response = null;
        if (request == null || Strings.isNullOrEmpty(request.getUrl())) {
            return response;
        }

        String requestUrl = request.getUrl();

        CloseableHttpResponse httpResponse = null;
        try {
            HttpGet httpget = new HttpGet(requestUrl);
            config(httpget, request.getHeaders());

            httpResponse = httpClient.execute(httpget, HttpClientContext.create());
            if (httpResponse == null) {
                return response;
            }

            response = new Response();
            response.setUrl(requestUrl);
            response.setRequest(request);

            HttpEntity entity = httpResponse.getEntity();
            String content = EntityUtils.toString(entity, "utf-8");
            response.setHtml(content);// 返回抓取网页的内容

            Header[] headers = httpResponse.getAllHeaders();
            if (headers != null && headers.length > 0) {
                Map<String, String> headerMap = new HashMap<String, String>();

                for (Header header: headers) {
                    headerMap.put(header.getName(), header.getValue());
                }
                if (headerMap != null && !headerMap.isEmpty()) {
                    response.setHeaders(headerMap);
                }
            }

            response.setStatus(httpResponse.getStatusLine().getStatusCode());

            EntityUtils.consume(entity);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (httpResponse != null)
                    httpResponse.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return response;
    }
}
