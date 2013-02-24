package com.tguzik.apsc.filters.domain;

import com.tguzik.apsc.filters.AudioFilter;
import com.tguzik.apsc.filters.basic.FrequencyFilter;

public class PreAmpFilter extends FrequencyFilter {
    private static double a[] = { Double.NaN, -1.69065929318241, 0.73248077421585 };
    private static double b[] = { 1.53512485958697, -2.69169618940638, 1.19839281085285 };

    public PreAmpFilter( AudioFilter nextInChain ) {
        super( nextInChain, a, b );
    }
}
