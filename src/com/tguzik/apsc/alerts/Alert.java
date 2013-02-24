package com.tguzik.apsc.alerts;

import org.joda.time.LocalDateTime;

public class Alert {
    private final LocalDateTime timestamp;
    private final double value;

    public Alert( LocalDateTime timestamp, double value ) {
        super();
        this.timestamp = timestamp;
        this.value = value;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public double getValue() {
        return value;
    }

    public static Alert create( LocalDateTime timestamp, double value ) {
        return new Alert( timestamp, value );
    }

    public static Alert create( double value ) {
        return new Alert( new LocalDateTime(), value );
    }
}
