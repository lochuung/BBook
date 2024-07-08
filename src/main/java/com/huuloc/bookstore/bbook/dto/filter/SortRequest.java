package com.huuloc.bookstore.bbook.dto.filter;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.huuloc.bookstore.bbook.enums.sort.filter.SortDirection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SortRequest {

    private String key;

    private List<String> keys;

    private List<Object> likeValues;

    private SortDirection direction;

}