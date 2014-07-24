package com.tguzik.apsc.gui;

import javax.swing.table.DefaultTableModel;

public class AlertsTableModel extends DefaultTableModel {
    private static final long serialVersionUID = 1L;

    public AlertsTableModel() {
        super( new Object[][] { }, new String[] { "Date and time", "Peak value" } );
    }

    public Class<?> getColumnClass( int columnIndex ) {
        return String.class;
    }

    public boolean isCellEditable( int row, int column ) {
        return false;
    }
}
