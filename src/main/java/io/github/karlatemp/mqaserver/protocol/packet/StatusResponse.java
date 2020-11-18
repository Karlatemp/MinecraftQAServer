package io.github.karlatemp.mqaserver.protocol.packet;

import io.github.karlatemp.mqaserver.protocol.AbstractPacketHandler;
import io.github.karlatemp.mqaserver.protocol.DefinedPacket;
import io.netty.buffer.ByteBuf;

public class StatusResponse extends DefinedPacket {

    private String response;

    public StatusResponse(String response) {
        this.response = response;
    }

    public StatusResponse() {
    }

    @Override
    public void read(ByteBuf buf) {
        response = readString(buf);
    }

    @Override
    public void write(ByteBuf buf) {
        writeString(response, buf);
    }

    @Override
    public void handle(AbstractPacketHandler handler) throws Exception {
        handler.handle(this);
    }

    public String getResponse() {
        return this.response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String toString() {
        return "StatusResponse(response=" + this.response + ")";
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof StatusResponse)) return false;
        final StatusResponse other = (StatusResponse) o;
        if (!other.canEqual(this)) return false;
        final Object this$response = this.getResponse();
        final Object other$response = other.getResponse();
        return this$response == null ? other$response == null : this$response.equals(other$response);
    }

    protected boolean canEqual(final Object other) {
        return other instanceof StatusResponse;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $response = this.getResponse();
        result = result * PRIME + ($response == null ? 43 : $response.hashCode());
        return result;
    }
}
