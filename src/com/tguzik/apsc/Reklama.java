package com.tguzik.apsc;

import com.tguzik.apsc.gui.StatusDisplay;
import com.tguzik.apsc.processor.AudioProcessor;
import com.tguzik.util.annotations.Shutdownable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Luzne notki:
 * - Dziennik ustaw z algorytmem jest do rzyci. Znacznie czytelniejsze jest zalecenie
 * ITU: @see http://www.itu.int/dms_pubrec/itu-r/rec/bs/R-REC-BS.1770-3-201208-I!!PDF-E.pdf
 * - Sygnal wejsciowy MUSI byc samplowany na 48000 Hz
 *
 * @author Tomek
 */
public class Reklama implements Shutdownable {
    private static final Logger LOGGER = LoggerFactory.getLogger( Reklama.class );
    private final StatusIntermediary statusIntermediary;
    private final AudioProcessor audioProcessor;
    private final StatusDisplay statusDisplay;
    private final Thread apThread;

    public static void main( String[] args ) {
        Reklama reklama = new Reklama( true );

        try {
            reklama.initialize();
            reklama.run();
        }
        catch ( Exception e ) {
            LOGGER.error( "Caught unexpected exception", e );
        }
        finally {
            try {
                reklama.shutdown();
            }
            catch ( Exception e ) {
                LOGGER.error( "Caught unexpected exception", e );
            }
        }

    }

    private Reklama( boolean enableLoopback ) {
        this.statusIntermediary = new StatusIntermediary( this );
        this.audioProcessor = new AudioProcessor( statusIntermediary, enableLoopback );
        this.statusDisplay = new StatusDisplay( statusIntermediary );
        this.apThread = new Thread( audioProcessor, "Audio Processor" );
    }

    public void run() throws InterruptedException {
        apThread.start();
        apThread.join();
    }

    @Override
    public void initialize() throws Exception {
        audioProcessor.initialize();
        statusDisplay.initialize();
    }

    @Override
    public void shutdown() throws Exception {
        audioProcessor.shutdown();
        statusDisplay.shutdown();
        System.exit( 0 );
    }
}
