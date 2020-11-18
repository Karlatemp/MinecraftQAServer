package io.github.karlatemp.mqaserver.netty.cipher;

import io.github.karlatemp.mqaserver.jni.cipher.BungeeCipher;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class CipherEncoder extends MessageToByteEncoder<ByteBuf> {

    private final BungeeCipher cipher;

    public CipherEncoder(BungeeCipher cipher) {
        this.cipher = cipher;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, ByteBuf in, ByteBuf out) throws Exception {
        cipher.cipher(in, out);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        cipher.free();
    }
}
