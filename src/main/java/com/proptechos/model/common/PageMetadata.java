package com.proptechos.model.common;

public class PageMetadata {

    private final long page;

    private final long size;

    private final long totalPages;

    private final long totalElements;

    public PageMetadata(long page, long size) {
        this.page = page;
        this.size = size;
        this.totalPages = 0;
        this.totalElements = 0;
    }

    public PageMetadata(long page, long size, long totalPages, long totalElements) {
        this.page = page;
        this.size = size;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
    }

    public long getPage() {
        return page;
    }

    public long getSize() {
        return size;
    }

    public long getTotalPages() {
        return totalPages;
    }

    public long getTotalElements() {
        return totalElements;
    }
}
