package io.github.karlatemp.mqaserver.protocol.packet;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import io.github.karlatemp.mqaserver.protocol.AbstractPacketHandler;
import io.github.karlatemp.mqaserver.protocol.DefinedPacket;
import io.github.karlatemp.mqaserver.protocol.ProtocolConstants;
import io.netty.buffer.ByteBuf;

import java.io.ByteArrayInputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.util.Locale;

public class PluginMessage extends DefinedPacket {

    public static final Function<String, String> MODERNISE = new Function<String, String>() {
        @Override
        public String apply(String tag) {
            // Transform as per Bukkit
            if (tag.equals("BungeeCord")) {
                return "bungeecord:main";
            }
            if (tag.equals("bungeecord:main")) {
                return "BungeeCord";
            }

            // Code that gets to here is UNLIKELY to be viable on the Bukkit side of side things,
            // but we keep it anyway. It will eventually be enforced API side.
            if (tag.indexOf(':') != -1) {
                return tag;
            }

            return "legacy:" + tag.toLowerCase(Locale.ROOT);
        }
    };
    public static final Predicate<PluginMessage> SHOULD_RELAY = new Predicate<PluginMessage>() {
        @Override
        public boolean apply(PluginMessage input) {
            return (input.getTag().equals("REGISTER") || input.getTag().equals("minecraft:register") || input.getTag().equals("MC|Brand") || input.getTag().equals("minecraft:brand")) && input.getData().length < Byte.MAX_VALUE;
        }
    };
    //
    private String tag;
    private byte[] data;

    /**
     * Allow this packet to be sent as an "extended" packet.
     */
    private boolean allowExtendedPacket = false;

    public PluginMessage(String tag, byte[] data, boolean allowExtendedPacket) {
        this.tag = tag;
        this.data = data;
        this.allowExtendedPacket = allowExtendedPacket;
    }

    public PluginMessage() {
    }

    @Override
    public void read(ByteBuf buf, ProtocolConstants.Direction direction, int protocolVersion) {
        tag = (protocolVersion >= ProtocolConstants.MINECRAFT_1_13) ? MODERNISE.apply(readString(buf)) : readString(buf);
        int maxSize = direction == ProtocolConstants.Direction.TO_SERVER ? Short.MAX_VALUE : 0x100000;
        Preconditions.checkArgument(buf.readableBytes() < maxSize);
        data = new byte[buf.readableBytes()];
        buf.readBytes(data);
    }

    @Override
    public void write(ByteBuf buf, ProtocolConstants.Direction direction, int protocolVersion) {
        writeString((protocolVersion >= ProtocolConstants.MINECRAFT_1_13) ? MODERNISE.apply(tag) : tag, buf);
        buf.writeBytes(data);
    }

    @Override
    public void handle(AbstractPacketHandler handler) throws Exception {
        handler.handle(this);
    }

    public DataInput getStream() {
        return new DataInputStream(new ByteArrayInputStream(data));
    }

    public String getTag() {
        return this.tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public byte[] getData() {
        return this.data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public boolean isAllowExtendedPacket() {
        return this.allowExtendedPacket;
    }

    public void setAllowExtendedPacket(boolean allowExtendedPacket) {
        this.allowExtendedPacket = allowExtendedPacket;
    }

    public String toString() {
        return "PluginMessage(tag=" + this.tag + ", data=" + java.util.Arrays.toString(this.data) + ", allowExtendedPacket=" + this.allowExtendedPacket + ")";
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof PluginMessage)) return false;
        final PluginMessage other = (PluginMessage) o;
        if (!other.canEqual(this)) return false;
        final Object this$tag = this.getTag();
        final Object other$tag = other.getTag();
        if (this$tag == null ? other$tag != null : !this$tag.equals(other$tag)) return false;
        if (!java.util.Arrays.equals(this.getData(), other.getData())) return false;
        return this.isAllowExtendedPacket() == other.isAllowExtendedPacket();
    }

    protected boolean canEqual(final Object other) {
        return other instanceof PluginMessage;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $tag = this.getTag();
        result = result * PRIME + ($tag == null ? 43 : $tag.hashCode());
        result = result * PRIME + java.util.Arrays.hashCode(this.getData());
        result = result * PRIME + (this.isAllowExtendedPacket() ? 79 : 97);
        return result;
    }
}
