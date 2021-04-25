package ru.pudgy.vertex.model.errors;

public final record NotAuthenticatedVertexError(String msg) implements VertexError {
}
