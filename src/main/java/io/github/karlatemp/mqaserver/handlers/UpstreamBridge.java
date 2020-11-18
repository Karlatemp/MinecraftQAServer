package io.github.karlatemp.mqaserver.handlers;

import io.github.karlatemp.mqaserver.core.PlayerImpl;
import io.github.karlatemp.mqaserver.netty.PacketHandler;
import io.github.karlatemp.mqaserver.protocol.DefinedPacket;

public abstract class UpstreamBridge extends PacketHandler {
    private final PlayerImpl player;

    public UpstreamBridge(PlayerImpl player) {
        this.player = player;
    }

    @Override
    public String toString() {
        return "[" + player.getName() + "] -> UpstreamBridge";
    }

    protected void writePacket(DefinedPacket packet) {
        player.sendPacket(packet);
    }

    protected abstract void setup();
}
