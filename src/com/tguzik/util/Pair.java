package com.tguzik.util;

import java.util.Objects;

public class Pair<K, V> {
    private final K first;
    private final V second;

    public Pair( K first, V second ) {
        this.first = first;
        this.second = second;
    }

    public K getFirst() {
        return first;
    }

    public V getSecond() {
        return second;
    }

    @Override
    public int hashCode() {
        return Objects.hash( first, second );
    }

    @Override
    public boolean equals( Object obj ) {
        if ( obj instanceof Pair ) {
            Pair<?, ?> other = (Pair<?, ?>) obj;

            return Objects.equals( first, other.first ) && //
                   Objects.equals( second, other.second );
        }
        return false;
    }

    @Override
    public String toString() {
        return "[" + first + ", " + second + "]";
    }
}
