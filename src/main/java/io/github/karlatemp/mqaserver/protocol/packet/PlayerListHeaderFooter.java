package io.github.karlatemp.mqaserver.protocol.packet;

import io.github.karlatemp.mqaserver.protocol.AbstractPacketHandler;
import io.github.karlatemp.mqaserver.protocol.DefinedPacket;
import io.github.karlatemp.mqaserver.protocol.ProtocolConstants;
import io.netty.buffer.ByteBuf;

public class PlayerListHeaderFooter extends DefinedPacket {

    private String header;
    private String footer;

    public PlayerListHeaderFooter(String header, String footer) {
        this.header = header;
        this.footer = footer;
    }

    public PlayerListHeaderFooter() {
    }

    @Override
    public void read(ByteBuf buf, ProtocolConstants.Direction direction, int protocolVersion) {
        header = readString(buf);
        footer = readString(buf);
    }

    @Override
    public void write(ByteBuf buf, ProtocolConstants.Direction direction, int protocolVersion) {
        writeString(header, buf);
        writeString(footer, buf);
    }

    @Override
    public void handle(AbstractPacketHandler handler) throws Exception {
        handler.handle(this);
    }

    public String getHeader() {
        return this.header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getFooter() {
        return this.footer;
    }

    public void setFooter(String footer) {
        this.footer = footer;
    }

    public String toString() {
        return "PlayerListHeaderFooter(header=" + this.header + ", footer=" + this.footer + ")";
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof PlayerListHeaderFooter)) return false;
        final PlayerListHeaderFooter other = (PlayerListHeaderFooter) o;
        if (!other.canEqual(this)) return false;
        final Object this$header = this.getHeader();
        final Object other$header = other.getHeader();
        if (this$header == null ? other$header != null : !this$header.equals(other$header)) return false;
        final Object this$footer = this.getFooter();
        final Object other$footer = other.getFooter();
        return this$footer == null ? other$footer == null : this$footer.equals(other$footer);
    }

    protected boolean canEqual(final Object other) {
        return other instanceof PlayerListHeaderFooter;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $header = this.getHeader();
        result = result * PRIME + ($header == null ? 43 : $header.hashCode());
        final Object $footer = this.getFooter();
        result = result * PRIME + ($footer == null ? 43 : $footer.hashCode());
        return result;
    }
}
