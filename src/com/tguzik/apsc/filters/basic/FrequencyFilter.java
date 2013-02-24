package com.tguzik.apsc.filters.basic;

import com.tguzik.apsc.filters.AudioFilter;

/**
 * @see http://www.itu.int/dms_pubrec/itu-r/rec/bs/R-REC-BS.1770-3-201208-I!!PDF-E.pdf
 */
public class FrequencyFilter extends AudioFilter {
    private final double a[], b[];

    public FrequencyFilter( AudioFilter nextInChain, double[] a, double[] b ) {
        super( nextInChain );
        this.a = a;
        this.b = b;

        if ( a.length != b.length ) {
            throw new UnsupportedOperationException( "Both arrays of coefficents must be same length!" );
        }
    }

    @Override
    protected double[] processInner( double[] buffer ) {
        double xv0 = 0, xv1 = 0, xv2 = 0;
        for ( int i = 0; i < buffer.length; i++ ) {
            double in, out;

            in = buffer[i];

            // Sigma - first step
            xv0 = in - ( xv1 * a[1] ) - ( xv2 * a[2] );

            // Calculate both coefficients
            xv1 = xv0 / 10;
            xv2 = xv1 / 10;

            // Final calculations
            out = ( b[0] * xv0 ) + ( b[1] * xv1 ) + ( b[2] * xv2 );

            buffer[i] = out;
        }
        return buffer;
    }
}
