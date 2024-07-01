package com.huuloc.bookstore.bbook.entity;

import com.huuloc.bookstore.bbook.entity.common.BaseEntity;
import com.huuloc.bookstore.bbook.entity.enums.OrderState;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = Book.TABLE_NAME)
@SQLDelete(sql = "UPDATE " + Book.TABLE_NAME + " SET deleted = true WHERE id=?")
@SQLRestriction("deleted=false")
public class Book extends BaseEntity {
    public static final String TABLE_NAME = "books";

    @PrePersist
    void prePersist() {
        if (this.averageRating == null) {
            this.averageRating = 0.0;
        }
        if (this.totalRating == null) {
            this.totalRating = 0L;
        }
        if (this.totalSold == null) {
            this.totalSold = 0L;
        }

        if (this.orderItems != null) {
            this.totalSold = this.orderItems.stream().mapToLong(OrderItem::getQuantity).sum();
            this.orderItems.stream()
                    .filter(orderItem -> orderItem.getOrder().getState() == OrderState.NEW)
                    .forEach(orderItem -> {
                        orderItem.setPrice(this.price);
                        orderItem.setTotalPrice(orderItem.getQuantity() * this.price);
                    });
        }
    }

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    private String title;
    @Column(columnDefinition = "TEXT")
    @Lob
    private String shortDescription;
    @Column(columnDefinition = "TEXT")
    @Lob
    private String description;
    private String isbn;
    @Column(unique = true)
    private String slug;

    private Long availableQuantity;
    private Double price;
    private Integer pageCount;
    private LocalDateTime publishedDate;

    private String thumbnailUrl;

    @ColumnDefault("0")
    private Double averageRating;

    @ColumnDefault("0")
    private Long totalRating;

    @ColumnDefault("0")
    private Long totalSold;

    @ColumnDefault("false")
    private boolean deleted;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Image> images;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "book_genre", joinColumns = @JoinColumn(name = "book_id"), inverseJoinColumns = @JoinColumn(name = "genre_id"))
    private List<Genre> genres;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "publisher_id")
    private Publisher publisher;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "book_author", joinColumns = @JoinColumn(name = "book_id"), inverseJoinColumns = @JoinColumn(name = "author_id"))
    private java.util.List<Author> authors;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Review> reviews;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrderItem> orderItems;
}
