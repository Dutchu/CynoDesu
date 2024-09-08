package edu.weeia.cynodesu.controllers;

import org.springframework.data.domain.Page;

public class PaginationModel<T> {
    private final Page<T> page;
    private final String url;

    public PaginationModel(Page<T> page, String url) {
        this.page = page;
        this.url = url;
    }

    public Page<T> getPage() {
        return page;
    }

    public String getUrl() {
        return url;
    }
}