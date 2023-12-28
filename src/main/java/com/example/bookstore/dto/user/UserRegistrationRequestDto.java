package com.example.bookstore.dto.user;

import com.example.bookstore.validation.FieldMatch;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
@FieldMatch(first = "password", second = "repeatPassword")
public class UserRegistrationRequestDto {
    @NotEmpty
    @Length(max = 255)
    private String email;
    @NotEmpty
    @Length(min = 8, max = 255)
    private String password;
    @NotEmpty
    @Length(min = 8, max = 255)
    private String repeatPassword;
    @NotEmpty
    @Length(max = 255)
    private String firstName;
    @NotEmpty
    @Length(max = 255)
    private String lastName;
    @NotNull
    @Length(max = 255)
    private String shippingAddress;
}
