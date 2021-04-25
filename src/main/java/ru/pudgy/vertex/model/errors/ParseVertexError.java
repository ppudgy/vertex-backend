package ru.pudgy.vertex.model.errors;

public sealed interface ParseVertexError extends VertexError
    permits ParseJsonVertexError
{
}
