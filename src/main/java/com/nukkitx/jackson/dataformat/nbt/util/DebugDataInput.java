package com.nukkitx.jackson.dataformat.nbt.util;

import java.io.Closeable;
import java.io.DataInput;
import java.io.Flushable;
import java.io.IOException;

public class DebugDataInput implements DataInput, Closeable, Flushable {

    private final DataInput origin;
    private final String prefix;

    public DebugDataInput(DataInput origin, String prefix) {
        this.origin = origin;
        this.prefix = prefix;
    }

    @Override
    public void readFully(byte[] b) throws IOException {
        System.out.println("debug(" + prefix + "): read fully (" + b.length + ")");
        origin.readFully(b);
    }

    @Override
    public void readFully(byte[] b, int off, int len) throws IOException {
        System.out.println("debug(" + prefix + "): read fully (" + len + ")");
        origin.readFully(b, off, len);
    }

    @Override
    public int skipBytes(int n) throws IOException {
        return origin.skipBytes(n);
    }

    @Override
    public boolean readBoolean() throws IOException {
        System.out.println("debug(" + prefix + "): read boolean");
        return origin.readBoolean();
    }

    @Override
    public byte readByte() throws IOException {
        System.out.println("debug(" + prefix + "): read byte");
        return origin.readByte();
    }

    @Override
    public int readUnsignedByte() throws IOException {
        System.out.println("debug(" + prefix + "): read ubyte");
        return origin.readUnsignedByte();
    }

    @Override
    public short readShort() throws IOException {
        System.out.println("debug(" + prefix + "): read short");
        return origin.readShort();
    }

    @Override
    public int readUnsignedShort() throws IOException {
        System.out.println("debug(" + prefix + "): read ushort");
        return origin.readUnsignedShort();
    }

    @Override
    public char readChar() throws IOException {
        System.out.println("debug(" + prefix + "): read char");
        return origin.readChar();
    }

    @Override
    public int readInt() throws IOException {
        System.out.println("debug(" + prefix + "): read int");
        return origin.readInt();
    }

    @Override
    public long readLong() throws IOException {
        System.out.println("debug(" + prefix + "): read long");
        return origin.readLong();
    }

    @Override
    public float readFloat() throws IOException {
        System.out.println("debug(" + prefix + "): read float");
        return origin.readFloat();
    }

    @Override
    public double readDouble() throws IOException {
        System.out.println("debug(" + prefix + "): read double");
        return origin.readDouble();
    }

    @Override
    public String readLine() throws IOException {
        System.out.println("debug(" + prefix + "): read line");
        return origin.readLine();
    }

    @Override
    public String readUTF() throws IOException {
        System.out.println("debug(" + prefix + "): read UTF");
        return origin.readUTF();
    }

    @Override
    public void close() throws IOException {
        if (origin instanceof Closeable) {
            ((Closeable) origin).close();
        }
    }

    @Override
    public void flush() throws IOException {
        if (origin instanceof Flushable) {
            ((Flushable) origin).flush();
        }
    }

    public static DebugDataInput from(DataInput origin) {
        return new DebugDataInput(origin, "");
    }

    public static DebugDataInput from(DataInput origin, String prefix) {
        return new DebugDataInput(origin, prefix);
    }
}
