package com.tguzik.apsc.filters.domain;

import com.tguzik.apsc.filters.AudioFilter;
import com.tguzik.apsc.filters.basic.NullFilter;

public class PreSumGatingFilter extends NullFilter {
    public PreSumGatingFilter( AudioFilter nextInChain ) {
        super( nextInChain );
    }
}
