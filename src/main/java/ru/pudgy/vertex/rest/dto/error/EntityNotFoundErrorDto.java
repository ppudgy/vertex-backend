package ru.pudgy.vertex.rest.dto.error;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Data
@RequiredArgsConstructor(staticName = "of")
public class EntityNotFoundErrorDto {
    private final UUID id;
    private final String name;
}
