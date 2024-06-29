package com.huuloc.bookstore.bbook.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.huuloc.bookstore.bbook.entity.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "authors")
public class Author extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private String image;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "authors")
    @JsonIgnore
    private java.util.List<Book> books;
}
