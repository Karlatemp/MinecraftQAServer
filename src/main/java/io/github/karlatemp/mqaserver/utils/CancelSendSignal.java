package io.github.karlatemp.mqaserver.utils;

public class CancelSendSignal extends Error {

    public static final CancelSendSignal INSTANCE = new CancelSendSignal();

    private CancelSendSignal() {
        super(null, null, false, false);
    }

    @Override
    public Throwable initCause(Throwable cause) {
        return this;
    }

    @Override
    public Throwable fillInStackTrace() {
        return this;
    }
}
