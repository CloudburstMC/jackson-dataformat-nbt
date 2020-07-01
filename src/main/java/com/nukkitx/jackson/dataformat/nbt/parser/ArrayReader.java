package com.nukkitx.jackson.dataformat.nbt.parser;

import com.fasterxml.jackson.core.JsonToken;

import java.io.DataInput;
import java.io.IOException;

public abstract class ArrayReader extends NBTReader {

    protected int index = 0;
    protected int length;

    protected JsonToken token;

    public ArrayReader(DataInput input, NBTReader parent) {
        super(input, parent);
    }

    @Override
    public JsonToken get() throws IOException {
        if (index >= length) {
            return null;
        }

        readValue();
        index++;
        return this.token;
    }

    abstract void readValue() throws IOException;

    @Override
    public JsonToken start() {
        return JsonToken.START_ARRAY;
    }

    @Override
    public JsonToken end() {
        return JsonToken.END_ARRAY;
    }
}
