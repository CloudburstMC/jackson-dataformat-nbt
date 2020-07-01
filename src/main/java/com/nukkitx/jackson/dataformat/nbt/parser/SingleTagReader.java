package com.nukkitx.jackson.dataformat.nbt.parser;

import com.fasterxml.jackson.core.JsonToken;
import com.nukkitx.nbt.NbtType;

import java.io.DataInput;
import java.io.IOException;

public class SingleTagReader extends NBTReader {

    protected NbtType<?> type;

    public SingleTagReader(DataInput input, NbtType<?> type) {
        super(input, null);
        this.type = type;
    }

    @Override
    public JsonToken get() {
        return null;
    }

    @Override
    public JsonToken start() throws IOException {
        return decodeValue(type);
    }

    @Override
    public JsonToken end() {
        return null;
    }
}
