package com.nukkitx.jackson.dataformat.nbt.parser;

import com.fasterxml.jackson.core.JsonToken;

public abstract class ArrayReader extends NBTReader {

    public ArrayReader(NBTReader parent) {
        super(parent);
    }

    @Override
    public JsonToken start() {
        return JsonToken.START_ARRAY;
    }

    @Override
    public JsonToken end() {
        return JsonToken.END_ARRAY;
    }
}
