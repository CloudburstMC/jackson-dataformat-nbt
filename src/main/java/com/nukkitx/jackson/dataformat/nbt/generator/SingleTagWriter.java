package com.nukkitx.jackson.dataformat.nbt.generator;

import com.nukkitx.nbt.NbtType;

import java.io.DataOutput;
import java.io.IOException;

public class SingleTagWriter extends NBTWriter {

    public SingleTagWriter(String name, DataOutput output) {
        super(name, output);
    }

    @Override
    public void write(NbtType<?> type, String name, Object value) throws IOException {
        output.writeByte(type.getId());
        output.writeUTF("");
    }
}
