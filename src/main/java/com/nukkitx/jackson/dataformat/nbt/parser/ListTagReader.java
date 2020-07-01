package com.nukkitx.jackson.dataformat.nbt.parser;

import com.fasterxml.jackson.core.JsonToken;
import com.nukkitx.nbt.NbtType;

import java.io.DataInput;
import java.io.IOException;

public class ListTagReader extends ArrayReader {

    protected NbtType<?> type;

    public ListTagReader(DataInput input, NBTReader parent) throws IOException {
        super(input, parent);

        this.type = NbtType.byId(input.readUnsignedByte());
    }

    @Override
    public JsonToken get() throws IOException {
        super.get();

        return decodeValue(type);
    }

    @Override
    void readValue() {

    }
}
