package io.github.karlatemp.mqaserver.protocol.packet;

import io.github.karlatemp.mqaserver.protocol.AbstractPacketHandler;
import io.github.karlatemp.mqaserver.protocol.DefinedPacket;
import io.github.karlatemp.mqaserver.protocol.ProtocolConstants;
import io.netty.buffer.ByteBuf;

public class Title extends DefinedPacket {

    private Action action;

    // TITLE & SUBTITLE
    private String text;

    // TIMES
    private int fadeIn;
    private int stay;
    private int fadeOut;

    public Title() {
    }

    @Override
    public void read(ByteBuf buf, ProtocolConstants.Direction direction, int protocolVersion) {
        int index = readVarInt(buf);

        // If we're working on 1.10 or lower, increment the value of the index so we pull out the correct value.
        if (protocolVersion <= ProtocolConstants.MINECRAFT_1_10 && index >= 2) {
            index++;
        }

        action = Action.values()[index];
        switch (action) {
            case TITLE:
            case SUBTITLE:
            case ACTIONBAR:
                text = readString(buf);
                break;
            case TIMES:
                fadeIn = buf.readInt();
                stay = buf.readInt();
                fadeOut = buf.readInt();
                break;
        }
    }

    @Override
    public void write(ByteBuf buf, ProtocolConstants.Direction direction, int protocolVersion) {
        int index = action.ordinal();

        // If we're working on 1.10 or lower, increment the value of the index so we pull out the correct value.
        if (protocolVersion <= ProtocolConstants.MINECRAFT_1_10 && index >= 2) {
            index--;
        }

        writeVarInt(index, buf);
        switch (action) {
            case TITLE:
            case SUBTITLE:
            case ACTIONBAR:
                writeString(text, buf);
                break;
            case TIMES:
                buf.writeInt(fadeIn);
                buf.writeInt(stay);
                buf.writeInt(fadeOut);
                break;
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

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getFadeIn() {
        return this.fadeIn;
    }

    public void setFadeIn(int fadeIn) {
        this.fadeIn = fadeIn;
    }

    public int getStay() {
        return this.stay;
    }

    public void setStay(int stay) {
        this.stay = stay;
    }

    public int getFadeOut() {
        return this.fadeOut;
    }

    public void setFadeOut(int fadeOut) {
        this.fadeOut = fadeOut;
    }

    public String toString() {
        return "Title(action=" + this.action + ", text=" + this.text + ", fadeIn=" + this.fadeIn + ", stay=" + this.stay + ", fadeOut=" + this.fadeOut + ")";
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof Title)) return false;
        final Title other = (Title) o;
        if (!other.canEqual(this)) return false;
        final Object this$action = this.getAction();
        final Object other$action = other.getAction();
        if (this$action == null ? other$action != null : !this$action.equals(other$action)) return false;
        final Object this$text = this.getText();
        final Object other$text = other.getText();
        if (this$text == null ? other$text != null : !this$text.equals(other$text)) return false;
        if (this.getFadeIn() != other.getFadeIn()) return false;
        if (this.getStay() != other.getStay()) return false;
        return this.getFadeOut() == other.getFadeOut();
    }

    protected boolean canEqual(final Object other) {
        return other instanceof Title;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $action = this.getAction();
        result = result * PRIME + ($action == null ? 43 : $action.hashCode());
        final Object $text = this.getText();
        result = result * PRIME + ($text == null ? 43 : $text.hashCode());
        result = result * PRIME + this.getFadeIn();
        result = result * PRIME + this.getStay();
        result = result * PRIME + this.getFadeOut();
        return result;
    }

    public enum Action {

        TITLE,
        SUBTITLE,
        ACTIONBAR,
        TIMES,
        CLEAR,
        RESET
    }
}
