package com.tianzhen.entity;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * t_book_info
 * @author 
 */
@Data
public class TBookInfo implements Serializable {
    private Integer id;

    private Integer fileId;

    private String fileName;

    private Double fileSize;

    private String fileSuffix;

    private String fileType;

    private String openFile;

    private String owner;

    private Integer userId;

    private Date createTime;

    private String downloadUrl;

    private static final long serialVersionUID = 1L;
}