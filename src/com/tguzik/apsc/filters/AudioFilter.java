package com.tguzik.apsc.filters;

import com.tguzik.util.cor.AbstractChainElement;

public abstract class AudioFilter extends AbstractChainElement<double[]> {
    public AudioFilter( AudioFilter nextInChain ) {
        super( nextInChain );
    }
}
