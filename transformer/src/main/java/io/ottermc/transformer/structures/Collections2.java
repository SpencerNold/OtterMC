package io.ottermc.transformer.structures;

import java.util.Collection;
import java.util.function.Function;

public class Collections2 {

    public static <T, E> void transform(Collection<T> src, Collection<E> dst, Function<T, E> function) {
        dst.clear();
        src.forEach(t -> dst.add(function.apply(t)));
    }

    public static <T, F, E extends  Throwable> void transformUnsafe(Collection<T> src, Collection<F> dst, ThrowableFunction<T, F, E> function) throws E {
        dst.clear();
        for (T t : src)
            dst.add(function.apply(t));
    }

    public interface ThrowableFunction<T, F, E extends Throwable> {
        F apply(T e) throws E;
    }
}
