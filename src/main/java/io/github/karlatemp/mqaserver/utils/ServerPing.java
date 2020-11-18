package io.github.karlatemp.mqaserver.utils;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Represents the standard list data returned by opening a server in the
 * Minecraft client server list, or hitting it with a packet 0xFE.
 */
public class ServerPing {

    private Protocol version;

    public ServerPing(Protocol version, Players players, BaseComponent description, Favicon favicon) {
        this.version = version;
        this.players = players;
        this.description = description;
        this.favicon = favicon;
    }

    public ServerPing() {
    }

    public Protocol getVersion() {
        return this.version;
    }

    public Players getPlayers() {
        return this.players;
    }

    public ModInfo getModinfo() {
        return this.modinfo;
    }

    public void setVersion(Protocol version) {
        this.version = version;
    }

    public void setPlayers(Players players) {
        this.players = players;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof ServerPing)) return false;
        final ServerPing other = (ServerPing) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$version = this.getVersion();
        final Object other$version = other.getVersion();
        if (!Objects.equals(this$version, other$version)) return false;
        final Object this$players = this.getPlayers();
        final Object other$players = other.getPlayers();
        if (!Objects.equals(this$players, other$players)) return false;
        final Object this$description = this.getDescription();
        final Object other$description = other.getDescription();
        if (!Objects.equals(this$description, other$description))
            return false;
        final Object this$favicon = this.getFavicon();
        final Object other$favicon = other.getFavicon();
        if (!Objects.equals(this$favicon, other$favicon)) return false;
        final Object this$modinfo = this.getModinfo();
        final Object other$modinfo = other.getModinfo();
        if (!Objects.equals(this$modinfo, other$modinfo)) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof ServerPing;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $version = this.getVersion();
        result = result * PRIME + ($version == null ? 43 : $version.hashCode());
        final Object $players = this.getPlayers();
        result = result * PRIME + ($players == null ? 43 : $players.hashCode());
        final Object $description = this.getDescription();
        result = result * PRIME + ($description == null ? 43 : $description.hashCode());
        final Object $favicon = this.getFavicon();
        result = result * PRIME + ($favicon == null ? 43 : $favicon.hashCode());
        final Object $modinfo = this.getModinfo();
        result = result * PRIME + ($modinfo == null ? 43 : $modinfo.hashCode());
        return result;
    }

    public String toString() {
        return "ServerPing(version=" + this.getVersion() + ", players=" + this.getPlayers() + ", description=" + this.getDescription() + ", modinfo=" + this.getModinfo() + ")";
    }

    public static class Protocol {

        private String name;
        private int protocol;

        public Protocol(String name, int protocol) {
            this.name = name;
            this.protocol = protocol;
        }

        public String getName() {
            return this.name;
        }

        public int getProtocol() {
            return this.protocol;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setProtocol(int protocol) {
            this.protocol = protocol;
        }

        public boolean equals(final Object o) {
            if (o == this) return true;
            if (!(o instanceof Protocol)) return false;
            final Protocol other = (Protocol) o;
            if (!other.canEqual((Object) this)) return false;
            final Object this$name = this.getName();
            final Object other$name = other.getName();
            if (!Objects.equals(this$name, other$name)) return false;
            if (this.getProtocol() != other.getProtocol()) return false;
            return true;
        }

        protected boolean canEqual(final Object other) {
            return other instanceof Protocol;
        }

        public int hashCode() {
            final int PRIME = 59;
            int result = 1;
            final Object $name = this.getName();
            result = result * PRIME + ($name == null ? 43 : $name.hashCode());
            result = result * PRIME + this.getProtocol();
            return result;
        }

        public String toString() {
            return "ServerPing.Protocol(name=" + this.getName() + ", protocol=" + this.getProtocol() + ")";
        }
    }

    private Players players;

    public static class Players {

        private int max;
        private int online;
        private PlayerInfo[] sample;

        public Players(int max, int online, PlayerInfo[] sample) {
            this.max = max;
            this.online = online;
            this.sample = sample;
        }

        public int getMax() {
            return this.max;
        }

        public int getOnline() {
            return this.online;
        }

        public PlayerInfo[] getSample() {
            return this.sample;
        }

        public void setMax(int max) {
            this.max = max;
        }

        public void setOnline(int online) {
            this.online = online;
        }

        public void setSample(PlayerInfo[] sample) {
            this.sample = sample;
        }

        public boolean equals(final Object o) {
            if (o == this) return true;
            if (!(o instanceof Players)) return false;
            final Players other = (Players) o;
            if (!other.canEqual((Object) this)) return false;
            if (this.getMax() != other.getMax()) return false;
            if (this.getOnline() != other.getOnline()) return false;
            if (!java.util.Arrays.deepEquals(this.getSample(), other.getSample())) return false;
            return true;
        }

        protected boolean canEqual(final Object other) {
            return other instanceof Players;
        }

        public int hashCode() {
            final int PRIME = 59;
            int result = 1;
            result = result * PRIME + this.getMax();
            result = result * PRIME + this.getOnline();
            result = result * PRIME + java.util.Arrays.deepHashCode(this.getSample());
            return result;
        }

        public String toString() {
            return "ServerPing.Players(max=" + this.getMax() + ", online=" + this.getOnline() + ", sample=" + java.util.Arrays.deepToString(this.getSample()) + ")";
        }
    }

    public static class PlayerInfo {

        private String name;
        private UUID uniqueId;

        private static final UUID md5UUID = Util.getUUID("af74a02d19cb445bb07f6866a861f783");

        public PlayerInfo(String name, String id) {
            setName(name);
            setId(id);
        }

        public PlayerInfo(String name, UUID uniqueId) {
            this.name = name;
            this.uniqueId = uniqueId;
        }

        public void setId(String id) {
            try {
                uniqueId = Util.getUUID(id);
            } catch (Exception e) {
                // Fallback on a valid uuid otherwise Minecraft complains
                uniqueId = md5UUID;
            }
        }

        public String getId() {
            return uniqueId.toString().replace("-", "");
        }

        public String getName() {
            return this.name;
        }

        public UUID getUniqueId() {
            return this.uniqueId;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setUniqueId(UUID uniqueId) {
            this.uniqueId = uniqueId;
        }

        public boolean equals(final Object o) {
            if (o == this) return true;
            if (!(o instanceof PlayerInfo)) return false;
            final PlayerInfo other = (PlayerInfo) o;
            if (!other.canEqual((Object) this)) return false;
            final Object this$name = this.getName();
            final Object other$name = other.getName();
            if (!Objects.equals(this$name, other$name)) return false;
            final Object this$uniqueId = this.getUniqueId();
            final Object other$uniqueId = other.getUniqueId();
            if (!Objects.equals(this$uniqueId, other$uniqueId)) return false;
            return true;
        }

        protected boolean canEqual(final Object other) {
            return other instanceof PlayerInfo;
        }

        public int hashCode() {
            final int PRIME = 59;
            int result = 1;
            final Object $name = this.getName();
            result = result * PRIME + ($name == null ? 43 : $name.hashCode());
            final Object $uniqueId = this.getUniqueId();
            result = result * PRIME + ($uniqueId == null ? 43 : $uniqueId.hashCode());
            return result;
        }

        public String toString() {
            return "ServerPing.PlayerInfo(name=" + this.getName() + ", uniqueId=" + this.getUniqueId() + ")";
        }
    }

    private BaseComponent description;
    private Favicon favicon;

    public static class ModInfo {

        private String type = "FML";
        private List<ModItem> modList = new ArrayList<>();

        public ModInfo() {
        }

        public String getType() {
            return this.type;
        }

        public List<ModItem> getModList() {
            return this.modList;
        }

        public void setType(String type) {
            this.type = type;
        }

        public void setModList(List<ModItem> modList) {
            this.modList = modList;
        }

        public boolean equals(final Object o) {
            if (o == this) return true;
            if (!(o instanceof ModInfo)) return false;
            final ModInfo other = (ModInfo) o;
            if (!other.canEqual((Object) this)) return false;
            final Object this$type = this.getType();
            final Object other$type = other.getType();
            if (this$type == null ? other$type != null : !this$type.equals(other$type)) return false;
            final Object this$modList = this.getModList();
            final Object other$modList = other.getModList();
            if (this$modList == null ? other$modList != null : !this$modList.equals(other$modList)) return false;
            return true;
        }

        protected boolean canEqual(final Object other) {
            return other instanceof ModInfo;
        }

        public int hashCode() {
            final int PRIME = 59;
            int result = 1;
            final Object $type = this.getType();
            result = result * PRIME + ($type == null ? 43 : $type.hashCode());
            final Object $modList = this.getModList();
            result = result * PRIME + ($modList == null ? 43 : $modList.hashCode());
            return result;
        }

        public String toString() {
            return "ServerPing.ModInfo(type=" + this.getType() + ", modList=" + this.getModList() + ")";
        }
    }

    public static class ModItem {

        private String modid;
        private String version;

        public ModItem(String modid, String version) {
            this.modid = modid;
            this.version = version;
        }

        public String getModid() {
            return this.modid;
        }

        public String getVersion() {
            return this.version;
        }

        public void setModid(String modid) {
            this.modid = modid;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public boolean equals(final Object o) {
            if (o == this) return true;
            if (!(o instanceof ModItem)) return false;
            final ModItem other = (ModItem) o;
            if (!other.canEqual((Object) this)) return false;
            final Object this$modid = this.getModid();
            final Object other$modid = other.getModid();
            if (!Objects.equals(this$modid, other$modid)) return false;
            final Object this$version = this.getVersion();
            final Object other$version = other.getVersion();
            if (!Objects.equals(this$version, other$version)) return false;
            return true;
        }

        protected boolean canEqual(final Object other) {
            return other instanceof ModItem;
        }

        public int hashCode() {
            final int PRIME = 59;
            int result = 1;
            final Object $modid = this.getModid();
            result = result * PRIME + ($modid == null ? 43 : $modid.hashCode());
            final Object $version = this.getVersion();
            result = result * PRIME + ($version == null ? 43 : $version.hashCode());
            return result;
        }

        public String toString() {
            return "ServerPing.ModItem(modid=" + this.getModid() + ", version=" + this.getVersion() + ")";
        }
    }

    // Right now, we don't get the mods from the user, so we just use a stock ModInfo object to
    // create the server ping. Vanilla clients will ignore this.
    private final ModInfo modinfo = new ModInfo();

    @Deprecated
    public ServerPing(Protocol version, Players players, String description, String favicon) {
        this(version, players, new TextComponent(TextComponent.fromLegacyText(description)), favicon == null ? null : Favicon.create(favicon));
    }

    @Deprecated
    public ServerPing(Protocol version, Players players, String description, Favicon favicon) {
        this(version, players, new TextComponent(TextComponent.fromLegacyText(description)), favicon);
    }

    @Deprecated
    public String getFavicon() {
        return getFaviconObject() == null ? null : getFaviconObject().getEncoded();
    }

    public Favicon getFaviconObject() {
        return this.favicon;
    }

    @Deprecated
    public void setFavicon(String favicon) {
        setFavicon(favicon == null ? null : Favicon.create(favicon));
    }

    public void setFavicon(Favicon favicon) {
        this.favicon = favicon;
    }

    @Deprecated
    public void setDescription(String description) {
        this.description = new TextComponent(TextComponent.fromLegacyText(description));
    }

    @Deprecated
    public String getDescription() {
        return BaseComponent.toLegacyText(description);
    }

    public void setDescriptionComponent(BaseComponent description) {
        this.description = description;
    }

    public BaseComponent getDescriptionComponent() {
        return description;
    }
}
