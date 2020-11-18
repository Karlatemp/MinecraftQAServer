/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.karlatemp.mqaserver.protocol.packet;

import io.github.karlatemp.mqaserver.protocol.AbstractPacketHandler;
import io.github.karlatemp.mqaserver.protocol.DefinedPacket;
import io.netty.buffer.ByteBuf;

public class LoginRequest extends DefinedPacket {

    private String data;

    public LoginRequest(String data) {
        this.data = data;
    }

    public LoginRequest() {
    }

    @Override
    public void read(ByteBuf buf) {
        data = readString(buf);
    }

    @Override
    public void write(ByteBuf buf) {
        writeString(data, buf);
    }

    @Override
    public void handle(AbstractPacketHandler handler) throws Exception {
        handler.handle(this);
    }

    public String getData() {
        return this.data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String toString() {
        return "LoginRequest(data=" + this.data + ")";
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof LoginRequest)) return false;
        final LoginRequest other = (LoginRequest) o;
        if (!other.canEqual(this)) return false;
        final Object this$data = this.getData();
        final Object other$data = other.getData();
        return this$data == null ? other$data == null : this$data.equals(other$data);
    }

    protected boolean canEqual(final Object other) {
        return other instanceof LoginRequest;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $data = this.getData();
        result = result * PRIME + ($data == null ? 43 : $data.hashCode());
        return result;
    }
}
