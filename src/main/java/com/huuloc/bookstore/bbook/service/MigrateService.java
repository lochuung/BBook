package com.huuloc.bookstore.bbook.service;

import com.huuloc.bookstore.bbook.entity.Author;
import com.huuloc.bookstore.bbook.entity.Book;
import com.huuloc.bookstore.bbook.entity.Genre;
import com.huuloc.bookstore.bbook.repository.AuthorRepository;
import com.huuloc.bookstore.bbook.repository.BookRepository;
import com.huuloc.bookstore.bbook.repository.GenreRepository;
import com.huuloc.bookstore.bbook.service.auth.UserService;
import com.huuloc.bookstore.bbook.util.SlugUtil;
import com.nimbusds.jose.util.IOUtils;
import jakarta.transaction.Transactional;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class MigrateService {
    @Autowired
    private GenreRepository genreRepository;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private UserService userService;

    @Autowired
    private JSONParser jsonParser;

    @Transactional
    public void migrateBooksData() throws IOException, ParseException {
        if (bookRepository.count() > 0) {
            return;
        }

        InputStream inputStream = getClass().getResourceAsStream("/data/data.json");
        assert inputStream != null;
        JSONArray jsonArray = (JSONArray) jsonParser.parse(IOUtils.readInputStreamToString(inputStream,
                StandardCharsets.UTF_8));

        long defaultQuantity = 1000;

        for (Object object : jsonArray) {
            JSONObject jsonObject = (JSONObject) object;
            List<Author> finalAuthors = new ArrayList<>();
            JSONArray authorsJson = (JSONArray) jsonObject.get("authors");
            authorsJson.forEach(author -> {
                Author authorEntity = Author.builder()
                        .name((String) author)
                        .build();
                if (StringUtils.isEmpty(authorEntity.getName())) {
                    return;
                }
                Author existed = authorRepository.findByName(authorEntity.getName());
                if (existed != null) {
                    finalAuthors.add(existed);
                    return;
                }
                authorEntity = authorRepository.save(authorEntity);
                finalAuthors.add(authorEntity);
            });

            List<Genre> genres = new ArrayList<>();
            JSONArray genresJson = (JSONArray) jsonObject.get("categories");
            genresJson.forEach(genre -> {
                Genre genreEntity = Genre.builder()
                        .name((String) genre)
                        .slug((SlugUtil.toSlug((String) genre)))
                        .build();
                if (StringUtils.isEmpty(genreEntity.getName())) {
                    return;
                }
                Genre existed = genreRepository.findBySlug(genreEntity.getSlug());
                if (existed != null) {
                    genres.add(existed);
                    return;
                }
                genres.add(genreEntity);
            });
            List<Genre> finalGenres = genreRepository.saveAll(genres);

            String title = (String) jsonObject.get("title");
            String isbn = (String) jsonObject.get("isbn");
            Integer pageCount = (Integer) jsonObject.get("pageCount");
            JSONObject publishedDateJson = (JSONObject) jsonObject.get("publishedDate");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX");
            LocalDateTime publishedDate = publishedDateJson == null ? null : LocalDateTime.parse((String) publishedDateJson.get("$date"), formatter);
            String thumbnailUrl = (String) jsonObject.get("thumbnailUrl");
            String shortDescription = (String) jsonObject.get("shortDescription");
            String longDescription = (String) jsonObject.get("longDescription");
            // random price from 30.000 to 170.000
            double price = Math.random() * 170000 + 30000;
            String slug = SlugUtil.toSlug(title);
            Book book = bookRepository.findBySlug(slug);
            if (book != null) {
                continue;
            }
            book = Book.builder()
                    .title(title)
                    .isbn(isbn)
                    .pageCount(pageCount)
                    .publishedDate(publishedDate)
                    .thumbnailUrl(thumbnailUrl)
                    .shortDescription(shortDescription)
                    .description(longDescription)
                    .slug(slug)
                    .price(price)
                    .availableQuantity(defaultQuantity)
                    .authors(finalAuthors)
                    .genres(finalGenres)
                    .build();
            bookRepository.save(book);
        }
    }

    public void migrateUsersData() {
        userService.createTestingData();
    }
}
