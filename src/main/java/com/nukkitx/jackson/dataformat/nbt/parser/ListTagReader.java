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
        this.length = input.readInt();
        this.token = JsonToken.VALUE_NUMBER_INT; //just a non-null value
    }

    @Override
    public JsonToken get() throws IOException {
        if (super.get() == null) {
            return null;
        }

        return decodeValue(type);
    }

    @Override
    void readValue() {

    }
}
