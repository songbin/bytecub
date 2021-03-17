package com.bytecub.common.domain.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
  * ByteCub.cn.
  * Copyright (c) 2020-2020 All Rights Reserved.
  * 
  * @author bytecub@163.com  songbin
  * @version Id: PageDto.java, v 0.1 2020-12-15  Exp $$
  */
@Data
public class PageResDto<T> implements Serializable {
    private static final long serialVersionUID = 4042956845807247626L;
    private int pageNo = 1;
    private int          limit = 20;
    private long         total;
    private int          pages = 1;
    private              List<T> resultData;
    private              Object paramData;
//    /**
//     * 构造方法
//     * @param pageNo 页码
//     * @param limit  查询记录数
//     */
//    public PageResDto(final int pageNo, final int limit) {
//        int pn = pageNo <= 0 ? 1 : pageNo;
//        int lm = limit <= 0 ? 1 : limit;
//        this.pageNo = pn;
//        this.limit = lm;
//    }

    /**
     * 设置页码
     * @param pageNo 页码
     */
    public void setPageNo(final int pageNo) {
        int pn = pageNo <= 0 ? 1 : pageNo;
        this.pageNo = pn;
    }

    public int getLimit() {
        return limit;
    }

    /**
     * 设置limit
     * @param limit 查询条数
     */
    public void setLimit(final int limit) {
        int lm = limit <= 0 ? 1 : limit;
        this.limit = lm;
    }

    /**总页码*/
    public int getPages() {
        if (getTotal() == 0) {
            setPages(1);
        } else {
            if (getTotal() % getLimit() == 0) {
                setPages((int) getTotal() / getLimit());
            } else {
                setPages((int) (getTotal() / getLimit()) + 1);
            }
        }
        return this.pages;
    }

    public int setPages(long count) {
        if (count == 0) {
            this.pages = 1;
        } else {
            if (count % getLimit() == 0) {
                this.pages = ((int) count / getLimit());
            } else {
                this.pages = ((int) (count / getLimit()) + 1);
            }
        }
        return this.pages;
    }

    public void setTotal(long total){
        this.total = total;
        this.setPages(total);
    }

//    public PageResDto(int pageNo, int limit, long total, List<T> resultData, Object paramData) {
//        this.pageNo = pageNo;
//        this.limit = limit;
//        this.total = total;
//        this.pages = this.setPages(total);
//        this.resultData = resultData;
//        this.paramData = paramData;
//    }

    public static <T> PageResDto genResult(int pageNo, int limit, long total, List<T> resultData, Object paramData) {
        PageResDto pageResDto = new PageResDto();
        pageResDto.pageNo = pageNo;
        pageResDto.limit = limit;
        pageResDto.total = total;
        pageResDto.pages = pageResDto.setPages(total);
        pageResDto.resultData = resultData;
        pageResDto.paramData = paramData;
        return pageResDto;
    }
}

