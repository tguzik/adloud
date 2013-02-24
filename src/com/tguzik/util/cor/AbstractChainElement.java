package com.tguzik.util.cor;

public abstract class AbstractChainElement<T> implements ChainOfResponsibility<T> {
    private final AbstractChainElement<T> nextInChain;

    public AbstractChainElement( final AbstractChainElement<T> nextInChain ) {
        this.nextInChain = nextInChain;
    }

    final public T process( T value ) {
        final T internalValue = processInner( value );
        if ( nextInChain != null ) {
            return nextInChain.process( internalValue );
        }
        return internalValue;
    }

    abstract protected T processInner( T value );
}
