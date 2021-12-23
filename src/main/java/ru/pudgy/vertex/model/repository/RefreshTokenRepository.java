package ru.pudgy.vertex.model.repository;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;
import ru.pudgy.vertex.model.entity.RefreshToken;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends CrudRepository<RefreshToken, Long> {
    RefreshToken save(@NonNull @NotBlank String username,
                      @NonNull @NotBlank String refreshToken,
                      @NonNull @NotNull Boolean revoked,
                      @NonNull @NotNull Instant dateCreated);

    Optional<RefreshToken> findByRefreshToken(@NonNull @NotBlank String refreshToken);

    long updateByUsername(@NonNull @NotBlank String username,
                          boolean revoked);
}
