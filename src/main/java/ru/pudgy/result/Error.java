package ru.pudgy.result;

import java.util.Optional;
import java.util.function.Function;

final record Error<R, E>(E err) implements Result<R, E> {

    @Override
    public Optional<R> ok() {
        return Optional.empty();
    }

    @Override
    public Optional<E> error() {
        return Optional.of(err);
    }

    @Override
    public <U> Result<U, E> map(Function<? super R, Result<U, E>> mapper) {
        return Result.error(err);
    }

    @Override
    public <G> Result<R,G> or(Function<? super E, Result<R, G>> mapper) {
        return mapper.apply(err);
    }

    public R recover(Function<? super E, R> mapper) {
        return mapper.apply(err);
    }

    @Override
    public R unwrap() {
        throw new RuntimeException("Some error");
    }

    @Override
    public void expect(String msg) {
        throw new RuntimeException(msg);
    }
}
