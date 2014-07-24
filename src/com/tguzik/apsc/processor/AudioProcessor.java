package com.tguzik.apsc.processor;

import javax.sound.sampled.*;
import java.nio.ByteBuffer;
import java.nio.ShortBuffer;

import com.tguzik.apsc.StatusIntermediary;
import com.tguzik.apsc.filters.AudioChainBuilder;
import com.tguzik.apsc.filters.AudioFilter;
import com.tguzik.util.annotations.Shutdownable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AudioProcessor implements Runnable, Shutdownable {
    private static final Logger LOGGER = LoggerFactory.getLogger( AudioProcessor.class );
    private static final AudioFormat AUDIO_FORMAT = constructAudioFormat();
    private static final int AUDIO_BUFFER_SIZE_SAMPLES = (int) (AUDIO_FORMAT.getFrameRate() / 10);

    private final boolean enableLoopback;
    private final AudioFilter filters;
    private SourceDataLine loopback = null;
    private TargetDataLine line = null;

    /**
     * Constructs a Mono, 48kHz, 2-bytes per sample, one sample per frame format
     */
    static AudioFormat constructAudioFormat() {
        AudioFormat af = new AudioFormat( AudioFormat.Encoding.PCM_SIGNED, 48000.0f, 16, 1, 2, 48000.0f, true );

        LOGGER.debug( "Format:                  " + af );
        LOGGER.debug( "Frame size in bytes:     " + af.getFrameSize() );
        LOGGER.debug( "Frames/second:           " + af.getFrameRate() );
        LOGGER.debug( "Sample size in bits:     " + af.getSampleSizeInBits() );
        LOGGER.debug( "Samples/second:          " + af.getSampleRate() );

        return af;
    }

    public AudioProcessor( StatusIntermediary statusIntermediary, boolean enableLoopback ) {
        this.filters = AudioChainBuilder.build( AUDIO_FORMAT, statusIntermediary, AUDIO_BUFFER_SIZE_SAMPLES );
        this.enableLoopback = enableLoopback;
    }

    @Override
    public void run() {
        LOGGER.info( "Reading audio data..." );
        captureLoop( line, loopback );
    }

    @Override
    public void initialize() throws Exception {
        LOGGER.info( "Opening lines..." );
        line = openDataLine();

        if ( enableLoopback ) {
            loopback = openLoopback();
        }
    }

    @Override
    public void shutdown() throws Exception {
        if ( line != null ) {
            line.close();
        }
        if ( loopback != null ) {
            loopback.close();
        }
    }

    /**
     * Fun fact - if we were to read the audio data too often (say, more often than once per
     * 100ms), CPU usage would go through the roof. Otherwise, with reads spaced by at least
     * 100ms, CPU usage should be at around 1%-2%.
     */
    void captureLoop( final TargetDataLine line, final SourceDataLine loopback ) {
        final int byteBufferSizeBytes = AUDIO_BUFFER_SIZE_SAMPLES * AUDIO_FORMAT.getFrameSize();

        byte bbuf[] = new byte[ byteBufferSizeBytes ];
        double dbuf[] = new double[ AUDIO_BUFFER_SIZE_SAMPLES ];
        ShortBuffer sbuf = ByteBuffer.wrap( bbuf ).asShortBuffer();

        while ( line.isOpen() ) {
            // Read audio data from input line (most likely the microphone)
            line.read( bbuf, 0, byteBufferSizeBytes );

            // If loopback is enabled, write the data from microphone to speakers
            if ( enableLoopback && loopback.isActive() ) {
                loopback.write( bbuf, 0, byteBufferSizeBytes );
            }

            // Rewrite the `short` values as `double` and send them to the filter chain
            rewriteData( sbuf, dbuf );
            filters.process( dbuf );
        }
    }

    SourceDataLine openLoopback() throws LineUnavailableException {
        SourceDataLine loopback = AudioSystem.getSourceDataLine( AUDIO_FORMAT );
        loopback.addLineListener( PrintingLineListener.create( "Output" ) );
        loopback.open();
        loopback.start();

        /* Fun fact - putting the following line here would hang the application on Windows XP:
         *    loopback.drain();
         */

        return loopback;
    }

    TargetDataLine openDataLine() throws LineUnavailableException {
        final TargetDataLine line = AudioSystem.getTargetDataLine( AUDIO_FORMAT );
        line.addLineListener( PrintingLineListener.create( "Input" ) );
        line.open( AUDIO_FORMAT );
        line.start();

        /* Fun fact - putting the following line here would hang the application on Windows XP:
         *    loopback.drain();
         */

        return line;
    }

    void rewriteData( ShortBuffer sbuf, double[] dbuf ) {
        if ( sbuf.limit() != dbuf.length ) {
            throw new UnsupportedOperationException( "Sizes of sbuf and dbuf must match" );
        }
        for ( int i = 0; i < dbuf.length; i++ ) {
            dbuf[ i ] = sbuf.get( i );
        }
    }
}
