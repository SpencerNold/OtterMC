package me.spencernold.transformer;

public interface Adapter<T, E> {
    E adapt(T t);
}
