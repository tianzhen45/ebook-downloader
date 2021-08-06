package com.tianzhen.controller;

import com.tianzhen.scheduler.UpdateBookDownLoadUrlTask;
import com.tianzhen.service.BookInfoCrawlerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/info")
public class BookInfoController {

    @Autowired
    private BookInfoCrawlerService bookInfoCrawlerService;

    @Autowired
    private UpdateBookDownLoadUrlTask urlTask;

    @GetMapping("/searchAndSave")
    public void searchAndSave(@RequestParam String keyWord){
        bookInfoCrawlerService.searchBookInfoAndSave(keyWord);

    }

    @GetMapping("/handFetchUrl")
    public void handFetchUrl(){
        urlTask.cron();
    }

}
