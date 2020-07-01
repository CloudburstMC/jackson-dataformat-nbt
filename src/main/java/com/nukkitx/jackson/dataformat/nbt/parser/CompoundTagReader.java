package com.nukkitx.jackson.dataformat.nbt.parser;

import com.fasterxml.jackson.core.JsonToken;
import com.nukkitx.nbt.NbtType;

import java.io.DataInput;
import java.io.IOException;

public class CompoundTagReader extends NBTReader {

    protected NbtType<?> type;

    public CompoundTagReader(DataInput input, NBTReader parent) {
        super(input, parent);
    }

    @Override
    public JsonToken get() throws IOException {
        if (type != null) {
            JsonToken r = decodeValue(type);
            type = null;
            return r;
        }

        type = NbtType.byId(input.readUnsignedByte());

        if (type == NbtType.END) {
            return null;
        }

        currentName = input.readUTF();
        return JsonToken.FIELD_NAME;
    }

    @Override
    public JsonToken start() {
        return JsonToken.START_OBJECT;
    }

    @Override
    public JsonToken end() {
        return JsonToken.END_OBJECT;
    }
}
