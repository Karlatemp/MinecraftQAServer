package io.github.karlatemp.mqaserver.protocol.packet;

import io.github.karlatemp.mqaserver.protocol.AbstractPacketHandler;
import io.github.karlatemp.mqaserver.protocol.DefinedPacket;
import io.github.karlatemp.mqaserver.protocol.ProtocolConstants;
import io.netty.buffer.ByteBuf;

import java.util.UUID;

public class PlayerListItem extends DefinedPacket {

    private Action action;
    private Item[] items;

    public PlayerListItem() {
    }

    @Override
    public void read(ByteBuf buf, ProtocolConstants.Direction direction, int protocolVersion) {
        action = Action.values()[DefinedPacket.readVarInt(buf)];
        items = new Item[DefinedPacket.readVarInt(buf)];
        for (int i = 0; i < items.length; i++) {
            Item item = items[i] = new Item();
            item.setUuid(DefinedPacket.readUUID(buf));
            switch (action) {
                case ADD_PLAYER:
                    item.username = DefinedPacket.readString(buf);
                    item.properties = new String[DefinedPacket.readVarInt(buf)][];
                    for (int j = 0; j < item.properties.length; j++) {
                        String name = DefinedPacket.readString(buf);
                        String value = DefinedPacket.readString(buf);
                        if (buf.readBoolean()) {
                            item.properties[j] = new String[]
                                    {
                                            name, value, DefinedPacket.readString(buf)
                                    };
                        } else {
                            item.properties[j] = new String[]
                                    {
                                            name, value
                                    };
                        }
                    }
                    item.gamemode = DefinedPacket.readVarInt(buf);
                    item.ping = DefinedPacket.readVarInt(buf);
                    if (buf.readBoolean()) {
                        item.displayName = DefinedPacket.readString(buf);
                    }
                    break;
                case UPDATE_GAMEMODE:
                    item.gamemode = DefinedPacket.readVarInt(buf);
                    break;
                case UPDATE_LATENCY:
                    item.ping = DefinedPacket.readVarInt(buf);
                    break;
                case UPDATE_DISPLAY_NAME:
                    if (buf.readBoolean()) {
                        item.displayName = DefinedPacket.readString(buf);
                    }
            }
        }
    }

    @Override
    public void write(ByteBuf buf, ProtocolConstants.Direction direction, int protocolVersion) {
        DefinedPacket.writeVarInt(action.ordinal(), buf);
        DefinedPacket.writeVarInt(items.length, buf);
        for (Item item : items) {
            DefinedPacket.writeUUID(item.uuid, buf);
            switch (action) {
                case ADD_PLAYER:
                    DefinedPacket.writeString(item.username, buf);
                    DefinedPacket.writeVarInt(item.properties.length, buf);
                    for (String[] prop : item.properties) {
                        DefinedPacket.writeString(prop[0], buf);
                        DefinedPacket.writeString(prop[1], buf);
                        if (prop.length >= 3) {
                            buf.writeBoolean(true);
                            DefinedPacket.writeString(prop[2], buf);
                        } else {
                            buf.writeBoolean(false);
                        }
                    }
                    DefinedPacket.writeVarInt(item.gamemode, buf);
                    DefinedPacket.writeVarInt(item.ping, buf);
                    buf.writeBoolean(item.displayName != null);
                    if (item.displayName != null) {
                        DefinedPacket.writeString(item.displayName, buf);
                    }
                    break;
                case UPDATE_GAMEMODE:
                    DefinedPacket.writeVarInt(item.gamemode, buf);
                    break;
                case UPDATE_LATENCY:
                    DefinedPacket.writeVarInt(item.ping, buf);
                    break;
                case UPDATE_DISPLAY_NAME:
                    buf.writeBoolean(item.displayName != null);
                    if (item.displayName != null) {
                        DefinedPacket.writeString(item.displayName, buf);
                    }
                    break;
            }
        }
    }

    @Override
    public void handle(AbstractPacketHandler handler) throws Exception {
        handler.handle(this);
    }

