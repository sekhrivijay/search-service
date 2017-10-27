package com.micro.services.search.api.response;

import java.io.Serializable;
import java.util.List;

public class Pagination implements Serializable {
    private long activePageNumber;
    private long totalNumberOfPages;
    private long numberOfRecordsPerPage;
    private List<Page> previousPageList;
    private List<Page> nextPageList;
    private List<Page> firstPageList;
    private List<Page> lastPageList;

    public long getActivePageNumber() {
        return activePageNumber;
    }

    public void setActivePageNumber(long activePageNumber) {
        this.activePageNumber = activePageNumber;
    }

    public long getTotalNumberOfPages() {
        return totalNumberOfPages;
    }

    public void setTotalNumberOfPages(long totalNumberOfPages) {
        this.totalNumberOfPages = totalNumberOfPages;
    }

    public long getNumberOfRecordsPerPage() {
        return numberOfRecordsPerPage;
    }

    public void setNumberOfRecordsPerPage(long numberOfRecordsPerPage) {
        this.numberOfRecordsPerPage = numberOfRecordsPerPage;
    }

    public List<Page> getPreviousPageList() {
        return previousPageList;
    }

    public void setPreviousPageList(List<Page> previousPageList) {
        this.previousPageList = previousPageList;
    }

    public List<Page> getNextPageList() {
        return nextPageList;
    }

    public void setNextPageList(List<Page> nextPageList) {
        this.nextPageList = nextPageList;
    }

    public List<Page> getFirstPageList() {
        return firstPageList;
    }

    public void setFirstPageList(List<Page> firstPageList) {
        this.firstPageList = firstPageList;
    }

    public List<Page> getLastPageList() {
        return lastPageList;
    }

    public void setLastPageList(List<Page> lastPageList) {
        this.lastPageList = lastPageList;
    }

    @Override
    public String toString() {
        return "Pagination{" +
                "activePageNumber=" + activePageNumber +
                ", totalNumberOfPages=" + totalNumberOfPages +
                ", numberOfRecordsPerPage=" + numberOfRecordsPerPage +
                ", previousPageList=" + previousPageList +
                ", nextPageList=" + nextPageList +
                ", firstPageList=" + firstPageList +
                ", lastPageList=" + lastPageList +
                '}';
    }


}
