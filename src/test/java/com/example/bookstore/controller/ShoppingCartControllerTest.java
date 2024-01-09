package com.example.bookstore.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.bookstore.dto.cart.CartItemDto;
import com.example.bookstore.dto.cart.CreateCartItemRequestDto;
import com.example.bookstore.dto.cart.ShoppingCartDto;
import com.example.bookstore.supplier.CartItemSupplier;
import com.example.bookstore.supplier.ShoppingCartSupplier;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.Connection;
import java.sql.SQLException;
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
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ShoppingCartControllerTest {
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
                    new ClassPathResource("database/user/add-user.sql")
            );
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
                    new ClassPathResource("database/user/delete-user.sql")
            );
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/book/delete-all-books.sql")
            );
        }
    }

    @WithUserDetails(value = "bob@gmail.com",
            userDetailsServiceBeanName = "customUserDetailsService")
    @Test
    @DisplayName("Get user's shopping cart test")
    void getShoppingCart_ValidUser_Success() throws Exception {
        ShoppingCartDto expected = ShoppingCartSupplier.getShoppingCartDto();

        MvcResult result = mockMvc.perform(
                get("/api/cart")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        ShoppingCartDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), ShoppingCartDto.class
        );

        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @WithUserDetails(value = "bob@gmail.com",
            userDetailsServiceBeanName = "customUserDetailsService")
    @Test
    @Sql(
            scripts = "classpath:database/cart/delete-created-cart-item.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @DisplayName("Add a new cart item to shopping cart with valid request dto test")
    void addCartItem_ValidRequestDto_Success() throws Exception {
        CreateCartItemRequestDto requestDto = CartItemSupplier.getCreateCartItemRequestDto();

        CartItemDto expected =
                new CartItemDto(1L, 1L, "Harry Potter and the Prisoner of Azkaban", 1);

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        MvcResult result = mockMvc.perform(
                post("/api/cart")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        CartItemDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), CartItemDto.class
        );

        assertNotNull(actual);
        assertNotNull(actual.id());
        EqualsBuilder.reflectionEquals(expected, actual, "id");
    }

    @WithUserDetails(value = "bob@gmail.com",
            userDetailsServiceBeanName = "customUserDetailsService")
    @Test
    @Sql(
            scripts = "classpath:database/cart/create-one-cart-item.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @DisplayName("Add a new cart item to shopping cart with valid request dto test")
    void deleteCartItem_ValidRequestDto_Success() throws Exception {
        mockMvc.perform(
                delete("/api/cart/cart-items/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andReturn();
    }
}
