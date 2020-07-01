package com.nukkitx.jackson.dataformat.nbt.generator;

import com.nukkitx.nbt.NbtType;

import java.io.DataOutput;
import java.io.IOException;
import java.util.Objects;

public class CompoundTagWriter extends NBTWriter {

    public CompoundTagWriter(String name, DataOutput output) {
        super(name, output);
    }

    @Override
    public void write(NbtType<?> type, String name, Object value) throws IOException {
        Objects.requireNonNull(name);
        output.writeByte(type.getId());
        output.writeUTF(name);
    }
}
