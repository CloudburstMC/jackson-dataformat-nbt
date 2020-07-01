package com.nukkitx.jackson.dataformat.nbt.generator;

import com.nukkitx.nbt.NbtType;

import java.io.*;

public class ListTagWriter extends NBTWriter {

    protected final NbtType<?> type;

    private final DataOutput origin;
    private final ByteArrayOutputStream byteStream;

    private int size;

    public ListTagWriter(String name, NbtType<?> type, DataOutput output) {
        super(name, null);
        this.byteStream = new ByteArrayOutputStream();
        this.output = new DataOutputStream(byteStream);
        this.origin = output;
        this.type = type;
    }

    @Override
    public void write(NbtType<?> type, String name, Object value) {
        if (type != this.type) {
            throw new IllegalArgumentException("Expected " + this.type + " got " + type);
        }

        size++;
    }

    @Override
    public void end() throws IOException {
        super.end();
        origin.writeByte(type.getId());
        origin.writeInt(size);
        origin.write(byteStream.toByteArray());
        ((Closeable) output).close();
    }
}
