package com.tguzik.apsc.filters.basic;

import com.tguzik.apsc.filters.AudioFilter;

public class NullFilter extends AudioFilter {
    public NullFilter( AudioFilter nextInChain ) {
        super( nextInChain );
    }

    @Override
    protected double[] processInner( double[] value ) {
        return value;
    }
}
