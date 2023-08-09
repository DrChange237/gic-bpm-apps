package com.ccabank.entityservice.domain;

import com.ccabank.entityservice.dto.pagination.PageParam;

/**
 * @author : <a href="mailto:herve.foudjo@cca-bank.com">Herve FOUDJO</a>
 * @project : entityservice
 * @Package : com.ccabank.entityservice.domain
 * <p>
 * @date: 08/08/2023
 * @time: 10:30
 * <p>
 * Created with IntelliJ IDEA
 * To change this template use File | Settings | File Templates.
 */
public class FullTextSearchWithPagingParam {

    private String text;
    private PageParam pageParam = new PageParam();

    public FullTextSearchWithPagingParam() {
    }

    public FullTextSearchWithPagingParam(String text,
                                         PageParam pageParam) {
        this.text = text;
        this.pageParam = pageParam;
    }

    /**
     * Gets text.
     *
     * @return value of text
     */
    public String getText() {
        return text;
    }

    /**
     * Sets text.
     *
     * @param text value of text
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * Gets pageParam.
     *
     * @return value of pageParam
     */
    public PageParam getPageParam() {
        return pageParam;
    }

    /**
     * Sets pageParam.
     *
     * @param pageParam value of pageParam
     */
    public void setPageParam(PageParam pageParam) {
        this.pageParam = pageParam;
    }
}
