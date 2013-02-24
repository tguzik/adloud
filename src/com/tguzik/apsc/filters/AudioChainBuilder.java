package com.tguzik.apsc.filters;

import javax.sound.sampled.AudioFormat;

import com.tguzik.apsc.StatusIntermediary;
import com.tguzik.apsc.alerts.AlertLogic;
import com.tguzik.apsc.filters.basic.MeanSquareRootFilter;
import com.tguzik.apsc.filters.domain.PeakVolumeFilter;
import com.tguzik.apsc.filters.domain.PreAmpFilter;
import com.tguzik.apsc.filters.domain.PreSumGatingFilter;
import com.tguzik.apsc.filters.domain.RLBFilter;
import com.tguzik.apsc.filters.technical.RetrospectFilter;
import com.tguzik.apsc.filters.technical.StatisticCounterFilter;

public class AudioChainBuilder {

    public static AudioFilter build( AudioFormat format, StatusIntermediary statusIntermediary, int audioBufferSize ) {
        AlertLogic logic = new AlertLogic( statusIntermediary, 20000 );
        AudioFilter chain = null;

        // From last to first
        chain = new PeakVolumeFilter( chain, logic );
        chain = new RetrospectFilter( chain, format, audioBufferSize, 4 );
        chain = new PreSumGatingFilter( chain );
        chain = new MeanSquareRootFilter( chain );
        chain = new RLBFilter( chain );
        chain = new PreAmpFilter( chain );
        chain = new StatisticCounterFilter( chain, statusIntermediary );

        return chain;
    }

}
