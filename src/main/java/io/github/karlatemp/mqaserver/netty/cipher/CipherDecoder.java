package io.github.karlatemp.mqaserver.netty.cipher;

import io.github.karlatemp.mqaserver.jni.cipher.BungeeCipher;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

public class CipherDecoder extends MessageToMessageDecoder<ByteBuf> {

    private final BungeeCipher cipher;

    public CipherDecoder(BungeeCipher cipher) {
        this.cipher = cipher;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        out.add(cipher.cipher(ctx, msg));
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        cipher.free();
    }
}
