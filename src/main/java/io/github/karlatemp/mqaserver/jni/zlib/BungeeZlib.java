package io.github.karlatemp.mqaserver.jni.zlib;

import io.netty.buffer.ByteBuf;

import java.util.zip.DataFormatException;

public interface BungeeZlib
{

    void init(boolean compress, int level);

    void free();

    void process(ByteBuf in, ByteBuf out) throws DataFormatException;
}
