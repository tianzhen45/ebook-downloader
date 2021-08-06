package com.tianzhen.dao;

import com.tianzhen.entity.TBookInfo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TBookInfoDao {
    int deleteByPrimaryKey(Integer id);

    int insert(TBookInfo record);

    int insertSelective(TBookInfo record);

    TBookInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TBookInfo record);

    int updateByPrimaryKey(TBookInfo record);

    List<TBookInfo> selectNoUrlBook(int size);
}