package com.tianzhen.entity;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class SearchResponseDO {

    private String countId;

    private int current;

    private int hitCount;

    private int maxLimit;

    private boolean optimizeCountSql;

    private List<TBookInfo> records;

    private int pages;

    private boolean searchCount;

    private int size;

    private int total;
}
