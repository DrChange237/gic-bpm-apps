package com.ccabank.signservice.dto.pagination;

/**
 * @author : <a href="mailto:herve.foudjo@cca-bank.com">Herve FOUDJO</a>
 * @project : entityservice
 * @Package : com.ccabank.entityservice.dto.pagination
 * <p>
 * @date: 08/08/2023
 * @time: 10:35
 * <p>
 * Created with IntelliJ IDEA
 * To change this template use File | Settings | File Templates.
 */
public class PageInfo {

    private Long totalElements;
    private Integer numberOfElement;
    private Integer currentPage;
    private Integer pageSize;
    private Integer totalPage;
    private Boolean hasNext;
    private Boolean hasPrevious;
    private Boolean isFirst;
    private Boolean isLast;

    public PageInfo() {
    }

    /**
     * Gets totalElements.
     *
     * @return value of totalElements
     */
    public Long getTotalElements() {
        return totalElements;
    }

    /**
     * Sets totalElements.
     *
     * @param totalElements value of totalElements
     */
    public void setTotalElements(Long totalElements) {
        this.totalElements = totalElements;
    }

    /**
     * Gets numberOfElement.
     *
     * @return value of numberOfElement
     */
    public Integer getNumberOfElement() {
        return numberOfElement;
    }

    /**
     * Sets numberOfElement.
     *
     * @param numberOfElement value of numberOfElement
     */
    public void setNumberOfElement(Integer numberOfElement) {
        this.numberOfElement = numberOfElement;
    }

    /**
     * Gets currentPage.
     *
     * @return value of currentPage
     */
    public Integer getCurrentPage() {
        return currentPage;
    }

    /**
     * Sets currentPage.
     *
     * @param currentPage value of currentPage
     */
    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    /**
     * Gets pageSize.
     *
     * @return value of pageSize
     */
    public Integer getPageSize() {
        return pageSize;
    }

    /**
     * Sets pageSize.
     *
     * @param pageSize value of pageSize
     */
    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    /**
     * Gets totalPage.
     *
     * @return value of totalPage
     */
    public Integer getTotalPage() {
        return totalPage;
    }

    /**
     * Sets totalPage.
     *
     * @param totalPage value of totalPage
     */
    public void setTotalPage(Integer totalPage) {
        this.totalPage = totalPage;
    }

    /**
     * Gets hasNext.
     *
     * @return value of hasNext
     */
    public Boolean getHasNext() {
        return hasNext;
    }

    /**
     * Sets hasNext.
     *
     * @param hasNext value of hasNext
     */
    public void setHasNext(Boolean hasNext) {
        this.hasNext = hasNext;
    }

    /**
     * Gets hasPrevious.
     *
     * @return value of hasPrevious
     */
    public Boolean getHasPrevious() {
        return hasPrevious;
    }

    /**
     * Sets hasPrevious.
     *
     * @param hasPrevious value of hasPrevious
     */
    public void setHasPrevious(Boolean hasPrevious) {
        this.hasPrevious = hasPrevious;
    }

    /**
     * Gets isFirst.
     *
     * @return value of isFirst
     */
    public Boolean getFirst() {
        return isFirst;
    }

    /**
     * Sets isFirst.
     *
     * @param first value of isFirst
     */
    public void setFirst(Boolean first) {
        isFirst = first;
    }

    /**
     * Gets isLast.
     *
     * @return value of isLast
     */
    public Boolean getLast() {
        return isLast;
    }

    /**
     * Sets isLast.
     *
     * @param last value of isLast
     */
    public void setLast(Boolean last) {
        isLast = last;
    }
}
