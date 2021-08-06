package com.tianzhen.runnable;

import com.tianzhen.common.EBookConstant;
import com.tianzhen.entity.SearchResponseDO;
import com.tianzhen.httpclient.Requestor;
import com.tianzhen.service.BookInfoDataService;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;

import java.util.HashMap;
import java.util.Map;
@Slf4j
public class SearchBookAndSaveTask implements Runnable{

    private int startPage;

    private int endPage;

    private String keyWord;

    private Requestor requestor;

    private BookInfoDataService dataService;

    private EBookConstant eBookConstant;


    public SearchBookAndSaveTask(int startPage, int endPage, String keyWord, Requestor requestor, BookInfoDataService dataService,EBookConstant eBookConstant) {
        this.startPage = startPage;
        this.endPage = endPage;
        this.keyWord = keyWord;
        this.requestor = requestor;
        this.dataService = dataService;
        this.eBookConstant = eBookConstant;
    }

    @Override
    public void run() {
        Map<String,String> params = new HashMap<>();
        params.put("orderBy", "true");
        params.put("orderColumn", "file_name");
        params.put("owner", "true");
        params.put("fileType", "");
        params.put("c",keyWord);
        int currentPage = startPage;
        while(currentPage <= endPage){
            params.put("current", String.valueOf(currentPage));
            log.info("线程["+Thread.currentThread().getId()+"]当前查询第["+currentPage+"]页");
            HttpResponse response = requestor.doGet(eBookConstant.getSearchUrl(), params);
            // 获取响应消息实体
            HttpEntity responseEntity = response.getEntity();
            // 响应状态
            log.info("execute search response status:" + response.getStatusLine());
            if(responseEntity != null) {
                SearchResponseDO responseDO2 = dataService.constructSearchResponseDO(responseEntity);
                dataService.saveBookInfoList(responseDO2);
            }
            currentPage++;
        }
        log.info("线程["+Thread.currentThread().getId()+"]本次查询并保存信息结束");
    }
}
