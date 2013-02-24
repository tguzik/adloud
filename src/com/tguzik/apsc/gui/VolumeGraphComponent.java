package com.tguzik.apsc.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.LinkedList;

import javax.swing.JPanel;

import com.tguzik.apsc.StatusIntermediary;
import com.tguzik.util.Pair;

public class VolumeGraphComponent extends JPanel {
    private static final long serialVersionUID = 1L;
    private final StatusIntermediary statusIntermediary;
    private final LinkedList<Pair<Integer, Integer>> graphValues;

    public VolumeGraphComponent( StatusIntermediary statusIntermediary, int width, int height ) {
        super();
        this.statusIntermediary = statusIntermediary;
        this.graphValues = new LinkedList<>();
    }

    public void paint( Graphics g ) {
        super.paint( g );

        Dimension d = this.getSize();
        g.setPaintMode();

        captureValues();

        for ( int i = 0; i < Math.min( d.width, graphValues.size() ); i++ ) {
            Pair<Integer, Integer> pair = graphValues.get( i );

            // Draw the value that is equal to average from last 20 seconds
            g.setColor( Color.GRAY );
            g.drawRect( d.width - ( i + 1 ), d.height - pair.getFirst(), 0, 0 );

            // Draw current value
            g.setColor( Color.GREEN );
            g.drawRect( d.width - ( i + 1 ), d.height - pair.getSecond(), 0, 0 );
        }
    }

    private void captureValues() {
        Dimension d = this.getSize();

        for ( int i = graphValues.size(); i >= d.width; i-- ) {
            graphValues.removeLast();
        }

        graphValues.addFirst( new Pair<>( getTimeWindowAvereage(), getCurrentValue() ) );
    }

    private int getCurrentValue() {
        if ( statusIntermediary != null ) {
            return getScaledValue( statusIntermediary.getCurrentPeakValue() );
        }
        return 0;
    }

    private int getTimeWindowAvereage() {
        if ( statusIntermediary != null ) {
            return getScaledValue( statusIntermediary.getTimeWindowAvereage() );
        }
        return 0;
    }

    private int getScaledValue( double in ) {
        int value = DataFormatter.prepareIntegerPeakValue( in );
        Dimension d = this.getSize();

        return (int) ( ( (double) value / 100.0 ) * d.height );
    }
}
