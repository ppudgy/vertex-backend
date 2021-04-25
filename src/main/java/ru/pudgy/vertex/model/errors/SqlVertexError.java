package ru.pudgy.vertex.model.errors;

public final record SqlVertexError(String reason, String SQLState, int vendorCode) implements VertexError {
}
