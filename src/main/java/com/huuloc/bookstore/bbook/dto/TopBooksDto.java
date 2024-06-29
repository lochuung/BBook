package com.huuloc.bookstore.bbook.dto;

import com.huuloc.bookstore.bbook.entity.Book;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Builder
@Getter
@Setter
public class TopBooksDto {
    private List<Book> topRateBooks;
    private List<Book> topNewBooks;
    private List<Book> topSellBooks;
}
