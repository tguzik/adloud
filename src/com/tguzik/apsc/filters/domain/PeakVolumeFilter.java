package com.tguzik.apsc.filters.domain;

import com.tguzik.apsc.alerts.AlertLogic;
import com.tguzik.apsc.filters.AudioFilter;

public class PeakVolumeFilter extends AudioFilter {
    private final AlertLogic logic;

    public PeakVolumeFilter( AudioFilter nextInChain, AlertLogic logic ) {
        super( nextInChain );
        this.logic = logic;
    }

    @Override
    protected double[] processInner( double[] buffer ) {
        double value, total = 0;

        for ( int i = 0; i < buffer.length; i++ ) {
            total += buffer[i];
        }

        value = -0.691 + 10 * Math.log10( total / buffer.length );
        logic.apply( value );

        return buffer;
    }
}
