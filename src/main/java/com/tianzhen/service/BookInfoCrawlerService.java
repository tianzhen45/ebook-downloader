package com.tianzhen.service;

import com.tianzhen.common.EBookConstant;
import com.tianzhen.entity.SearchResponseDO;
import com.tianzhen.httpclient.Requestor;
import com.tianzhen.runnable.SearchBookAndSaveTask;
import com.tianzhen.runnable.SimpleThreadPool;
import javafx.util.Pair;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HeaderIterator;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class BookInfoCrawlerService {

    @Autowired
    private Requestor requestor;


    @Autowired
    private EBookConstant eBookConstant;

    @Autowired
    private BookInfoDataService dataService;

    @Autowired
    private SimpleThreadPool threadPool;

    //最大线程数
    private final static int SEARCH_MAX_TASK_NUM = 5;

    //每个线程最少处理页数
    private final static int MIN_PROCESS_PAGES = 5;


    @PostConstruct
    public void postConstruct() {
        log.info("初始化抓取服务: 开始登录");
        this.doLogin();
    }

    public void searchBookInfoAndSave(String keyWord) {

        Map<String, String> params = new HashMap<String, String>();
        int currentPage = 1;
        String fileType = "";
        params.put("c", keyWord);
        params.put("orderBy", "true");
        params.put("orderColumn", "file_name");
        params.put("owner", "true");
        params.put("fileType", fileType);
        params.put("current", String.valueOf(currentPage));
        HttpResponse httpResponse = requestor.doGet(eBookConstant.getSearchUrl(), params);

        // 获取响应消息实体
        HttpEntity entity = httpResponse.getEntity();
        // 响应状态
        log.info("execute search response status:" + httpResponse.getStatusLine());
        log.info("execute search response headers:");
        HeaderIterator iterator = httpResponse.headerIterator();
        while (iterator.hasNext()) {
            log.info("\t" + iterator.next());
        }

        if (entity != null) {
            SearchResponseDO responseDO = dataService.constructSearchResponseDO(entity);
            log.info("通过关键词 ["+keyWord+"] 共查询到"+responseDO.getTotal()+"条记录");

            int pages = responseDO.getPages();
            if(pages > 0){
                List<Pair<Integer, Integer>> splitPageList = splitPageList(pages, currentPage);
                for (Pair<Integer, Integer> pagePair : splitPageList) {
                    threadPool.execute(new SearchBookAndSaveTask(pagePair.getKey(),pagePair.getValue(),keyWord,requestor,dataService,eBookConstant));
                }
            }


        } else {
            log.error("execute search failed. response is null");
        }

    }


    public List<Pair<Integer,Integer>> splitPageList(int pages,int currentPage){
        List<Pair<Integer,Integer>> pagePairList = new ArrayList<>();
        int threadNum = SEARCH_MAX_TASK_NUM;
        int pageLength = (pages / threadNum + (pages % threadNum == 0 ? 0 : 1)) ;

        if(pages <= SEARCH_MAX_TASK_NUM * (MIN_PROCESS_PAGES-1)){
            threadNum = pages / MIN_PROCESS_PAGES + (pages % MIN_PROCESS_PAGES == 0 ? 0 : 1);
            pageLength = MIN_PROCESS_PAGES;
        }

        while(currentPage <= pages){
            int endPage = currentPage + pageLength -1;
            if(endPage > pages){
                endPage = pages;
            }
            Pair<Integer, Integer> pagePair = new Pair<>(currentPage,endPage);
            pagePairList.add(pagePair);
            currentPage = endPage+1;
        }
        return pagePairList;
    }


    public void doLogin() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("username", eBookConstant.getUsername());
        params.put("password", eBookConstant.getPassword());
        params.put("grant_type", eBookConstant.getGrantType());
        requestor.doLogin(eBookConstant.getLoginUrl(), params);
    }


}
