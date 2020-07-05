package com.nukkitx.jackson.dataformat.nbt.generator;

import com.nukkitx.jackson.dataformat.nbt.NBTGenerator;
import com.nukkitx.nbt.NbtType;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.DataOutput;
import java.io.IOException;

public class ListTagWriter extends NBTWriter {

    protected final NbtType<?> type;

    private final DataOutput origin;
    private final ByteArrayOutputStream byteStream;

    private int size;

    public ListTagWriter(String name, NbtType<?> type, DataOutput output, NBTGenerator generator) throws IOException {
        super(name, null);
        this.byteStream = new ByteArrayOutputStream();
        this.output = generator.wrapStream(byteStream);
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
