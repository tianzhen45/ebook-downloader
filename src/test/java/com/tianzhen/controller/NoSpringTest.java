package com.tianzhen.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.tianzhen.entity.TBookInfo;
import javafx.util.Pair;
import org.junit.Test;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class NoSpringTest {

    @Test
    public void testJson(){
        String jsonStr = "{\"code\":0,\"msg\":null,\"data\":{\"records\":[{\"id\":\"247935\",\"owner\":false,\"fileType\":1,\"fileName\":\"预测分析：Python语言实现.pdf\",\"fileSuffix\":\"pdf\",\"fileSize\":\"40035\",\"fileId\":\"247909\",\"createTime\":\"2021-07-04 21:16:23\",\"openFile\":true,\"userId\":49},{\"id\":\"132446\",\"owner\":false,\"fileType\":1,\"fileName\":\"预测分析：Python语言实现.mobi\",\"fileSuffix\":\"mobi\",\"fileSize\":\"55614\",\"fileId\":\"132425\",\"createTime\":\"2021-07-03 21:16:09\",\"openFile\":true,\"userId\":49},{\"id\":\"4791\",\"owner\":false,\"fileType\":1,\"fileName\":\"预测分析：Python语言实现.epub\",\"fileSuffix\":\"epub\",\"fileSize\":\"41616\",\"fileId\":\"4779\",\"createTime\":\"2021-07-01 23:24:25\",\"openFile\":true,\"userId\":49},{\"id\":\"91531\",\"owner\":false,\"fileType\":1,\"fileName\":\"预测分析：Python语言实现.azw3\",\"fileSuffix\":\"azw3\",\"fileSize\":\"57509\",\"fileId\":\"91511\",\"createTime\":\"2021-07-02 23:45:07\",\"openFile\":true,\"userId\":49},{\"id\":\"247878\",\"owner\":false,\"fileType\":1,\"fileName\":\"零起点PYTHON机器学习快速入门_14189739.pdf\",\"fileSuffix\":\"pdf\",\"fileSize\":\"79324\",\"fileId\":\"247852\",\"createTime\":\"2021-07-04 21:15:18\",\"openFile\":true,\"userId\":49},{\"id\":\"301610\",\"owner\":false,\"fileType\":1,\"fileName\":\"零起点Python大数据与量化交易高清书签.pdf\",\"fileSuffix\":\"pdf\",\"fileSize\":\"22063\",\"fileId\":\"301581\",\"createTime\":\"2021-07-05 00:37:10\",\"openFile\":true,\"userId\":49},{\"id\":\"301611\",\"owner\":false,\"fileType\":1,\"fileName\":\"零起点Python大数据与量化交易.pdf\",\"fileSuffix\":\"pdf\",\"fileSize\":\"23954\",\"fileId\":\"301582\",\"createTime\":\"2021-07-05 00:37:10\",\"openFile\":true,\"userId\":49},{\"id\":\"301598\",\"owner\":false,\"fileType\":1,\"fileName\":\"零基础学Python.pdf\",\"fileSuffix\":\"pdf\",\"fileSize\":\"17297\",\"fileId\":\"301569\",\"createTime\":\"2021-07-05 00:37:02\",\"openFile\":true,\"userId\":49},{\"id\":\"210143\",\"owner\":false,\"fileType\":1,\"fileName\":\"零基础学Python.mobi\",\"fileSuffix\":\"mobi\",\"fileSize\":\"9691\",\"fileId\":\"210122\",\"createTime\":\"2021-07-04 00:01:53\",\"openFile\":true,\"userId\":49},{\"id\":\"14540\",\"owner\":false,\"fileType\":1,\"fileName\":\"零基础学Python.epub\",\"fileSuffix\":\"epub\",\"fileSize\":\"7408\",\"fileId\":\"14526\",\"createTime\":\"2021-07-02 01:33:31\",\"openFile\":true,\"userId\":49}],\"total\":762,\"size\":10,\"current\":1,\"orders\":[{\"column\":\"file_name\",\"asc\":false},{\"column\":\"id\",\"asc\":true}],\"optimizeCountSql\":true,\"hitCount\":false,\"countId\":null,\"maxLimit\":null,\"searchCount\":true,\"pages\":77}}";
        JSONObject jsonObject = JSONObject.parseObject(jsonStr);
        JSONObject data = jsonObject.getJSONObject("data");
        JSONArray records = data.getJSONArray("records");
        List<TBookInfo> tBookInfos = records.toJavaList(TBookInfo.class);
        System.out.println(tBookInfos);
    }

    @Test
    public void testPages(){
        int pages = 11;
        int SEARCH_MAX_TASK_NUM =5 ;
        int MIN_PROCESS_PAGES = 5;
        int currentPage = 1;
        if(pages > 0){
            int threadNum = SEARCH_MAX_TASK_NUM;
            int pageLength = (pages / threadNum + (pages % threadNum == 0 ? 0 : 1)) ;
            if(pages <= SEARCH_MAX_TASK_NUM * (MIN_PROCESS_PAGES-1)){
                threadNum = pages / MIN_PROCESS_PAGES + (pages % MIN_PROCESS_PAGES == 0 ? 0 : 1);
                pageLength = MIN_PROCESS_PAGES;
            }

            List<Pair<Integer,Integer>> pagePairList = new ArrayList<>();

            while(currentPage <= pages){
                int endPage = currentPage + pageLength -1;
                if(endPage > pages){
                    endPage = pages;
                }
                Pair<Integer, Integer> pagePair = new Pair<>(currentPage,endPage);
                pagePairList.add(pagePair);
                currentPage = endPage+1;
            }

            System.out.println(pagePairList);
        }
    }
}
