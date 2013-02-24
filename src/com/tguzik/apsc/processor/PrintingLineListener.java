package com.tguzik.apsc.processor;

import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class PrintingLineListener implements LineListener {
    private static final Logger LOGGER = LoggerFactory.getLogger( PrintingLineListener.class );
    private final String tag;

    private PrintingLineListener( String tag ) {
        this.tag = tag;
    }

    @Override
    public void update( LineEvent event ) {
        LOGGER.info( String.format( "[%s] %s event recieved at position %d",
                                    tag,
                                    event.getType(),
                                    event.getFramePosition() ) );
    }

    public static PrintingLineListener create( String tag ) {
        return new PrintingLineListener( tag );
    }
}
