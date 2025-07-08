package com.poly.customerapplicationservice.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class PageResult <T>{
    private List<T> items;
    private int currentPage;
    private int pageSize;
    private int totalPages;
    private int totalItems;

    public PageResult(List<T> items, int currentPage, int pageSize, int totalItems) {
        this.items = items;
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.totalItems = totalItems;
        this.totalPages = (int) Math.ceil((double) totalItems / pageSize);
    }

}
