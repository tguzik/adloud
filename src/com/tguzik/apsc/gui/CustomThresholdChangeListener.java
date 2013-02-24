package com.tguzik.apsc.gui;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JFormattedTextField;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tguzik.apsc.StatusIntermediary;

class CustomThresholdChangeListener implements PropertyChangeListener {
    private static final Logger LOGGER = LoggerFactory.getLogger( CustomThresholdChangeListener.class );
    private final JFormattedTextField field;
    private final StatusIntermediary si;

    CustomThresholdChangeListener( StatusIntermediary si, JFormattedTextField field ) {
        this.field = field;
        this.si = si;
    }

    @Override
    public void propertyChange( PropertyChangeEvent evt ) {
        double v = ( (Number) field.getValue() ).doubleValue();
        si.setThreshold( v );

        LOGGER.info( String.format( "Setting threshold at %d LKFS", v ) );
    }
}
