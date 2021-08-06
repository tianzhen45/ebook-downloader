package com.tianzhen.scheduler;

import com.alibaba.fastjson.JSONObject;
import com.tianzhen.common.EBookConstant;
import com.tianzhen.dao.TBookInfoDao;
import com.tianzhen.entity.TBookInfo;
import com.tianzhen.httpclient.Requestor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class UpdateBookDownLoadUrlTask {

    @Value("${yuedu.fetchNums}")
    private int fetchNums;

    @Autowired
    private EBookConstant eBookConstant;

    @Autowired
    private TBookInfoDao tBookInfoDao;

    @Autowired
    private Requestor requestor;

    @Scheduled(cron = "* 0/10 * * * ? ")
    public void cron() {
        List<TBookInfo> tBookInfos = tBookInfoDao.selectNoUrlBook(fetchNums);
        for (TBookInfo tBookInfo : tBookInfos) {
            String url = getDownLoadUrlById(tBookInfo.getId());
            tBookInfo.setDownloadUrl(url);
            tBookInfoDao.updateByPrimaryKey(tBookInfo);
        }
    }


    public String getDownLoadUrlById(int id){
        HttpResponse response = requestor.doGet(eBookConstant.getFetchDownloadUrl()+id);
        // 获取响应消息实体
        HttpEntity responseEntity = response.getEntity();
        // 响应状态
        String url = "";
        log.info("execute fetchDownloadUrl response status:" + response.getStatusLine());
        if(responseEntity != null){
            try {
                String jsonStr = EntityUtils.toString(responseEntity);
                JSONObject jsonObject = JSONObject.parseObject(jsonStr);
                JSONObject data = jsonObject.getJSONObject("data");
                url = data.getString("url");
            }catch (Exception e){
                log.error("查询文件下载地址出错：",e);
            }
        }
        return url;
    }

}
