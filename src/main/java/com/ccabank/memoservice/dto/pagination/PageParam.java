package com.ccabank.memoservice.dto.pagination;

import com.ccabank.memoservice.constant.PaginationConstant;
import com.ccabank.memoservice.util.StringUtil;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * @author : <a href="mailto:herve.foudjo@cca-bank.com">Herve FOUDJO</a>
 * @project : entityservice
 * @Package : com.ccabank.entityservice.dto.pagination
 * <p>
 * @date: 08/08/2023
 * @time: 10:38
 * <p>
 * Created with IntelliJ IDEA
 * To change this template use File | Settings | File Templates.
 */
public class PageParam {

    private Integer pageIndex = PaginationConstant.DEFAULT_PAGE_INDEX;
    private Integer pageSize = PaginationConstant.DEFAULT_PAGE_SIZE;
    private String sortBy = null;
    private Boolean isAcsending = true;

    public Boolean isSort() {
        return (sortBy == null || StringUtil.isBlank(sortBy)) ? false : true;
    }

    /**
     * Gets pageIndex.
     *
     * @return value of pageIndex
     */
    public Integer getPageIndex() {
        return pageIndex;
    }

    /**
     * Sets pageIndex.
     *
     * @param pageIndex value of pageIndex
     */
    public void setPageIndex(Integer pageIndex) {
        this.pageIndex = pageIndex;
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
     * Gets sortBy.
     *
     * @return value of sortBy
     */
    public String getSortBy() {
        return sortBy;
    }

    /**
     * Sets sortBy.
     *
     * @param sortBy value of sortBy
     */
    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    /**
     * Gets isAcsending.
     *
     * @return value of isAcsending
     */
    public Boolean getAcsending() {
        return isAcsending;
    }

    /**
     * Sets isAcsending.
     *
     * @param acsending value of isAcsending
     */
    public void setAcsending(Boolean acsending) {
        isAcsending = acsending;
    }

    public Sort getSort() {
        if (sortBy != null || !StringUtil.isBlank(sortBy)) {
            return isAcsending ? Sort.by(sortBy) : Sort.by(sortBy).descending();
        } else {
            return null;
        }
    }

    public Pageable getPageable() {
        if (sortBy == null || StringUtil.isBlank(sortBy)) {
            return PageRequest.of(this.pageIndex, this.pageSize);
        } else {
            return PageRequest.of(this.pageIndex, this.pageSize,
                    isAcsending ? Sort.by(sortBy) : Sort.by(sortBy).descending());
        }
    }

}
