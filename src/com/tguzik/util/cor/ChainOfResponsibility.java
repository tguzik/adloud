package com.tguzik.util.cor;

public interface ChainOfResponsibility<T> {
    public T process( T value );
}
