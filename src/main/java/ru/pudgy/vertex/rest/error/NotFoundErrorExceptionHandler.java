package ru.pudgy.vertex.rest.error;

import io.micronaut.context.annotation.Requires;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.server.exceptions.ExceptionHandler;
import ru.pudgy.vertex.exceptions.NotFoundException;
import ru.pudgy.vertex.rest.dto.ErrorDto;

import javax.inject.Singleton;

/**
 * Not found exception to http error code converter
 * TODO create handlers for other exceptions
 */
@Produces
@Singleton
@Requires(classes = {NotFoundException.class, ExceptionHandler.class})
public class NotFoundErrorExceptionHandler implements ExceptionHandler<NotFoundException, HttpResponse> {

    @Override
    public HttpResponse handle(HttpRequest request, NotFoundException exception) {
        return HttpResponse.notFound().body(ErrorDto.of(exception.getMessage()));
    }
}