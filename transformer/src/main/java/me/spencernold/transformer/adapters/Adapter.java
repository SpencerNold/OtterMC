package me.spencernold.transformer.adapters;

public interface Adapter<T, E> {
    E adapt(T t);
}
