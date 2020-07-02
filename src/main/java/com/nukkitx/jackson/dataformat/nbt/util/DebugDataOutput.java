package com.nukkitx.jackson.dataformat.nbt.util;

import java.io.Closeable;
import java.io.DataOutput;
import java.io.Flushable;
import java.io.IOException;

public class DebugDataOutput implements DataOutput, Closeable, Flushable {

    private final DataOutput origin;
    private final String prefix;

    public DebugDataOutput(DataOutput origin, String prefix) {
        this.origin = origin;
        this.prefix = prefix;
    }

    @Override
    public void write(int b) throws IOException {
        System.out.println("debug(" + prefix + "): write int (" + b + ")");
        origin.write(b);
    }

    @Override
    public void write(byte[] b) throws IOException {
        System.out.println("debug(" + prefix + "): write byte[] (" + b.length + ")");
        origin.write(b);
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        System.out.println("debug(" + prefix + "): write byte[] (" + len + ")");
        origin.write(b, off, len);
    }

    @Override
    public void writeBoolean(boolean v) throws IOException {
        System.out.println("debug(" + prefix + "): write boolean (" + v + ")");
        origin.writeBoolean(v);
    }

    @Override
    public void writeByte(int v) throws IOException {
        System.out.println("debug(" + prefix + "): write byte (" + v + ")");
        origin.writeByte(v);
    }

    @Override
    public void writeShort(int v) throws IOException {
        System.out.println("debug(" + prefix + "): write short (" + v + ")");
        origin.writeShort(v);
    }

    @Override
    public void writeChar(int v) throws IOException {
        System.out.println("debug(" + prefix + "): write char (" + v + ")");
        origin.writeChar(v);
    }

    @Override
    public void writeInt(int v) throws IOException {
        System.out.println("debug(" + prefix + "): write int (" + v + ")");
        origin.writeInt(v);
    }

    @Override
    public void writeLong(long v) throws IOException {
        System.out.println("debug(" + prefix + "): write long (" + v + ")");
        origin.writeLong(v);
    }

    @Override
    public void writeFloat(float v) throws IOException {
        System.out.println("debug(" + prefix + "): write float (" + v + ")");
        origin.writeFloat(v);
    }

    @Override
    public void writeDouble(double v) throws IOException {
        System.out.println("debug(" + prefix + "): write double (" + v + ")");
        origin.writeDouble(v);
    }

    @Override
    public void writeBytes(String s) throws IOException {
        System.out.println("debug(" + prefix + "): write bytes (" + s + ")");
        origin.writeBytes(s);
    }

    @Override
    public void writeChars(String s) throws IOException {
        System.out.println("debug(" + prefix + "): write chars (" + s + ")");
        origin.writeChars(s);
    }

    @Override
    public void writeUTF(String s) throws IOException {
        System.out.println("debug(" + prefix + "): write UTF (" + s + ")");
        origin.writeUTF(s);
    }

    public static DebugDataOutput from(DataOutput origin) {
        return new DebugDataOutput(origin, "");
    }

    public static DebugDataOutput from(DataOutput origin, String prefix) {
        return new DebugDataOutput(origin, prefix);
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
}
