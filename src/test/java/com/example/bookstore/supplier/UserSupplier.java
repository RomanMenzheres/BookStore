package com.example.bookstore.supplier;

import com.example.bookstore.model.User;

public class UserSupplier {

    public static User getUser() {
        User user = new User();
        user.setId(1L);
        user.setEmail("bob@gmail.com");
        user.setPassword("123456");
        user.setFirstName("Bob");
        user.setLastName("Bobckovich");
        user.setShippingAddress("123 Main St, City, Country");
        return user;
    }
}
