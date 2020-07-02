package com.nukkitx.jackson.dataformat.nbt.generator;

import com.nukkitx.nbt.NbtType;

import java.io.DataOutput;
import java.io.IOException;

public abstract class NBTWriter {

    protected String name;

    protected boolean end = false;

    protected DataOutput output;

    public NBTWriter(String name, DataOutput output) {
        this.name = name;
        this.output = output;
    }

    public void write(NbtType<?> type, String name, Object value) throws IOException {

    }

    public void end() throws IOException {
        this.end = true;
    }
}
