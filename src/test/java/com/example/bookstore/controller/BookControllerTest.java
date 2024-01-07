package com.example.bookstore.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.bookstore.dto.book.BookDto;
import com.example.bookstore.dto.book.CreateBookRequestDto;
import com.example.bookstore.dto.category.CategoryDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import javax.sql.DataSource;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookControllerTest {
    protected static MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(
            @Autowired DataSource dataSource,
            @Autowired WebApplicationContext applicationContext
    ) throws SQLException {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
        teardown(dataSource);
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/book/add-three-default-books.sql")
            );
        }
    }

    @AfterAll
    static void afterAll(@Autowired DataSource dataSource) {
        teardown(dataSource);
    }

    @SneakyThrows
    static void teardown(DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/book/delete-all-books.sql")
            );
        }
    }

    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    @Test
    @Sql(
            scripts = "classpath:database/book/delete-created-book.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @DisplayName("Create a new book with valid request dto test")
    void createBook_ValidRequestDto_Success() throws Exception {
        CreateBookRequestDto requestDto = new CreateBookRequestDto()
                .setTitle("Title")
                .setAuthor("Author")
                .setIsbn("978-3-16-148410-0")
                .setPrice(BigDecimal.valueOf(19.99))
                .setDescription("description")
                .setCoverImage("https://example.com/updated-cover-image.jpg")
                .setCategoryIds(Set.of(1L, 3L));

        BookDto expected = new BookDto();
        expected.setId(1L);
        expected.setTitle(requestDto.getTitle());
        expected.setAuthor(requestDto.getAuthor());
        expected.setIsbn(requestDto.getIsbn());
        expected.setPrice(requestDto.getPrice());
        expected.setDescription(requestDto.getDescription());
        expected.setCoverImage(requestDto.getCoverImage());
        expected.setCategoryIds(requestDto.getCategoryIds());

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        MvcResult result = mockMvc.perform(
                post("/api/books")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andReturn();

        BookDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), BookDto.class
        );

        assertNotNull(actual);
        assertNotNull(actual.getId());
        EqualsBuilder.reflectionEquals(expected, actual, "id");
    }

    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    @Test
    @DisplayName("Create a new book with invalid request dto test")
    void createBook_InvalidRequestDto_BadRequest() throws Exception {
        CreateBookRequestDto requestDto = new CreateBookRequestDto()
                .setTitle("")
                .setAuthor("")
                .setIsbn("978-3-16-148410-0")
                .setPrice(BigDecimal.valueOf(19.99))
                .setDescription("")
                .setCoverImage("");

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        mockMvc.perform(
                        post("/api/books")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    @Test
    @DisplayName("Get All books test")
    void getAll_GivenBooks_Success() throws Exception {
        List<BookDto> expected = new ArrayList<>();
        expected.add(new BookDto()
                .setId(1L)
                .setTitle("Harry Potter and the Prisoner of Azkaban")
                .setAuthor("J.K. Rowling")
                .setIsbn("978-3-16-148411-0")
                .setPrice(BigDecimal.valueOf(29.99))
                .setDescription("Fantasy book")
                .setCoverImage("https://example.com/updated-cover-image.jpg")
        );
        expected.add(new BookDto()
                .setId(2L)
                .setTitle("History Year by Year")
                .setAuthor("Dorling Kindersley")
                .setIsbn("978-3-16-148412-0")
                .setPrice(BigDecimal.valueOf(19.99))
                .setDescription("Historical book")
                .setCoverImage("https://example.com/updated-cover-image.jpg")
        );
        expected.add(new BookDto()
                .setId(3L)
                .setTitle("The Silent Patient")
                .setAuthor("Alex Michaelides")
                .setIsbn("978-3-16-148413-0")
                .setPrice(BigDecimal.valueOf(21.99))
                .setDescription("Mystery book")
                .setCoverImage("https://example.com/updated-cover-image.jpg")
        );

        MvcResult result = mockMvc.perform(
                get("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        BookDto[] actual = objectMapper.readValue(
                result.getResponse().getContentAsByteArray(), BookDto[].class);

        assertEquals(expected.size(), actual.length);
        assertEquals(expected, Arrays.stream(actual).toList());
    }

    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    @Test
    @DisplayName("Get book by valid id test")
    void getBookById_ValidId_Success() throws Exception {
        BookDto expected = new BookDto()
                .setId(1L)
                .setTitle("Harry Potter and the Prisoner of Azkaban")
                .setAuthor("J.K. Rowling")
                .setIsbn("978-3-16-148410-0")
                .setPrice(BigDecimal.valueOf(29.99))
                .setDescription("Fantasy book")
                .setCoverImage("https://example.com/updated-cover-image.jpg");

        MvcResult result = mockMvc.perform(
                get("/api/books/1")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        CategoryDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), CategoryDto.class);

        assertNotNull(actual);
        EqualsBuilder.reflectionEquals(expected, actual);
    }

    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    @Test
    @DisplayName("Get book by invalid id test")
    void getBookById_NotExistsId_NotFound() throws Exception {
        mockMvc.perform(
                        get("/api/categories/-1")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound())
                .andReturn();
    }
}
