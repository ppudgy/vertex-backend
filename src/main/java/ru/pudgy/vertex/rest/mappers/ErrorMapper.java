package ru.pudgy.vertex.rest.mappers;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MutableHttpResponse;
import ru.pudgy.vertex.model.errors.EntityNotFoundVertexError;
import ru.pudgy.vertex.model.errors.NotAuthenticatedVertexError;
import ru.pudgy.vertex.model.errors.VertexError;
import ru.pudgy.vertex.rest.dto.error.AuthenticationErrorDto;
import ru.pudgy.vertex.rest.dto.error.EntityNotFoundErrorDto;

public interface ErrorMapper{
    static <T> MutableHttpResponse<T> toHttpResponse(VertexError error) {
        //return switch(error) {
            // case NotAuthenticatedVertexError -> HttpResponse.status(HttpStatus.UNAUTHORIZED);
            if(error instanceof NotAuthenticatedVertexError n) {
                return HttpResponse.status(HttpStatus.UNAUTHORIZED).body((T)AuthenticationErrorDto.of());
            // case EntityNotFoundVertexError -> HttpResponse.notFound();
            } else if (error instanceof EntityNotFoundVertexError n) {
                return HttpResponse.notFound().body((T)EntityNotFoundErrorDto.of(n.id(), n.clazz().getName()));
            }
            // case _ -> HttpResponse.badRequest();
            return HttpResponse.badRequest();
    }
}
