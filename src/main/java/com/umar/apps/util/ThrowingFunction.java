package com.umar.apps.util;

import java.util.function.Function;

public interface ThrowingFunction<T, R, E extends Throwable> {

    R apply(T t) throws E;

    static <T, R, E extends Throwable> Function<T, R> unchecked(ThrowingFunction<T, R, E> function) {
        return (t) -> {
            try {
                return function.apply(t);
            }catch (Throwable throwable) {
                throw new RuntimeException(throwable);
            }
        };
    }
}
