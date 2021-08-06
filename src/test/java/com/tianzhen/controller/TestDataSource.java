package com.tianzhen.controller;

import com.tianzhen.dao.TBookInfoDao;
import com.tianzhen.entity.TBookInfo;
import com.tianzhen.httpclient.Requestor;
import com.tianzhen.service.BookInfoCrawlerService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestDataSource {

    @Autowired
    private TBookInfoDao tBookInfoDao;

    @Autowired
    private Requestor requestor;

    @Autowired
    private BookInfoCrawlerService bookInfoService;

    @Test
    public void test01(){
        TBookInfo tBookInfo = tBookInfoDao.selectByPrimaryKey(1);
        System.out.println(tBookInfo);
    }

    @Test
    public void test02(){
        System.out.println(requestor);
    }

    @Test
    public void testSavePython(){
        bookInfoService.searchBookInfoAndSave("java");
    }

    @Test
    public void test03(){
        List<TBookInfo> tBookInfos = tBookInfoDao.selectNoUrlBook(30);
        System.out.println(tBookInfos);
    }
}
