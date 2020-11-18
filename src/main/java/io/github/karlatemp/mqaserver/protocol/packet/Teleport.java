package io.github.karlatemp.mqaserver.protocol.packet;

import io.github.karlatemp.mqaserver.core.ISystemCore;
import io.github.karlatemp.mqaserver.protocol.AbstractPacketHandler;
import io.github.karlatemp.mqaserver.protocol.DefinedPacket;
import io.github.karlatemp.mqaserver.protocol.ProtocolConstants;
import io.netty.buffer.ByteBuf;

public class Teleport extends DefinedPacket {
    @Override
    public void handle(AbstractPacketHandler handler) throws Exception {

    }

    public double x, y, z;

    public Teleport() {
        double[] location = ISystemCore.getInstance().newJoinLocation();
        x = location[0];
        y = location[1];
        z = location[2];
    }

    public Teleport(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
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
        return "Teleport";
    }

    @Override
    public void write(ByteBuf buf, ProtocolConstants.Direction direction, int protocolVersion) {
        buf.writeDouble(x);
        buf.writeDouble(y);
        buf.writeDouble(z);
        buf.writeFloat(0);
        buf.writeFloat(0);
        buf.writeByte(0);
        if (protocolVersion >= ProtocolConstants.MINECRAFT_1_9) {
            writeVarInt(1, buf);
        }
    }
}
