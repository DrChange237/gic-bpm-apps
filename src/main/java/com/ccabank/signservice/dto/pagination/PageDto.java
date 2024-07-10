package com.ccabank.signservice.dto.pagination;

import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @author : <a href="mailto:herve.foudjo@cca-bank.com">Herve FOUDJO</a>
 * @project : entityservice
 * @Package : com.ccabank.entityservice.dto.pagination
 * <p>
 * @date: 08/08/2023
 * @time: 10:32
 * <p>
 * Created with IntelliJ IDEA
 * To change this template use File | Settings | File Templates.
 */
public class PageDto<T> {

    private List<T> content;
    private PageInfo pageInfo;

    public PageDto(Page<T> page) {

        if (page.hasContent()) {
            this.content = page.getContent();
        }

        this.pageInfo = new PageInfo();
        this.pageInfo.setCurrentPage(page.getNumber());
        this.pageInfo.setTotalPage(page.getTotalPages());
        this.pageInfo.setPageSize(page.getSize());
        this.pageInfo.setHasNext(page.hasNext());
        this.pageInfo.setHasPrevious(page.hasPrevious());
        this.pageInfo.setFirst(page.isFirst());
        this.pageInfo.setLast(page.isLast());
        this.pageInfo.setNumberOfElement(page.getNumberOfElements());
        this.pageInfo.setTotalElements(page.getTotalElements());
    }

    public PageDto() {
    }

    /**
     * Gets content.
     *
     * @return value of content
     */
    public List<T> getContent() {
        return content;
    }

    /**
     * Sets content.
     *
     * @param content value of content
     */
    public void setContent(List<T> content) {
        this.content = content;
    }

    /**
     * Gets pageInfo.
     *
     * @return value of pageInfo
     */
    public PageInfo getPageInfo() {
        return pageInfo;
    }

    /**
     * Sets pageInfo.
     *
     * @param pageInfo value of pageInfo
     */
    public void setPageInfo(PageInfo pageInfo) {
        this.pageInfo = pageInfo;
    }
}
