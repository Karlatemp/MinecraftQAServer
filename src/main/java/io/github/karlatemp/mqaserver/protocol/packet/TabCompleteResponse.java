package io.github.karlatemp.mqaserver.protocol.packet;

import com.mojang.brigadier.LiteralMessage;
import com.mojang.brigadier.context.StringRange;
import com.mojang.brigadier.suggestion.Suggestion;
import com.mojang.brigadier.suggestion.Suggestions;
import io.github.karlatemp.mqaserver.protocol.AbstractPacketHandler;
import io.github.karlatemp.mqaserver.protocol.DefinedPacket;
import io.github.karlatemp.mqaserver.protocol.ProtocolConstants;
import io.netty.buffer.ByteBuf;

import java.util.LinkedList;
import java.util.List;

public class TabCompleteResponse extends DefinedPacket {

    private int transactionId;
    private Suggestions suggestions;
    //
    private List<String> commands;

    public TabCompleteResponse(int transactionId, Suggestions suggestions) {
        this.transactionId = transactionId;
        this.suggestions = suggestions;
    }

    public TabCompleteResponse(List<String> commands) {
        this.commands = commands;
    }

    public TabCompleteResponse() {
    }

    @Override
    public void read(ByteBuf buf, ProtocolConstants.Direction direction, int protocolVersion) {
        if (protocolVersion >= ProtocolConstants.MINECRAFT_1_13) {
            transactionId = readVarInt(buf);
            int start = readVarInt(buf);
            int length = readVarInt(buf);
            StringRange range = StringRange.between(start, start + length);

            int cnt = readVarInt(buf);
            List<Suggestion> matches = new LinkedList<>();
            for (int i = 0; i < cnt; i++) {
                String match = readString(buf);
                String tooltip = buf.readBoolean() ? readString(buf) : null;

                matches.add(new Suggestion(range, match, new LiteralMessage(tooltip)));
            }

            suggestions = new Suggestions(range, matches);
        }

        if (protocolVersion < ProtocolConstants.MINECRAFT_1_13) {
            commands = readStringArray(buf);
        }
    }

    @Override
    public void write(ByteBuf buf, ProtocolConstants.Direction direction, int protocolVersion) {
        if (protocolVersion >= ProtocolConstants.MINECRAFT_1_13) {
            writeVarInt(transactionId, buf);
            writeVarInt(suggestions.getRange().getStart(), buf);
            writeVarInt(suggestions.getRange().getLength(), buf);

            writeVarInt(suggestions.getList().size(), buf);
            for (Suggestion suggestion : suggestions.getList()) {
                writeString(suggestion.getText(), buf);
                buf.writeBoolean(suggestion.getTooltip() != null && suggestion.getTooltip().getString() != null);
                if (suggestion.getTooltip() != null && suggestion.getTooltip().getString() != null) {
                    writeString(suggestion.getTooltip().getString(), buf);
                }
            }
        }

        if (protocolVersion < ProtocolConstants.MINECRAFT_1_13) {
            writeStringArray(commands, buf);
        }
    }

    @Override
    public void handle(AbstractPacketHandler handler) throws Exception {
        handler.handle(this);
    }

    public int getTransactionId() {
        return this.transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public Suggestions getSuggestions() {
        return this.suggestions;
    }

    public void setSuggestions(Suggestions suggestions) {
        this.suggestions = suggestions;
    }

    public List<String> getCommands() {
        return this.commands;
    }

    public void setCommands(List<String> commands) {
        this.commands = commands;
    }

    public String toString() {
        return "TabCompleteResponse(transactionId=" + this.transactionId + ", suggestions=" + this.suggestions + ", commands=" + this.commands + ")";
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof TabCompleteResponse)) return false;
        final TabCompleteResponse other = (TabCompleteResponse) o;
        if (!other.canEqual(this)) return false;
        if (this.getTransactionId() != other.getTransactionId()) return false;
        final Object this$suggestions = this.getSuggestions();
        final Object other$suggestions = other.getSuggestions();
        if (this$suggestions == null ? other$suggestions != null : !this$suggestions.equals(other$suggestions))
            return false;
        final Object this$commands = this.getCommands();
        final Object other$commands = other.getCommands();
        return this$commands == null ? other$commands == null : this$commands.equals(other$commands);
    }

    protected boolean canEqual(final Object other) {
        return other instanceof TabCompleteResponse;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * PRIME + this.getTransactionId();
        final Object $suggestions = this.getSuggestions();
        result = result * PRIME + ($suggestions == null ? 43 : $suggestions.hashCode());
        final Object $commands = this.getCommands();
        result = result * PRIME + ($commands == null ? 43 : $commands.hashCode());
        return result;
    }
}
