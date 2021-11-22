package com.umar.apps.util;

import java.util.function.Consumer;

public interface ThrowingConsumer<T, E extends Throwable> {

    void accept(T t) throws E;

    static <T, E extends Throwable>Consumer<T> unchecked(ThrowingConsumer<T, E> consumer) {
        return (t) -> {
            try {
                consumer.accept(t);
            }catch (Throwable throwable) {
                throw new RuntimeException(throwable);
            }
        };
    }
}
