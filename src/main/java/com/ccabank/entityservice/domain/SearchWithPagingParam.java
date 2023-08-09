package com.ccabank.entityservice.domain;

import com.ccabank.entityservice.dto.pagination.PageParam;

/**
 * @author : <a href="mailto:herve.foudjo@cca-bank.com">Herve FOUDJO</a>
 * @project : entityservice
 * @Package : com.ccabank.entityservice.domain
 * <p>
 * @date: 08/08/2023
 * @time: 10:35
 * <p>
 * Created with IntelliJ IDEA
 * To change this template use File | Settings | File Templates.
 */
public class SearchWithPagingParam {

    private Long categoryId;
    private Long breedId;
    private PageParam pageParam = new PageParam();

    public SearchWithPagingParam() {
    }

    /**
     * Gets categoryId.
     *
     * @return value of categoryId
     */
    public Long getCategoryId() {
        return categoryId;
    }

    /**
     * Sets categoryId.
     *
     * @param categoryId value of categoryId
     */
    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    /**
     * Gets breedId.
     *
     * @return value of breedId
     */
    public Long getBreedId() {
        return breedId;
    }

    /**
     * Sets breedId.
     *
     * @param breedId value of breedId
     */
    public void setBreedId(Long breedId) {
        this.breedId = breedId;
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
