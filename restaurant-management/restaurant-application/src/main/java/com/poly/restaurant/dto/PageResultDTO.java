package com.poly.restaurant.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageResultDTO<T> {
    private List<T> items;
    @JsonProperty("currentPage")
    private int currentPage;
    @JsonProperty("pageSize")
    private int pageSize;
    @JsonProperty("totalPages")
    private int totalPages;
    @JsonProperty("totalItems")
    private int totalItems;
}