    public Action getAction() {
        return this.action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public Item[] getItems() {
        return this.items;
    }

    public void setItems(Item[] items) {
        this.items = items;
    }

    public String toString() {
        return "PlayerListItem(action=" + this.action + ", items=" + java.util.Arrays.deepToString(this.items) + ")";
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof PlayerListItem)) return false;
        final PlayerListItem other = (PlayerListItem) o;
        if (!other.canEqual(this)) return false;
        final Object this$action = this.getAction();
        final Object other$action = other.getAction();
        if (this$action == null ? other$action != null : !this$action.equals(other$action)) return false;
        return java.util.Arrays.deepEquals(this.getItems(), other.getItems());
    }

    protected boolean canEqual(final Object other) {
        return other instanceof PlayerListItem;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $action = this.getAction();
        result = result * PRIME + ($action == null ? 43 : $action.hashCode());
        result = result * PRIME + java.util.Arrays.deepHashCode(this.getItems());
        return result;
    }

    public enum Action {

        ADD_PLAYER,
        UPDATE_GAMEMODE,
        UPDATE_LATENCY,
        UPDATE_DISPLAY_NAME,
        REMOVE_PLAYER
    }

    public static class Item {

        // ALL
        private UUID uuid;

        // ADD_PLAYER
        private String username;
        private String[][] properties;

        // ADD_PLAYER & UPDATE_GAMEMODE
        private int gamemode;

        // ADD_PLAYER & UPDATE_LATENCY
        private int ping;

        // ADD_PLAYER & UPDATE_DISPLAY_NAME
        private String displayName;

        public Item() {
        }

        public UUID getUuid() {
            return this.uuid;
        }

        public void setUuid(UUID uuid) {
            this.uuid = uuid;
        }

        public String getUsername() {
            return this.username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String[][] getProperties() {
            return this.properties;
        }

        public void setProperties(String[][] properties) {
            this.properties = properties;
        }

        public int getGamemode() {
            return this.gamemode;
        }

        public void setGamemode(int gamemode) {
            this.gamemode = gamemode;
        }

        public int getPing() {
            return this.ping;
        }

        public void setPing(int ping) {
            this.ping = ping;
        }

        public String getDisplayName() {
            return this.displayName;
        }

        public void setDisplayName(String displayName) {
            this.displayName = displayName;
        }

        public boolean equals(final Object o) {
            if (o == this) return true;
            if (!(o instanceof Item)) return false;
            final Item other = (Item) o;
            if (!other.canEqual(this)) return false;
            final Object this$uuid = this.uuid;
            final Object other$uuid = other.uuid;
            if (this$uuid == null ? other$uuid != null : !this$uuid.equals(other$uuid)) return false;
            final Object this$username = this.username;
            final Object other$username = other.username;
            if (this$username == null ? other$username != null : !this$username.equals(other$username)) return false;
            if (!java.util.Arrays.deepEquals(this.properties, other.properties)) return false;
            if (this.gamemode != other.gamemode) return false;
            if (this.ping != other.ping) return false;
            final Object this$displayName = this.displayName;
            final Object other$displayName = other.displayName;
            return this$displayName == null ? other$displayName == null : this$displayName.equals(other$displayName);
        }

        protected boolean canEqual(final Object other) {
            return other instanceof Item;
        }

        public int hashCode() {
            final int PRIME = 59;
            int result = 1;
            final Object $uuid = this.uuid;
            result = result * PRIME + ($uuid == null ? 43 : $uuid.hashCode());
            final Object $username = this.username;
            result = result * PRIME + ($username == null ? 43 : $username.hashCode());
            result = result * PRIME + java.util.Arrays.deepHashCode(this.properties);
            result = result * PRIME + this.gamemode;
            result = result * PRIME + this.ping;
            final Object $displayName = this.displayName;
            result = result * PRIME + ($displayName == null ? 43 : $displayName.hashCode());
            return result;
        }

        public String toString() {
            return "PlayerListItem.Item(uuid=" + this.uuid + ", username=" + this.username + ", properties=" + java.util.Arrays.deepToString(this.properties) + ", gamemode=" + this.gamemode + ", ping=" + this.ping + ", displayName=" + this.displayName + ")";
        }
    }
}
