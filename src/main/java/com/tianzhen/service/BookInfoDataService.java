package com.tianzhen.service;

import com.alibaba.fastjson.JSONObject;
import com.tianzhen.dao.TBookInfoDao;
import com.tianzhen.entity.SearchResponseDO;
import com.tianzhen.entity.TBookInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class BookInfoDataService {

    @Autowired
    private TBookInfoDao tBookInfoDao;

    public SearchResponseDO constructSearchResponseDO(HttpEntity entity) {
        SearchResponseDO responseDO = new SearchResponseDO();
        try {
            String responseStr = EntityUtils.toString(entity);
            JSONObject jsonObject = JSONObject.parseObject(responseStr);
            JSONObject data = jsonObject.getJSONObject("data");
            responseDO.setCurrent(data.getInteger("current"));
            responseDO.setPages(data.getInteger("pages"));
            responseDO.setSearchCount(data.getBoolean("searchCount"));
            responseDO.setSize(data.getInteger("size"));
            responseDO.setTotal(data.getInteger("total"));
            responseDO.setRecords(data.getJSONArray("records").toJavaList(TBookInfo.class));
        } catch (Exception e) {
            log.error("constructSearchResponseDO failed..", e);
        }
        return responseDO;
    }

    public void saveBookInfoList(SearchResponseDO responseDO){
        List<TBookInfo> records = responseDO.getRecords();
        for(TBookInfo bookInfo : records){
            saveOrUpdateBook(bookInfo);
        }
        log.info("成功保存["+records.size()+"]条书籍信息!");
    }

    private void saveOrUpdateBook(TBookInfo bookInfo){
        TBookInfo bookInfo1 = tBookInfoDao.selectByPrimaryKey(bookInfo.getId());
        if(bookInfo1 != null){
            tBookInfoDao.updateByPrimaryKey(bookInfo);
        }else {
            tBookInfoDao.insert(bookInfo);
        }
    }
}
