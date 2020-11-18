package io.github.karlatemp.mqaserver.protocol;

public class OverflowPacketException extends RuntimeException {

    public OverflowPacketException(String message) {
        super(message);
    }
}
