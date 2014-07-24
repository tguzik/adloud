package com.tguzik.apsc.gui;

import com.tguzik.apsc.alerts.Alert;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class DataFormatter {
    private static DateTimeFormatter FORMATTER = DateTimeFormat.forPattern( "YYYY-MM-dd HH:ss" );

    public static String formatPeakValue( double value ) {
        return String.format( "%2.2f LKFS", value );
    }

    public static int prepareIntegerPeakValue( double value ) {
        value = Math.abs( value );
        if ( value < 0.001 ) {
            return 0;
        }
        if ( value > 70 ) {
            return 100;
        }
        return (int) (value / 0.70);
    }

    public static String formatLong( long value ) {
        return String.valueOf( value );
    }

    public static Object[] prepareAlertRow( Alert alert ) {
        String timestamp = FORMATTER.print( alert.getTimestamp() );
        String value = String.format( "%2.2f LKFS", alert.getValue() );

        return new Object[] { timestamp, value };
    }
}
