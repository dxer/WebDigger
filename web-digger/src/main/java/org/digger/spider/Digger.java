package org.digger.spider;

import java.util.List;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.digger.spider.conf.Configuration;
import org.digger.spider.conf.DiggerConf;
import org.digger.spider.entity.CrawlerModel;
import org.digger.spider.entity.Request;
import org.digger.spider.entity.Response;
import org.digger.spider.scheduler.QueueScheduler;
import org.digger.spider.scheduler.Scheduler;
import org.digger.spider.tools.FieldResolver;
import org.digger.spider.tools.LinkExtractor;

/**
 * 
 * @class Digger
 * @author linghf
 * @version 1.0
 * @since 2016年4月11日
 */
public class Digger {

    private Scheduler<Request> scheduler = new QueueScheduler();

    private boolean isRunning = false;

    private static ThreadPoolExecutor threadPoolExecutor;

    private static Lock diggerLocker = new ReentrantLock(false);

    private static Condition condition = diggerLocker.newCondition();

    private DiggerConf diggerConf;

    /**
     * 使用静态内部类的方式实现单例模式
     * 
     * @class DiggerBuilder
     * @author linghf
     * @version 1.0
     * @since 2016年7月4日
     */
    private static class DiggerBuilder {
        private static final Digger INSTANCE = new Digger();
    }

    public static final Digger getInstance() {
        return DiggerBuilder.INSTANCE;
    }

    public void setConf(Configuration conf) {
        this.diggerConf = (DiggerConf) conf;
    }

    public Configuration getConf() {
        return this.diggerConf;
    }

    public void addRequests(Spider spider, List<String> urls) {
        try {
            if (urls != null && !urls.isEmpty()) {
                for (String url: urls) {
                    addRequest(spider, url);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置调度器
     * 
     * @param scheduler
     * @return
     */
    public Digger setScheduler(Scheduler<Request> scheduler) {
        this.scheduler = scheduler;
        return this;
    }

    /**
	 * 
	 */
    public Digger register(Spider spider) {
        if (spider != null) {
            List<String> startUrls = spider.getStartUrls();

            addRequests(spider, startUrls);
        }
        return this;
    }

    /**
     * 启动爬虫
     */
    public void start() {
        try {
            if (threadPoolExecutor == null) {
                int threadNum = diggerConf.getInt(DiggerConf.WORK_THREAD_NUM, 3);
                threadPoolExecutor = new ThreadPoolExecutor(threadNum, threadNum, 3, TimeUnit.SECONDS,
                                new LinkedBlockingQueue<Runnable>());
            }

            if (!this.isRunning) {
                this.isRunning = true;

                new Thread(new Worker()).start();
            }
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
                    final Request request = scheduler.get();
                    if (request == null) {
                        diggerLocker.lock();

                        try {
                            condition.await();
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            diggerLocker.unlock();
                        }

                    } else { // 将任务加入到线程池中进行出入
                        threadPoolExecutor.execute(new Runnable(){

                            public void run() {
                                execute(request);
                            }
                        });
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void addRequest(Spider spider, String url) {
        Request request = new Request();
        request.setUrl(url);
        request.setSpider(spider);

        try {
            diggerLocker.lock();
            scheduler.put(request);
            condition.signalAll();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            diggerLocker.unlock();
        }
    }

    /**
     * 爬虫线程sleep
     * 
     * @param time
     */
    private void sleep(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 执行，核心逻辑
     * 
     * @param request
     */
    private void execute(Request request) {
        Spider spider = request.getSpider();

        if (!request.vertify()) {
            return;
        }

        // 请求对应的url，获取网页相关数据Response
        Response response = spider.download(request);
        if (response != null) {

            // digger会对定义的OutputModel进行解析处理
            Class<? extends CrawlerModel> claz = spider.getCrawlClass();
            if (claz != null) {
                FieldResolver.resolve(response, claz);
            }
            // 对用户自定义的设置，进行网页分析
            spider.process(response);

            if (spider.isFollowed() || spider.getFilter().isEmpty()) { // 提取当前页面其他符合规则的url，进行继续爬取
                Set<String> urls = LinkExtractor.extract(response, spider.getFilter());
                if (urls != null && urls.size() > 0) {
                    for (String url: urls) {
                        addRequest(spider, url);
                    }
                }
            }

            // storage对item进行处理
            spider.persist(response.getItem());
        }

        sleep(3 * 1000);

    }

}
