package com.example.mongospringboottest.dataModel.request.query;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "Paging")
public class PagingRequest {

    @NotNull(message = "paging.page is required")
    @Min(value = 0, message = "paging.page starts from {value}")
    private Integer page;
    
    @NotNull(message = "paging.size is required")
    @Min(value = 1, message = "paging.size starts from {value}")
    private Integer size;

    public PagingRequest() {
    }

    public PagingRequest(
            @NotNull(message = "paging.page is required") @Min(value = 0, message = "paging.page starts from {value}") Integer page,
            @NotNull(message = "paging.size is required") @Min(value = 1, message = "paging.size starts from {value}") Integer size) {
        this.page = page;
        this.size = size;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

}
