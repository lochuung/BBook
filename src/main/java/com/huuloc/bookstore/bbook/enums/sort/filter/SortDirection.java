package com.huuloc.bookstore.bbook.enums.sort.filter;

import com.huuloc.bookstore.bbook.dto.filter.SortRequest;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Root;
import org.apache.commons.lang3.StringUtils;

public enum SortDirection {
    ASC {
        public <T> Order build(Root<T> root, CriteriaBuilder cb, SortRequest request) {
            return cb.asc(root.get(request.getKey()));
        }
    }, DESC {
        public <T> Order build(Root<T> root, CriteriaBuilder cb, SortRequest request) {
            return cb.desc(root.get(request.getKey()));
        }
    }, ASC_LIKE {
        @Override
        public <T> Order build(Root<T> root, CriteriaBuilder cb, SortRequest request) {
            CriteriaBuilder.Case<Number> selectCase = cb.selectCase();
            int order = 1;
            for (String key : request.getKeys()) {
                Expression<String> expression;
                if (key.contains(".")) {
                    String[] relations = key.split("\\.");
                    expression = root.get(relations[0]).get(relations[1]);
                } else {
                    expression = root.get(key);
                }

                for (Object likeValue : request.getLikeValues()) {
                    selectCase = selectCase
                            .when(cb.like(expression, "%" + StringUtils.upperCase((String) likeValue) + "%"),
                                    order++);
                }
            }
            return cb.asc(selectCase.otherwise(order));
        }
    };

    public abstract <T> Order build(Root<T> root, CriteriaBuilder cb, SortRequest request);
}
