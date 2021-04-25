package ru.pudgy.vertex.model.errors;

import java.util.UUID;

public final record EntityNotFoundVertexError<T>(Class<T> clazz, UUID id) implements NotFoundVertexError {
}
