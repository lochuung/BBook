package com.huuloc.bookstore.bbook.controller.customer;

import com.huuloc.bookstore.bbook.dto.filter.FilterRequest;
import com.huuloc.bookstore.bbook.dto.filter.SearchRequest;
import com.huuloc.bookstore.bbook.dto.filter.SortRequest;
import com.huuloc.bookstore.bbook.entity.Book;
import com.huuloc.bookstore.bbook.entity.Image;
import com.huuloc.bookstore.bbook.enums.sort.filter.FieldType;
import com.huuloc.bookstore.bbook.enums.sort.filter.Operator;
import com.huuloc.bookstore.bbook.enums.sort.filter.SortDirection;
import com.huuloc.bookstore.bbook.service.BookService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.stream.IntStreams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/book")
public class BookController {
    @Autowired
    private BookService bookService;

    @GetMapping("/{slug}")
    public String getBookDetail(Model model, @PathVariable("slug") String slug) {
        Book book = bookService.findBySlug(slug);
        // images array string
        List<String> images = new ArrayList<>();
        images.add("\"" + book.getThumbnailUrl() + "\"");

        for (Image image : book.getImages()) {
            images.add("\"" + image.getUrl() + "\"");
        }
        String imagesString = "[" + String.join(",", images) + "]";
        model.addAttribute("images", imagesString);
        model.addAttribute("book", book);
        return "customer/book-detail";
    }

    @GetMapping("/search")
    public String search(Model model,
                         @RequestParam(value = "keyword", required = false) String keyword,
                         @RequestParam("page") Optional<Integer> page,
                         @RequestParam("size") Optional<Integer> size,
                         @RequestParam("sortBy") Optional<String> sortBy,
                         @RequestParam("sortDirection") Optional<String> sortDirection,
                         @RequestParam("genres") Optional<List<Long>> genres,
                         @RequestParam("priceFrom") Optional<Double> priceFrom,
                         @RequestParam("priceTo") Optional<Double> priceTo) {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(12);
        String sort = sortBy.orElse("id");
        SortDirection direction = SortDirection.ASC;
        if ("DESC".equals(sortDirection.orElse(null))) {
            direction = SortDirection.DESC;
        }

        StringBuilder restoredUrl = new StringBuilder("/book/search?size=" + pageSize);

        List<FilterRequest> filters = new ArrayList<>();
        List<SortRequest> sorts = new ArrayList<>();
        if (keyword != null) {
            List<Object> keywords = List.of(keyword.split(" "));
            filters.add(FilterRequest.builder()
                    .key("title")
                    .operator(Operator.LIKE_OR)
                    .fieldType(FieldType.STRING)
                    .values(keywords)
                    .build());

            sorts.add(SortRequest.builder()
                    .direction(SortDirection.ASC_LIKE)
                    .keys(List.of("title", "authors.name"))
                    .likeValues(keywords)
                    .build());

            filters.add(FilterRequest.builder()
                    .key("authors.name")
                    .operator(Operator.LIKE_OR)
                    .fieldType(FieldType.STRING)
                    .values(keywords)
                    .build());

            restoredUrl.append("&keyword=").append(keyword);
        }
        if (genres.isPresent() && !genres.get().isEmpty()) {
            List<Object> genreIds = new ArrayList<>(genres.get());
            filters.add(FilterRequest.builder()
                    .key("genres.id")
                    .operator(Operator.IN)
                    .fieldType(FieldType.LONG)
                    .values(genreIds)
                    .build());

            restoredUrl.append("&genres=").append(StringUtils.join(genres.get(), ","));
        }
        if (priceFrom.isPresent() && priceTo.isPresent()) {
            filters.add(FilterRequest.builder()
                    .key("price")
                    .operator(Operator.BETWEEN)
                    .fieldType(FieldType.DOUBLE)
                    .value(priceFrom.get())
                    .valueTo(priceTo.get())
                    .build());

            restoredUrl.append("&priceFrom=").append(priceFrom.get());
            restoredUrl.append("&priceTo=").append(priceTo.get());
        } else if (priceFrom.isPresent()) {
            filters.add(FilterRequest.builder()
                    .key("price")
                    .operator(Operator.GREATER_THAN_OR_EQUAL)
                    .fieldType(FieldType.DOUBLE)
                    .value(priceFrom.get())
                    .build());

            restoredUrl.append("&priceFrom=").append(priceFrom.get());
        } else if (priceTo.isPresent()) {
            filters.add(FilterRequest.builder()
                    .key("price")
                    .operator(Operator.LESS_THAN_OR_EQUAL)
                    .fieldType(FieldType.DOUBLE)
                    .value(priceTo.get())
                    .build());

            restoredUrl.append("&priceTo=").append(priceTo.get());
        }
        if (sortBy.isPresent()) {
            sorts.add(SortRequest.builder()
                    .key(sort)
                    .direction(direction)
                    .build());
            restoredUrl.append("&sortBy=").append(sort);
            restoredUrl.append("&sortDirection=").append(direction);
        }

        SearchRequest searchRequest = SearchRequest.builder()
                .filters(filters)
                .sorts(sorts)
                .page(currentPage - 1)
                .size(pageSize)
                .build();

        Page<Book> books = bookService.search(searchRequest);
        int totalPages = books.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .filter(i -> i > currentPage - 5 && i < currentPage + 5)
                    .boxed()
                    .toList();
            model.addAttribute("pageNumbers", pageNumbers);
            model.addAttribute("totalPages", totalPages);
        }
        model.addAttribute("books", books);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("keyword", keyword);
        model.addAttribute("restoredUrl", restoredUrl.toString());


        return "customer/search";
    }
}
