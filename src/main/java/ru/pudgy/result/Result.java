package ru.pudgy.result;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

public sealed interface Result<R,E> permits Ok, Error {

    static <R, E> Result<R,E> ok(R result) {
        Objects.requireNonNull(result);
        return new Ok(result);
    }

    static <R, E> Result<R, E> error(E error) {
        Objects.requireNonNull(error);
        return new Error(error);
    }

    Optional<R> ok();

    Optional<E> error();

    <U> Result<U, E> map(Function<? super R, Result<U, E>> mapper);

    <G> Result<R,G> or(Function<? super E, Result<R, G>> mapper);

    R recover(Function<? super E, R> mapper);

    R unwrap();
    void expect(String msg);
}
