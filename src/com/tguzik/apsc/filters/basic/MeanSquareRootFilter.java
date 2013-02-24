package com.tguzik.apsc.filters.basic;

import com.tguzik.apsc.filters.AudioFilter;

public class MeanSquareRootFilter extends AudioFilter {
    public MeanSquareRootFilter( AudioFilter next ) {
        super( next );
    }

    @Override
    /**
     * Calculate mean square root value over whole input and save the same value
     * to each field of the output. This method is ugly as sin, but fiddling 
     * around with Chain Of Responsibility that has different output and 
     * input arguments is even worse :(
     */
    protected double[] processInner( double[] input ) {
        double value = 0;

        // Odczyt
        for ( int i = 0; i < input.length; i++ ) {
            value += input[i] * input[i];
        }

        value = Math.sqrt( value / input.length );

        // Zapis
        for ( int i = 0; i < input.length; i++ ) {
            input[i] = value;
        }

        return input;
    }

}
