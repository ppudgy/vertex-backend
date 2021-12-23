package ru.pudgy.vertex.model.entity;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.data.annotation.DateCreated;


import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.Instant;

@Data
@Entity
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NonNull
    private Long id;

    @NonNull
    @NotBlank
    private String username;

    @NonNull
    @NotBlank
    private String refreshToken;

    @NonNull
    @NotNull
    private Boolean revoked;

    @DateCreated
    @NonNull
    @NotNull
    private Instant dateCreated;

}
