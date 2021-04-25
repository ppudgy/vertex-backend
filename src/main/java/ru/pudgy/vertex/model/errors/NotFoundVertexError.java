package ru.pudgy.vertex.model.errors;

public sealed interface NotFoundVertexError extends VertexError
    permits EntityNotFoundVertexError
{
}
