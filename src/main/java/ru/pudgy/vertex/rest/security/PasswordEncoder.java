package ru.pudgy.vertex.rest.security;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public interface PasswordEncoder {
    String encode(@NotBlank @NotNull String rawPassword);
    boolean matches(@NotBlank @NotNull String rawPassword, @NotBlank @NotNull String encodedPassword);
}