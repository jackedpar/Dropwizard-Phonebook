package com.dwbook.phonebook.representations;

import io.dropwizard.jackson.JsonSnakeCase;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

@JsonSnakeCase
public class User {

    @NotBlank
    @Length(min = 2, max = 255)
    private final String username;

    @NotBlank
    @Length(min = 2, max = 255)
    private final String password;


    public User() {
        this.username = null;
        this.password = null;
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

}
