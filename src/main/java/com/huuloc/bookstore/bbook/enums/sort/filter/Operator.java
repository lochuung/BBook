package com.huuloc.bookstore.bbook.enums.sort.filter;

import com.huuloc.bookstore.bbook.dto.filter.FilterRequest;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
public enum Operator {

    EQUAL {
        public <T> Predicate build(Root<T> root, CriteriaBuilder cb, FilterRequest request, Predicate predicate) {
            Object value = request.getFieldType().parse(request.getValue().toString());
            String[] relations = request.getKey().split("\\.");
            Expression<?> key;
            if (relations.length <= 1) {
                key = root.get(request.getKey());
            } else {
                key = root.join(relations[0]).get(relations[1]);
            }
            return cb.and(cb.equal(key, value), predicate);
        }
    },
    NOT_EQUAL {
        public <T> Predicate build(Root<T> root, CriteriaBuilder cb, FilterRequest request, Predicate predicate) {
            Object value = request.getFieldType().parse(request.getValue().toString());
            String[] relations = request.getKey().split("\\.");
            Expression<?> key;
            if (relations.length <= 1) {
                key = root.get(request.getKey());
            } else {
                key = root.join(relations[0]).get(relations[1]);
            }
            return cb.and(cb.notEqual(key, value), predicate);
        }
    },

    LIKE {
        public <T> Predicate build(Root<T> root, CriteriaBuilder cb, FilterRequest request, Predicate predicate) {
            String[] relations = request.getKey().split("\\.");
            Expression<String> key;
            if (relations.length <= 1) {
                key = root.get(request.getKey());
            } else {
                key = root.join(relations[0]).get(relations[1]);
            }

            return cb.and(cb.like(cb.upper(key), "%" + request.getValue().toString().toUpperCase() + "%"), predicate);
        }
    },

    LIKE_OR {
        public <T> Predicate build(Root<T> root, CriteriaBuilder cb, FilterRequest request, Predicate predicate) {
            String[] relations = request.getKey().split("\\.");
            Expression<String> key;
            if (relations.length <= 1) {
                key = root.get(request.getKey());
            } else {
                key = root.join(relations[0]).get(relations[1]);
            }

            if (request.getValues() != null && !request.getValues().isEmpty()) {
                for (Object value : request.getValues()) {
                    predicate = cb.or(predicate, cb.like(cb.upper(key), "%" + value.toString().toUpperCase() + "%"));
                }
                return predicate;
            }
            return cb.or(predicate, cb.like(cb.upper(key), "%" + request.getValue().toString().toUpperCase() + "%"));
        }
    },

    IN {
        public <T> Predicate build(Root<T> root, CriteriaBuilder cb, FilterRequest request, Predicate predicate) {
            String[] relations = request.getKey().split("\\.");
            Expression<String> key;
            if (relations.length <= 1) {
                key = root.get(request.getKey());
            } else {
                key = root.join(relations[0]).get(relations[1]);
            }
            CriteriaBuilder.In<Object> inClause = cb.in(key);

            List<Object> values = request.getValues();
            for (Object value : values) {
                inClause.value(request.getFieldType().parse(value.toString()));
            }
            return cb.and(inClause, predicate);
        }
    },

    BETWEEN {
        public <T> Predicate build(Root<T> root, CriteriaBuilder cb, FilterRequest request, Predicate predicate) {
            Object value = request.getFieldType().parse(request.getValue().toString());
            Object valueTo = request.getFieldType().parse(request.getValueTo().toString());
            if (request.getFieldType() == FieldType.DATE) {
                LocalDateTime startDate = (LocalDateTime) value;
                LocalDateTime endDate = (LocalDateTime) valueTo;
                Expression<LocalDateTime> key = root.get(request.getKey());
                return cb.and(cb.and(cb.greaterThanOrEqualTo(key, startDate), cb.lessThanOrEqualTo(key, endDate)), predicate);
            }

            if (request.getFieldType() != FieldType.CHAR && request.getFieldType() != FieldType.BOOLEAN) {
                Number start = (Number) value;
                Number end = (Number) valueTo;
                Expression<Number> key = root.get(request.getKey());
                return cb.and(cb.and(cb.ge(key, start), cb.le(key, end)), predicate);
            }

            log.info("Can not use between for {} field type.", request.getFieldType());
            return predicate;
        }
    }, GREATER_THAN_OR_EQUAL {
        @Override
        public <T> Predicate build(Root<T> root, CriteriaBuilder cb, FilterRequest request, Predicate predicate) {
            Object value = request.getFieldType().parse(request.getValue().toString());
            if (request.getFieldType() == FieldType.DATE) {
                LocalDateTime startDate = (LocalDateTime) value;
                Expression<LocalDateTime> key = root.get(request.getKey());
                return cb.and(cb.greaterThanOrEqualTo(key, startDate), predicate);
            }

            if (request.getFieldType() != FieldType.CHAR && request.getFieldType() != FieldType.BOOLEAN) {
                Number start = (Number) value;
                Expression<Number> key = root.get(request.getKey());
                return cb.and(cb.ge(key, start), predicate);
            }

            log.info("Can not use greater than or equal for {} field type.", request.getFieldType());
            return predicate;
        }
    }, LESS_THAN_OR_EQUAL {
        @Override
        public <T> Predicate build(Root<T> root, CriteriaBuilder cb, FilterRequest request, Predicate predicate) {
            Object value = request.getFieldType().parse(request.getValue().toString());
            if (request.getFieldType() == FieldType.DATE) {
                LocalDateTime endDate = (LocalDateTime) value;
                Expression<LocalDateTime> key = root.get(request.getKey());
                return cb.and(cb.lessThanOrEqualTo(key, endDate), predicate);
            }

            if (request.getFieldType() != FieldType.CHAR && request.getFieldType() != FieldType.BOOLEAN) {
                Number end = (Number) value;
                Expression<Number> key = root.get(request.getKey());
                return cb.and(cb.le(key, end), predicate);
            }

            log.info("Can not use less than or equal for {} field type.", request.getFieldType());
            return predicate;
        }
    };

    public abstract <T> Predicate build(Root<T> root, CriteriaBuilder cb, FilterRequest request, Predicate predicate);
}
