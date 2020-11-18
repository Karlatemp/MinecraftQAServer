package io.github.karlatemp.mqaserver.protocol.packet;

import io.github.karlatemp.mqaserver.protocol.AbstractPacketHandler;
import io.github.karlatemp.mqaserver.protocol.DefinedPacket;
import io.netty.buffer.ByteBuf;

public class Kick extends DefinedPacket {

    private String message;

    public Kick(String message) {
        this.message = message;
    }

    public Kick() {
    }

    @Override
    public void read(ByteBuf buf) {
        message = readString(buf);
    }

    @Override
    public void write(ByteBuf buf) {
        writeString(message, buf);
    }

    @Override
    public void handle(AbstractPacketHandler handler) throws Exception {
        handler.handle(this);
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String toString() {
        return "Kick(message=" + this.message + ")";
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof Kick)) return false;
        final Kick other = (Kick) o;
        if (!other.canEqual(this)) return false;
        final Object this$message = this.getMessage();
        final Object other$message = other.getMessage();
        return this$message == null ? other$message == null : this$message.equals(other$message);
    }

    protected boolean canEqual(final Object other) {
        return other instanceof Kick;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $message = this.getMessage();
        result = result * PRIME + ($message == null ? 43 : $message.hashCode());
        return result;
    }
}
