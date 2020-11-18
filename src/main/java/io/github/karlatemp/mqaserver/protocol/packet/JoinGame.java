package io.github.karlatemp.mqaserver.protocol.packet;

import com.syntaxphoenix.syntaxapi.nbt.NbtCompound;
import com.syntaxphoenix.syntaxapi.nbt.NbtList;
import com.syntaxphoenix.syntaxapi.nbt.NbtNamedTag;
import com.syntaxphoenix.syntaxapi.nbt.tools.NbtOutputStream;
import io.github.karlatemp.mqaserver.core.ISystemCore;
import io.github.karlatemp.mqaserver.protocol.AbstractPacketHandler;
import io.github.karlatemp.mqaserver.protocol.DefinedPacket;
import io.github.karlatemp.mqaserver.protocol.ProtocolConstants;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufOutputStream;

import java.io.IOException;

public class JoinGame extends DefinedPacket {
    private final WorldType type;

    public enum WorldType {
        WORLD(0, "minecraft:overworld","minecraft:infiniburn_overworld"),
        // TODO: 1.15+ Nether update.
        // WORLD_NETHER(-1, "minecraft:the_nether","minecraft:infiniburn_nether"),
        WORLD_THE_END(1, "minecraft:the_end","minecraft:infiniburn_end");

        WorldType(int type, String namespace, String infiniburn) {
            this.type = type;
            this.namespace = namespace;
            this.infiniburn=infiniburn;
        }

        public final String namespace;
        public final int type;
        public final String infiniburn;
    }

    public JoinGame() {
        this(ISystemCore.getInstance().worldType());
    }

    public JoinGame(WorldType type) {
        this.type = type;
    }

    @Override
    public void handle(AbstractPacketHandler handler) throws Exception {
        handler.handle(this);
    }

    @Override
    public void write(ByteBuf buf, ProtocolConstants.Direction direction, int protocolVersion) {

        buf.writeInt(1); // Entity id
        // Gamemode | isHardmode
        buf.writeByte(3 | 8);
        if (protocolVersion >= ProtocolConstants.MINECRAFT_1_15) {
            // region
            buf.writeByte(3); // Gamemode

            writeVarInt(1, buf); // worlds size
            writeString(type.namespace, buf); // world


            try {
                NbtCompound dimensionCodec = new NbtCompound();
                dimensionCodec.set("name", type.namespace);
                dimensionCodec.set("piglin_safe", true);
                dimensionCodec.set("natural", true);
                dimensionCodec.set("ambient_light", 1f);
                dimensionCodec.set("fixed_time", 10086L);
                dimensionCodec.set("infiniburn", type.infiniburn);
                dimensionCodec.set("respawn_anchor_works", false);
                dimensionCodec.set("has_skylight", true);
                dimensionCodec.set("bed_works", false);
                dimensionCodec.set("effects", type.namespace);
                dimensionCodec.set("has_raids", false);
                dimensionCodec.set("coordinate_scale", 0f);
                dimensionCodec.set("ultrawarm", false);
                dimensionCodec.set("has_ceiling", false);
                dimensionCodec.set("shrunk", false);
                dimensionCodec.set("logical_height", 256);

                NbtCompound root = new NbtCompound(
                        new NbtNamedTag("dimension",
                                new NbtList<>(
                                        dimensionCodec
                                )
                        )
                );
                NbtOutputStream outputStream = new NbtOutputStream(new ByteBufOutputStream(buf));
                outputStream.writeNamedTag("", root);
            } catch (IOException ex) {
                throw new RuntimeException("Exception writing dimensions", ex);
            }
            writeString(type.namespace, buf);
            writeString(type.namespace, buf);
            buf.writeLong(0); // hashed seed
            buf.writeByte(1); // max player
            writeVarInt(32, buf); // View Distance
            buf.writeBoolean(false); // Reduced Debug Info
            buf.writeBoolean(true); // Enable respawn screen
            buf.writeBoolean(true); // Is Debug
            buf.writeBoolean(false); // Is Flat
            // endregion
        } else if (protocolVersion >= ProtocolConstants.MINECRAFT_1_14) {
            // region
            buf.writeInt(type.type);// -1: Nether, 0: Overworld, 1: End
            buf.writeByte(1); // Max players
            writeString("default", buf); // level type
            writeVarInt(32, buf); // view dis
            buf.writeBoolean(false); // Reduced Debug Info
            // endregion
        } else {
            if (protocolVersion <= ProtocolConstants.MINECRAFT_1_9) {
                buf.writeByte(type.type);// -1: Nether, 0: Overworld, 1: End
            } else {
                buf.writeInt(type.type);// -1: Nether, 0: Overworld, 1: End
            }
            buf.writeByte(3); // 0: peaceful, 1: easy, 2: normal, 3: hard
            buf.writeByte(1); // Max players
            writeString("default", buf); // Level Type
            buf.writeByte(0); // Reduced Debug Info
        }
    }

    @Override
    public boolean equals(Object obj) {
        return false;
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public String toString() {
        return "JoinGame";
    }
}
