package io.github.karlatemp.mqaserver.protocol.packet;

import com.google.common.base.Preconditions;
import io.github.karlatemp.mqaserver.protocol.AbstractPacketHandler;
import io.github.karlatemp.mqaserver.protocol.DefinedPacket;
import io.github.karlatemp.mqaserver.protocol.ProtocolConstants;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import se.llbit.nbt.NamedTag;
import se.llbit.nbt.Tag;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class Login extends DefinedPacket {

    private int entityId;
    private short gameMode;
    private short previousGameMode;
    private Set<String> worldNames;
    private Tag dimensions;
    private Object dimension;
    private String worldName;
    private long seed;
    private short difficulty;
    private short maxPlayers;
    private String levelType;
    private int viewDistance;
    private boolean reducedDebugInfo;
    private boolean normalRespawn;
    private boolean debug;
    private boolean flat;

    public Login(int entityId, short gameMode, short previousGameMode, Set<String> worldNames, Tag dimensions, Object dimension, String worldName, long seed, short difficulty, short maxPlayers, String levelType, int viewDistance, boolean reducedDebugInfo, boolean normalRespawn, boolean debug, boolean flat) {
        this.entityId = entityId;
        this.gameMode = gameMode;
        this.previousGameMode = previousGameMode;
        this.worldNames = worldNames;
        this.dimensions = dimensions;
        this.dimension = dimension;
        this.worldName = worldName;
        this.seed = seed;
        this.difficulty = difficulty;
        this.maxPlayers = maxPlayers;
        this.levelType = levelType;
        this.viewDistance = viewDistance;
        this.reducedDebugInfo = reducedDebugInfo;
        this.normalRespawn = normalRespawn;
        this.debug = debug;
        this.flat = flat;
    }

    public Login() {
    }

    @Override
    public void read(ByteBuf buf, ProtocolConstants.Direction direction, int protocolVersion) {
        entityId = buf.readInt();
        gameMode = buf.readUnsignedByte();
        if (protocolVersion >= ProtocolConstants.MINECRAFT_1_16) {
            previousGameMode = buf.readUnsignedByte();

            worldNames = new HashSet<>();
            int worldCount = readVarInt(buf);
            Preconditions.checkArgument(worldCount < 128, "Too many worlds %s", worldCount);

            for (int i = 0; i < worldCount; i++) {
                worldNames.add(readString(buf));
            }

            dimensions = NamedTag.read(new DataInputStream(new ByteBufInputStream(buf)));
            Preconditions.checkArgument(!dimensions.isError(), "Error reading dimensions: %s", dimensions.error());
        }

        if (protocolVersion >= ProtocolConstants.MINECRAFT_1_16) {
            dimension = readString(buf);
            worldName = readString(buf);
        } else if (protocolVersion > ProtocolConstants.MINECRAFT_1_9) {
            dimension = buf.readInt();
        } else {
            dimension = (int) buf.readByte();
        }
        if (protocolVersion >= ProtocolConstants.MINECRAFT_1_15) {
            seed = buf.readLong();
        }
        if (protocolVersion < ProtocolConstants.MINECRAFT_1_14) {
            difficulty = buf.readUnsignedByte();
        }
        maxPlayers = buf.readUnsignedByte();
        if (protocolVersion < ProtocolConstants.MINECRAFT_1_16) {
            levelType = readString(buf);
        }
        if (protocolVersion >= ProtocolConstants.MINECRAFT_1_14) {
            viewDistance = readVarInt(buf);
        }
        if (protocolVersion >= 29) {
            reducedDebugInfo = buf.readBoolean();
        }
        if (protocolVersion >= ProtocolConstants.MINECRAFT_1_15) {
            normalRespawn = buf.readBoolean();
        }
        if (protocolVersion >= ProtocolConstants.MINECRAFT_1_16) {
            debug = buf.readBoolean();
            flat = buf.readBoolean();
        }
    }

    @Override
    public void write(ByteBuf buf, ProtocolConstants.Direction direction, int protocolVersion) {
        buf.writeInt(entityId);
        buf.writeByte(gameMode);
        if (protocolVersion >= ProtocolConstants.MINECRAFT_1_16) {
            buf.writeByte(previousGameMode);

            writeVarInt(worldNames.size(), buf);
            for (String world : worldNames) {
                writeString(world, buf);
            }

            try {
                dimensions.write(new DataOutputStream(new ByteBufOutputStream(buf)));
            } catch (IOException ex) {
                throw new RuntimeException("Exception writing dimensions", ex);
            }
        }

        if (protocolVersion >= ProtocolConstants.MINECRAFT_1_16) {
            writeString((String) dimension, buf);
            writeString(worldName, buf);
        } else if (protocolVersion > ProtocolConstants.MINECRAFT_1_9) {
            buf.writeInt((Integer) dimension);
        } else {
            buf.writeByte((Integer) dimension);
        }
        if (protocolVersion >= ProtocolConstants.MINECRAFT_1_15) {
            buf.writeLong(seed);
        }
        if (protocolVersion < ProtocolConstants.MINECRAFT_1_14) {
            buf.writeByte(difficulty);
        }
        buf.writeByte(maxPlayers);
        if (protocolVersion < ProtocolConstants.MINECRAFT_1_16) {
            writeString(levelType, buf);
        }
        if (protocolVersion >= ProtocolConstants.MINECRAFT_1_14) {
            writeVarInt(viewDistance, buf);
        }
        if (protocolVersion >= 29) {
            buf.writeBoolean(reducedDebugInfo);
        }
        if (protocolVersion >= ProtocolConstants.MINECRAFT_1_15) {
            buf.writeBoolean(normalRespawn);
        }
        if (protocolVersion >= ProtocolConstants.MINECRAFT_1_16) {
            buf.writeBoolean(debug);
            buf.writeBoolean(flat);
        }
    }

    @Override
    public void handle(AbstractPacketHandler handler) throws Exception {
        handler.handle(this);
    }

    public int getEntityId() {
        return this.entityId;
    }

    public void setEntityId(int entityId) {
        this.entityId = entityId;
    }

    public short getGameMode() {
        return this.gameMode;
    }

    public void setGameMode(short gameMode) {
        this.gameMode = gameMode;
    }

    public short getPreviousGameMode() {
        return this.previousGameMode;
    }

    public void setPreviousGameMode(short previousGameMode) {
        this.previousGameMode = previousGameMode;
    }

    public Set<String> getWorldNames() {
        return this.worldNames;
    }

    public void setWorldNames(Set<String> worldNames) {
        this.worldNames = worldNames;
    }

    public Tag getDimensions() {
        return this.dimensions;
    }

    public void setDimensions(Tag dimensions) {
        this.dimensions = dimensions;
    }

    public Object getDimension() {
        return this.dimension;
    }

    public void setDimension(Object dimension) {
        this.dimension = dimension;
    }

    public String getWorldName() {
        return this.worldName;
    }

    public void setWorldName(String worldName) {
        this.worldName = worldName;
    }

    public long getSeed() {
        return this.seed;
    }

    public void setSeed(long seed) {
        this.seed = seed;
    }

    public short getDifficulty() {
        return this.difficulty;
    }

    public void setDifficulty(short difficulty) {
        this.difficulty = difficulty;
    }

    public short getMaxPlayers() {
        return this.maxPlayers;
    }

    public void setMaxPlayers(short maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public String getLevelType() {
        return this.levelType;
    }

    public void setLevelType(String levelType) {
        this.levelType = levelType;
    }

    public int getViewDistance() {
        return this.viewDistance;
    }

    public void setViewDistance(int viewDistance) {
        this.viewDistance = viewDistance;
    }

    public boolean isReducedDebugInfo() {
        return this.reducedDebugInfo;
    }

    public void setReducedDebugInfo(boolean reducedDebugInfo) {
        this.reducedDebugInfo = reducedDebugInfo;
    }

    public boolean isNormalRespawn() {
        return this.normalRespawn;
    }

    public void setNormalRespawn(boolean normalRespawn) {
        this.normalRespawn = normalRespawn;
    }

    public boolean isDebug() {
        return this.debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public boolean isFlat() {
        return this.flat;
    }

    public void setFlat(boolean flat) {
        this.flat = flat;
    }

    public String toString() {
        return "Login(entityId=" + this.entityId + ", gameMode=" + this.gameMode + ", previousGameMode=" + this.previousGameMode + ", worldNames=" + this.worldNames + ", dimensions=" + this.dimensions + ", dimension=" + this.dimension + ", worldName=" + this.worldName + ", seed=" + this.seed + ", difficulty=" + this.difficulty + ", maxPlayers=" + this.maxPlayers + ", levelType=" + this.levelType + ", viewDistance=" + this.viewDistance + ", reducedDebugInfo=" + this.reducedDebugInfo + ", normalRespawn=" + this.normalRespawn + ", debug=" + this.debug + ", flat=" + this.flat + ")";
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof Login)) return false;
        final Login other = (Login) o;
        if (!other.canEqual(this)) return false;
        if (this.getEntityId() != other.getEntityId()) return false;
        if (this.getGameMode() != other.getGameMode()) return false;
        if (this.getPreviousGameMode() != other.getPreviousGameMode()) return false;
        final Object this$worldNames = this.getWorldNames();
        final Object other$worldNames = other.getWorldNames();
        if (this$worldNames == null ? other$worldNames != null : !this$worldNames.equals(other$worldNames))
            return false;
        final Object this$dimensions = this.getDimensions();
        final Object other$dimensions = other.getDimensions();
        if (this$dimensions == null ? other$dimensions != null : !this$dimensions.equals(other$dimensions))
            return false;
        final Object this$dimension = this.getDimension();
        final Object other$dimension = other.getDimension();
        if (this$dimension == null ? other$dimension != null : !this$dimension.equals(other$dimension)) return false;
        final Object this$worldName = this.getWorldName();
        final Object other$worldName = other.getWorldName();
        if (this$worldName == null ? other$worldName != null : !this$worldName.equals(other$worldName)) return false;
        if (this.getSeed() != other.getSeed()) return false;
        if (this.getDifficulty() != other.getDifficulty()) return false;
        if (this.getMaxPlayers() != other.getMaxPlayers()) return false;
        final Object this$levelType = this.getLevelType();
        final Object other$levelType = other.getLevelType();
        if (this$levelType == null ? other$levelType != null : !this$levelType.equals(other$levelType)) return false;
        if (this.getViewDistance() != other.getViewDistance()) return false;
        if (this.isReducedDebugInfo() != other.isReducedDebugInfo()) return false;
        if (this.isNormalRespawn() != other.isNormalRespawn()) return false;
        if (this.isDebug() != other.isDebug()) return false;
        return this.isFlat() == other.isFlat();
    }

    protected boolean canEqual(final Object other) {
        return other instanceof Login;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * PRIME + this.getEntityId();
        result = result * PRIME + this.getGameMode();
        result = result * PRIME + this.getPreviousGameMode();
        final Object $worldNames = this.getWorldNames();
        result = result * PRIME + ($worldNames == null ? 43 : $worldNames.hashCode());
        final Object $dimensions = this.getDimensions();
        result = result * PRIME + ($dimensions == null ? 43 : $dimensions.hashCode());
        final Object $dimension = this.getDimension();
        result = result * PRIME + ($dimension == null ? 43 : $dimension.hashCode());
        final Object $worldName = this.getWorldName();
        result = result * PRIME + ($worldName == null ? 43 : $worldName.hashCode());
        final long $seed = this.getSeed();
        result = result * PRIME + (int) ($seed >>> 32 ^ $seed);
        result = result * PRIME + this.getDifficulty();
        result = result * PRIME + this.getMaxPlayers();
        final Object $levelType = this.getLevelType();
        result = result * PRIME + ($levelType == null ? 43 : $levelType.hashCode());
        result = result * PRIME + this.getViewDistance();
        result = result * PRIME + (this.isReducedDebugInfo() ? 79 : 97);
        result = result * PRIME + (this.isNormalRespawn() ? 79 : 97);
        result = result * PRIME + (this.isDebug() ? 79 : 97);
        result = result * PRIME + (this.isFlat() ? 79 : 97);
        return result;
    }
}
