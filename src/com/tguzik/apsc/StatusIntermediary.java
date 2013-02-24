package com.tguzik.apsc;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicLong;

import com.tguzik.apsc.alerts.Alert;

/**
 * This class acts as an proxy between AudioProcessor (and its filtering chain),
 * which has its own thread, and the GUI, which is on another thread.
 */
public class StatusIntermediary {
    private final ConcurrentLinkedQueue<Alert> alerts;
    private final AtomicLong framesProcessed;
    private final Reklama reklama;

    private volatile double timeWindowAvereage;
    private volatile double currentPeakValue;
    private volatile double threshold;

    public StatusIntermediary( Reklama reklama ) {
        this.alerts = new ConcurrentLinkedQueue<>();
        this.framesProcessed = new AtomicLong( 0 );
        this.reklama = reklama;

        timeWindowAvereage = Double.NaN;
        currentPeakValue = Double.NaN;
        threshold = 2;
    }

    public Queue<Alert> getAlerts() {
        return alerts;
    }

    public double getCurrentPeakValue() {
        return currentPeakValue;
    }

    public void setCurrentPeakValue( double d ) {
        this.currentPeakValue = d;
    }

    public AtomicLong getFramesProcessed() {
        return framesProcessed;
    }

    public double getThreshold() {
        return threshold;
    }

    public void setThreshold( double threshold ) {
        this.threshold = threshold;
    }

    public void setTimeWindowAvereage( double avg ) {
        this.timeWindowAvereage = avg;
    }

    public double getTimeWindowAvereage() {
        return timeWindowAvereage;
    }

    public void shutdownApplication() {
        try {
            reklama.shutdown();
        }
        catch ( Exception e ) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
