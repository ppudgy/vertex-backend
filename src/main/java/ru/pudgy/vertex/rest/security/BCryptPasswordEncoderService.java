package ru.pudgy.vertex.rest.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


public class BCryptPasswordEncoderService implements PasswordEncoder {
    org.springframework.security.crypto.password.PasswordEncoder delegate = new BCryptPasswordEncoder();
    @Override
    public String encode(@NotBlank @NotNull String rawPassword) {
        return delegate.encode(rawPassword);
    }

    @Override
    public boolean matches(@NotBlank @NotNull String rawPassword, @NotBlank @NotNull String encodedPassword) {
        return delegate.matches(rawPassword, encodedPassword);
    }
}
