package com.db.json.request;

import lombok.Getter;
import lombok.Setter;


public class SearchRequest {
    private SearchCriteria search;
   private String value;
    private int page;
    private int limit;

    public SearchCriteria getSearch() {
        return search;
    }

    public void setSearch(SearchCriteria search) {
        this.search = search;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public SearchRequest(SearchCriteria search, int page, int limit) {
        this.search = search;
        this.page = page;
        this.limit = limit;
    }
}
