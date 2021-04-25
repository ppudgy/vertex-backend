package ru.pudgy.vertex.model.errors;

public final record RuntimeVertexError(String reason) implements VertexError {
}
