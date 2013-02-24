package com.tguzik.util.annotations;

public interface Shutdownable {
    void initialize() throws Exception;

    void shutdown() throws Exception;
}
