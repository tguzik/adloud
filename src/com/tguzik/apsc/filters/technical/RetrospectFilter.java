package com.tguzik.apsc.filters.technical;

import javax.sound.sampled.AudioFormat;

import com.tguzik.apsc.filters.AudioFilter;

public class RetrospectFilter extends AudioFilter {
    private final double[] temporary;
    private final double[] buffer;

    private final int bufferBaseSize;

    public RetrospectFilter( AudioFilter nextInChain, AudioFormat format, int inputAudioBufferSizeSamples, int multipler ) {
        super( nextInChain );
        this.bufferBaseSize = inputAudioBufferSizeSamples;
        this.temporary = createBuffer( this.bufferBaseSize, multipler - 1 );
        this.buffer = createBuffer( this.bufferBaseSize, multipler );
    }

    private double[] createBuffer( int size, int multipler ) {
        return new double[size * multipler];
    }

    @Override
    protected double[] processInner( double[] value ) {
        /* 1. Shift data: */
        /* 1.1. Copy to (n-1) arguments worth of data to temporary array */
        System.arraycopy( buffer, bufferBaseSize, temporary, 0, temporary.length );

        /* 1.2. Copy the data back to the buffer */
        System.arraycopy( temporary, 0, buffer, 0, temporary.length );

        /* 2. Copy new data */
        System.arraycopy( value, 0, buffer, temporary.length, bufferBaseSize );

        /* 3. Return the buffer */
        return buffer;
    }
}
