package com.huuloc.bookstore.bbook.entity;

import com.huuloc.bookstore.bbook.entity.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "books")
public class Book extends BaseEntity {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private Long availableQuantity;
    private Double price;
    private Integer pages;
    private LocalDateTime publishedDate;

    private String coverImage;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private List<Image> images;

    @ManyToOne
    @JoinColumn(name = "genre_id")
    private Genre genre;

    @ManyToOne
    @JoinColumn(name = "publisher_id")
    private Publisher publisher;

    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "books")
    private java.util.Set<Author> authors;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private List<Review> reviews;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private List<OrderItem> orderItems;
}
