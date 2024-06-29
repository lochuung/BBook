package com.huuloc.bookstore.bbook.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.huuloc.bookstore.bbook.entity.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "publishers")
public class Publisher extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;

    @OneToMany(mappedBy = "publisher")
    @JsonIgnore
    private List<Book> books;
}
