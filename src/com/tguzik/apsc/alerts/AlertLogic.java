package com.tguzik.apsc.alerts;

import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;

import com.tguzik.apsc.StatusIntermediary;
import com.tguzik.util.Pair;

public class AlertLogic {
    private final Deque<Pair<Long, Double>> valuesInTimeWindow;
    private final StatusIntermediary intermediary;
    private final long windowSizeMilis;
    private long lastAlertTimestamp;

    public AlertLogic( StatusIntermediary intermediary, long thresholdMiliseconds ) {
        this.valuesInTimeWindow = new LinkedList<>();
        this.windowSizeMilis = thresholdMiliseconds;
        this.intermediary = intermediary;
    }

    public void apply( double value ) {
        long timestamp = System.currentTimeMillis();
        double avg;

        dropValuesOutOfWindow( timestamp );
        addCurrentValue( timestamp, value );
        avg = calculateAverage();

        if ( isAboveThreshold( avg, value ) && //
             isOutsideTimeWindow( timestamp, lastAlertTimestamp, 5000 ) ) {
            intermediary.getAlerts().add( Alert.create( value ) );
            lastAlertTimestamp = timestamp;
        }

        intermediary.setCurrentPeakValue( value );
        intermediary.setTimeWindowAvereage( avg );
    }

    private boolean isOutsideTimeWindow( long now, long item, long windowSize ) {
        return (now - item) > windowSize;
    }

    private boolean isAboveThreshold( double avg, double value ) {
        return (value - avg) > intermediary.getThreshold();
    }

    private double calculateAverage() {
        Iterator<Pair<Long, Double>> it = valuesInTimeWindow.iterator();
        int elements = 0;
        double sum = 0;

        while ( it.hasNext() ) {
            sum += it.next().getSecond();
            elements++;
        }

        if ( elements != 0 ) {
            return sum / elements;
        }
        return 0;
    }

    private void addCurrentValue( long timestamp, double value ) {
        valuesInTimeWindow.add( new Pair<>( timestamp, value ) );
    }

    private void dropValuesOutOfWindow( long timestamp ) {
        Iterator<Pair<Long, Double>> it = valuesInTimeWindow.iterator();

        while ( it.hasNext() ) {
            if ( isOutsideTimeWindow( timestamp, it.next().getFirst(), windowSizeMilis ) ) {
                it.remove();
            }
        }
    }
}
