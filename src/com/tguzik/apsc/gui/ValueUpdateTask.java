package com.tguzik.apsc.gui;

import javax.swing.*;

import com.tguzik.apsc.StatusIntermediary;
import com.tguzik.apsc.alerts.Alert;

public class ValueUpdateTask implements Runnable {
    private final StatusIntermediary statusIntermediary;
    private final JTextField framesPerSecondField;
    private final JTextField currentPeakValueField;
    private final JTextField timeWindowAverageField;
    private final JProgressBar currentPeakValueProgressBar;
    private final AlertsTableModel alertsTable;
    private final VolumeGraphComponent volumeGraph;
    private int refreshCounter;

    public ValueUpdateTask( StatusIntermediary statusIntermediary,
                            JTextField framesPerSecondField,
                            JTextField currentPeakValueField,
                            JTextField timeWindowAverageField,
                            JProgressBar currentPeakValueProgressBar,
                            AlertsTableModel alertsTable,
                            VolumeGraphComponent volumeGraph ) {
        this.statusIntermediary = statusIntermediary;
        this.framesPerSecondField = framesPerSecondField;
        this.currentPeakValueField = currentPeakValueField;
        this.timeWindowAverageField = timeWindowAverageField;
        this.currentPeakValueProgressBar = currentPeakValueProgressBar;
        this.alertsTable = alertsTable;
        this.volumeGraph = volumeGraph;
    }

    @Override
    public void run() {
        currentPeakValueField.setText( DataFormatter.formatPeakValue( statusIntermediary.getCurrentPeakValue() ) );
        timeWindowAverageField.setText( DataFormatter.formatPeakValue( statusIntermediary.getTimeWindowAvereage() ) );
        currentPeakValueProgressBar.setValue( DataFormatter.prepareIntegerPeakValue( statusIntermediary
                                                                                             .getCurrentPeakValue() ) );

        refreshCounter++;
        if ( refreshCounter == 10 ) {
            refreshCounter = 0;
            framesPerSecondField.setText( DataFormatter.formatLong( statusIntermediary.getFramesProcessed()
                                                                                      .getAndSet( 0 ) ) );
        }

        Alert alert = statusIntermediary.getAlerts().poll();
        if ( alert != null ) {
            alertsTable.addRow( DataFormatter.prepareAlertRow( alert ) );
        }

        volumeGraph.repaint();
    }
}
