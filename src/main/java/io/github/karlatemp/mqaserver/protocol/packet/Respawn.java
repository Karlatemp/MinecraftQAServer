package io.github.karlatemp.mqaserver.protocol.packet;

import io.github.karlatemp.mqaserver.protocol.AbstractPacketHandler;
import io.github.karlatemp.mqaserver.protocol.DefinedPacket;
import io.github.karlatemp.mqaserver.protocol.ProtocolConstants;
import io.netty.buffer.ByteBuf;

public class Respawn extends DefinedPacket {

    private Object dimension;
    private String worldName;
    private long seed;
    private short difficulty;
    private short gameMode;
    private short previousGameMode;
    private String levelType;
    private boolean debug;
    private boolean flat;
    private boolean copyMeta;

    public Respawn(Object dimension, String worldName, long seed, short difficulty, short gameMode, short previousGameMode, String levelType, boolean debug, boolean flat, boolean copyMeta) {
        this.dimension = dimension;
        this.worldName = worldName;
        this.seed = seed;
        this.difficulty = difficulty;
        this.gameMode = gameMode;
        this.previousGameMode = previousGameMode;
        this.levelType = levelType;
        this.debug = debug;
        this.flat = flat;
        this.copyMeta = copyMeta;
    }

    public Respawn() {
    }

    @Override
    public void read(ByteBuf buf, ProtocolConstants.Direction direction, int protocolVersion) {
        if (protocolVersion >= ProtocolConstants.MINECRAFT_1_16) {
            dimension = readString(buf);
            worldName = readString(buf);
        } else {
            dimension = buf.readInt();
        }
        if (protocolVersion >= ProtocolConstants.MINECRAFT_1_15) {
            seed = buf.readLong();
        }
        if (protocolVersion < ProtocolConstants.MINECRAFT_1_14) {
            difficulty = buf.readUnsignedByte();
        }
        gameMode = buf.readUnsignedByte();
        if (protocolVersion >= ProtocolConstants.MINECRAFT_1_16) {
            previousGameMode = buf.readUnsignedByte();
            debug = buf.readBoolean();
            flat = buf.readBoolean();
            copyMeta = buf.readBoolean();
        } else {
            levelType = readString(buf);
        }
    }

    @Override
    public void write(ByteBuf buf, ProtocolConstants.Direction direction, int protocolVersion) {
        if (protocolVersion >= ProtocolConstants.MINECRAFT_1_16) {
            writeString((String) dimension, buf);
            writeString(worldName, buf);
        } else {
            buf.writeInt(((Integer) dimension));
        }
        if (protocolVersion >= ProtocolConstants.MINECRAFT_1_15) {
            buf.writeLong(seed);
        }
        if (protocolVersion < ProtocolConstants.MINECRAFT_1_14) {
            buf.writeByte(difficulty);
        }
        buf.writeByte(gameMode);
        if (protocolVersion >= ProtocolConstants.MINECRAFT_1_16) {
            buf.writeByte(previousGameMode);
            buf.writeBoolean(debug);
            buf.writeBoolean(flat);
            buf.writeBoolean(copyMeta);
        } else {
            writeString(levelType, buf);
        }
    }

    @Override
    public void handle(AbstractPacketHandler handler) throws Exception {
        handler.handle(this);
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

    public String getLevelType() {
        return this.levelType;
    }

    public void setLevelType(String levelType) {
        this.levelType = levelType;
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

    public boolean isCopyMeta() {
        return this.copyMeta;
    }

    public void setCopyMeta(boolean copyMeta) {
        this.copyMeta = copyMeta;
    }

    public String toString() {
        return "Respawn(dimension=" + this.dimension + ", worldName=" + this.worldName + ", seed=" + this.seed + ", difficulty=" + this.difficulty + ", gameMode=" + this.gameMode + ", previousGameMode=" + this.previousGameMode + ", levelType=" + this.levelType + ", debug=" + this.debug + ", flat=" + this.flat + ", copyMeta=" + this.copyMeta + ")";
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof Respawn)) return false;
        final Respawn other = (Respawn) o;
        if (!other.canEqual(this)) return false;
        final Object this$dimension = this.getDimension();
        final Object other$dimension = other.getDimension();
        if (this$dimension == null ? other$dimension != null : !this$dimension.equals(other$dimension)) return false;
        final Object this$worldName = this.getWorldName();
        final Object other$worldName = other.getWorldName();
        if (this$worldName == null ? other$worldName != null : !this$worldName.equals(other$worldName)) return false;
        if (this.getSeed() != other.getSeed()) return false;
        if (this.getDifficulty() != other.getDifficulty()) return false;
        if (this.getGameMode() != other.getGameMode()) return false;
        if (this.getPreviousGameMode() != other.getPreviousGameMode()) return false;
        final Object this$levelType = this.getLevelType();
        final Object other$levelType = other.getLevelType();
        if (this$levelType == null ? other$levelType != null : !this$levelType.equals(other$levelType)) return false;
        if (this.isDebug() != other.isDebug()) return false;
        if (this.isFlat() != other.isFlat()) return false;
        return this.isCopyMeta() == other.isCopyMeta();
    }

    protected boolean canEqual(final Object other) {
        return other instanceof Respawn;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $dimension = this.getDimension();
        result = result * PRIME + ($dimension == null ? 43 : $dimension.hashCode());
        final Object $worldName = this.getWorldName();
        result = result * PRIME + ($worldName == null ? 43 : $worldName.hashCode());
        final long $seed = this.getSeed();
        result = result * PRIME + (int) ($seed >>> 32 ^ $seed);
        result = result * PRIME + this.getDifficulty();
        result = result * PRIME + this.getGameMode();
        result = result * PRIME + this.getPreviousGameMode();
        final Object $levelType = this.getLevelType();
        result = result * PRIME + ($levelType == null ? 43 : $levelType.hashCode());
        result = result * PRIME + (this.isDebug() ? 79 : 97);
        result = result * PRIME + (this.isFlat() ? 79 : 97);
        result = result * PRIME + (this.isCopyMeta() ? 79 : 97);
        return result;
    }
}
