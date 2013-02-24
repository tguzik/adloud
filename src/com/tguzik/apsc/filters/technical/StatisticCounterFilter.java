package com.tguzik.apsc.filters.technical;

import com.tguzik.apsc.StatusIntermediary;
import com.tguzik.apsc.filters.AudioFilter;
import com.tguzik.apsc.filters.basic.NullFilter;

public class StatisticCounterFilter extends NullFilter {
    private final StatusIntermediary statusIntermediary;

    public StatisticCounterFilter( AudioFilter nextInChain, StatusIntermediary statusIntermediary ) {
        super( nextInChain );
        this.statusIntermediary = statusIntermediary;
    }

    @Override
    protected double[] processInner( double[] value ) {
        statusIntermediary.getFramesProcessed().addAndGet( value.length );
        return super.processInner( value );
    }
}
