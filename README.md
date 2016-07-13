# WebDigger

WebDigger是一个采用Java开发的爬虫框架。WebDigger的目标是简化爬虫的开发流程，让开发者只需要进行简单的代码开发，便能完成爬虫的开发。


### 组件

- Digger
负责控制数据流在系统中所有组件中流动，并在相应动作发生时触发事件。

- Scheduler
调度器从引擎接受request并将他们入队，以便之后引擎请求他们时提供给引擎。

- Downloader
下载器负责获取页面数据Response，供Processor模块进行数据提取

- Processor
Processor是用于分析response并提取item(需要的抓取的具体的数据)。每个spider的Processor负责处理一个特定(或一些)网站。

- Storage
Storage负责处理被Processor提取出来的item。主要是用来将item进行持久化操作（保存到文件，打印输出等）。

### 使用方法

```java
public class DBBookSpider extends Spider {

    /**
     * 自定义的processor，负责解析页面书籍的相关信息
     */
    @Override
    public void process(Response response) {
        Selector selector = response.getJXDoc().getSelector();

        Elements els = selector.selElements("#subject_list > ul > li");

        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        for (Element e: els) {
            Map<String, String> map = new HashMap<String, String>();
            String name = e.select("div.info > h2 > a").text();
            String pub = e.select("div.info > div.pub").text();
            String score = e.select("div.info > div.star.clearfix > span.rating_nums").text();
            String comments = e.select("div.info > div.star.clearfix > span.pl").text();
            String intro = e.select("div.info > p").text();

            map.put("name", name);
            map.put("score", score);
            map.put("pub", pub);
            map.put("comments", comments);
            map.put("intro", intro);

            list.add(map);
        }
        response.put("list", list);
    }

    /**
     * 自定义的Storage，这里仅仅是将文件打印输出
     */
    @Override
    public void persist(Item item) {
        List<Map<String, String>> list = (List<Map<String, String>>) item.get("list");
        if (list != null && !list.isEmpty()) {
            for (Map<String, String> map: list) {
                System.out.println(JSON.toJSONString(map));
            }
        }

    }

    public static void main(String[] args) {
        new DBBookSpider()
                        .addStartUrls("https://book.douban.com/tag/中国文学",
                            "https://book.douban.com/tag/中国文学?start=20&type=T",
                            "https://book.douban.com/tag/中国文学?start=40&type=T").setFollowed(false).start(); // 启动爬虫
    }
}

```