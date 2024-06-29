package com.huuloc.bookstore.bbook.dto.filter;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.huuloc.bookstore.bbook.enums.sort.filter.FieldType;
import com.huuloc.bookstore.bbook.enums.sort.filter.Operator;
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
public class FilterRequest {
    private String key;

    private Operator operator;

    private FieldType fieldType;

    private Object value;

    private Object valueTo;

    private List<Object> values;
}
