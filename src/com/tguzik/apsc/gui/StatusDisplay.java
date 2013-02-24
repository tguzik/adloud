package com.tguzik.apsc.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import com.tguzik.apsc.StatusIntermediary;
import com.tguzik.util.annotations.Shutdownable;

/**
 * Here be dragons...
 * 
 * ...but seriously, creating GUIs in any language sucks.
 */
public class StatusDisplay extends JFrame implements Shutdownable {
    private static final long serialVersionUID = 1L;
    private final ScheduledExecutorService executor;

    private final JPanel contentPanel = new JPanel();
    private final StatusIntermediary statusIntermediary;
    private final JTextField fldCurrentPeakValue;
    private final JTextField fldFramesPerSecond;
    private final JTextField fldWindowAvereage;
    private final JProgressBar pbCurrentPeakValue;
    private final VolumeGraphComponent volumeGraph;
    private final AlertsTableModel lstAlertsModel;
    private final JScrollPane lstAlertsScrollPane;

    public StatusDisplay( StatusIntermediary statusIntermediary ) {
        this.statusIntermediary = statusIntermediary;
        this.pbCurrentPeakValue = new JProgressBar();
        this.fldCurrentPeakValue = new JTextField();
        this.fldFramesPerSecond = new JTextField();
        this.fldWindowAvereage = new JTextField();
        this.lstAlertsScrollPane = new JScrollPane();
        this.lstAlertsModel = new AlertsTableModel();
        this.volumeGraph = new VolumeGraphComponent( statusIntermediary, 275, 90 );
        this.executor = Executors.newSingleThreadScheduledExecutor();
    }

    @Override
    public void initialize() throws Exception {
        createWindow();
        setUpExecutorTask();
    }

    @Override
    public void shutdown() throws Exception {
        executor.shutdownNow();
        dispose();
    }

    private void setUpExecutorTask() {
        ValueUpdateTask vut = new ValueUpdateTask( statusIntermediary,
                                                   fldFramesPerSecond,
                                                   fldCurrentPeakValue,
                                                   fldWindowAvereage,
                                                   pbCurrentPeakValue,
                                                   lstAlertsModel,
                                                   volumeGraph );
        executor.scheduleAtFixedRate( vut, 500, 100, TimeUnit.MILLISECONDS );
    }

    private void createWindow() {
        setTitle( "APSC - Reklama" );
        setResizable( false );
        setBounds( 100, 100, 300, 500 );
        getContentPane().setLayout( null );
        contentPanel.setBounds( 0, 0, 294, 470 );
        contentPanel.setBorder( new EmptyBorder( 5, 5, 5, 5 ) );
        getContentPane().add( contentPanel );
        contentPanel.setLayout( null );

        JLabel lblPrzetworzonychRameks = new JLabel( "Approximate processed frames/s:" );
        lblPrzetworzonychRameks.setBounds( 10, 11, 179, 14 );
        lblPrzetworzonychRameks.setFont( new Font( "Tahoma", Font.PLAIN, 11 ) );
        contentPanel.add( lblPrzetworzonychRameks );
        fldFramesPerSecond.setHorizontalAlignment( SwingConstants.CENTER );
        fldFramesPerSecond.setBounds( 200, 8, 85, 20 );
        fldFramesPerSecond.setColumns( 10 );
        fldFramesPerSecond.setEditable( false );
        contentPanel.add( fldFramesPerSecond );

        JLabel lblCurrentPeakValue = new JLabel( "Current peak value:" );
        lblCurrentPeakValue.setBounds( 10, 39, 179, 14 );
        lblCurrentPeakValue.setFont( new Font( "Tahoma", Font.PLAIN, 11 ) );
        contentPanel.add( lblCurrentPeakValue );
        fldCurrentPeakValue.setHorizontalAlignment( SwingConstants.CENTER );
        fldCurrentPeakValue.setEditable( false );
        fldCurrentPeakValue.setBounds( 200, 36, 85, 20 );
        fldCurrentPeakValue.setColumns( 10 );
        contentPanel.add( fldCurrentPeakValue );

        JLabel lblSredniaOkna = new JLabel( "Time window average:" );
        lblSredniaOkna.setBounds( 10, 65, 180, 15 );
        lblSredniaOkna.setFont( new Font( "Tahoma", Font.PLAIN, 11 ) );
        contentPanel.add( lblSredniaOkna );

        fldWindowAvereage.setEditable( false );
        fldWindowAvereage.setHorizontalAlignment( SwingConstants.CENTER );
        fldWindowAvereage.setBounds( 200, 60, 86, 20 );
        fldWindowAvereage.setColumns( 10 );
        contentPanel.add( fldWindowAvereage );

        pbCurrentPeakValue.setStringPainted( true );
        pbCurrentPeakValue.setMinimum( 0 );
        pbCurrentPeakValue.setMaximum( 100 );
        pbCurrentPeakValue.setBounds( 10, 190, 275, 20 );
        contentPanel.add( pbCurrentPeakValue );

        JLabel lblWartoProgowa = new JLabel( "Threshold value:" );
        lblWartoProgowa.setBounds( 10, 224, 136, 14 );
        contentPanel.add( lblWartoProgowa );

        NumberFormat format = NumberFormat.getInstance();
        format.setMaximumIntegerDigits( 2 );
        format.setMinimumIntegerDigits( 2 );
        format.setMaximumFractionDigits( 2 );
        format.setMinimumFractionDigits( 2 );
        JFormattedTextField fldProg = new JFormattedTextField( format );
        fldProg.setHorizontalAlignment( SwingConstants.CENTER );
        fldProg.setText( "5.00" );
        fldProg.setBounds( 200, 221, 85, 20 );
        fldProg.addPropertyChangeListener( "value", new CustomThresholdChangeListener( statusIntermediary, fldProg ) );
        contentPanel.add( fldProg );

        JTable lstAlerts = new JTable();
        lstAlerts.setForeground( SystemColor.textText );
        lstAlertsScrollPane.setHorizontalScrollBarPolicy( ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER );
        lstAlertsScrollPane.setBounds( 10, 249, 275, 180 );
        lstAlertsScrollPane.setViewportView( lstAlerts );
        lstAlertsScrollPane.setAutoscrolls( true );
        contentPanel.add( lstAlertsScrollPane );
        lstAlerts.setFillsViewportHeight( true );
        lstAlerts.setModel( lstAlertsModel );
        lstAlerts.getColumnModel().getColumn( 0 ).setPreferredWidth( 150 );
        lstAlerts.getColumnModel().getColumn( 1 ).setPreferredWidth( 150 );
        lstAlerts.setSelectionMode( ListSelectionModel.MULTIPLE_INTERVAL_SELECTION );

        JButton btnWyjscie = new JButton( "Exit" );
        btnWyjscie.setBounds( 195, 440, 89, 23 );
        contentPanel.add( btnWyjscie );

        volumeGraph.setBounds( 10, 89, 275, 90 );
        volumeGraph.setForeground( Color.GREEN );
        volumeGraph.setBackground( Color.BLACK );
        volumeGraph.setVisible( true );
        contentPanel.add( volumeGraph );

        btnWyjscie.addActionListener( new ActionListener() {
            public void actionPerformed( ActionEvent e ) {
                statusIntermediary.shutdownApplication();
            }
        } );

        setDefaultCloseOperation( JDialog.DO_NOTHING_ON_CLOSE );
        setVisible( true );
    }
}
