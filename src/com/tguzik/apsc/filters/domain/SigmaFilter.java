package com.tguzik.apsc.filters.domain;

import com.tguzik.apsc.filters.AudioFilter;
import com.tguzik.apsc.filters.basic.NullFilter;

/**
 * If we were measuring loudness on an multichannel audio, this filter
 * would sum all channels using their respective weights. Instead this
 * filter does not do anything and JVM should optimize it out.
 */
public class SigmaFilter extends NullFilter {
    public SigmaFilter( AudioFilter nextInChain ) {
        super( nextInChain );
    }
}
