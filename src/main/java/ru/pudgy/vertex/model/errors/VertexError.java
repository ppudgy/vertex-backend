package ru.pudgy.vertex.model.errors;

public sealed interface VertexError
    permits NotFoundVertexError,
        NotAuthenticatedVertexError,
        ParseVertexError,
        SqlVertexError,
        RuntimeVertexError
{
}
