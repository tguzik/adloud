package com.tguzik.apsc.filters.domain;

import com.tguzik.apsc.filters.AudioFilter;
import com.tguzik.apsc.filters.basic.FrequencyFilter;

public class RLBFilter extends FrequencyFilter {
    private static double a[] = { Double.NaN, -1.99004745483398, 0.99007225036621 };
    private static double b[] = { 1, -2, 1 };

    public RLBFilter( AudioFilter nextInChain ) {
        super( nextInChain, a, b );
    }
}
