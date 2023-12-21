package com.example.bookstore.dto;

import com.example.bookstore.validation.FieldMatch;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

@FieldMatch(first = "password", second = "repeatPassword")
public class UserRegistrationRequestDto {
    @NotNull
    @NotEmpty
    @Length(max = 255)
    private String email;
    @NotNull
    @NotEmpty
    @Length(min = 8, max = 255)
    private String password;
    @NotNull
    @NotEmpty
    @Length(min = 8, max = 255)
    private String repeatPassword;
    @NotNull
    @NotEmpty
    @Length(max = 255)
    private String firstName;
    @NotNull
    @NotEmpty
    @Length(max = 255)
    private String lastName;
    @NotNull
    @Length(max = 255)
    private String shippingAddress;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRepeatPassword() {
        return repeatPassword;
    }

    public void setRepeatPassword(String repeatPassword) {
        this.repeatPassword = repeatPassword;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }
}
