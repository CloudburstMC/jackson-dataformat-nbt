package com.nukkitx.jackson.dataformat.nbt;

import com.fasterxml.jackson.core.Base64Variant;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.PrettyPrinter;
import com.fasterxml.jackson.core.base.GeneratorBase;
import com.fasterxml.jackson.core.io.IOContext;
import com.fasterxml.jackson.core.json.JsonWriteContext;
import com.nukkitx.jackson.dataformat.nbt.generator.NBTWriteContext;
import com.nukkitx.jackson.dataformat.nbt.util.IOUtils;
import com.nukkitx.nbt.NbtType;

import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class NBTGenerator extends GeneratorBase {

    protected int _formatFeatures;

    protected DataOutput _out;

    final protected IOContext _ioContext;

    protected NBTWriteContext rootContext;

    public NBTGenerator(IOContext context, int jsonFeatures, int nbtFeatures, ObjectCodec codec, OutputStream out) throws IOException {
        super(jsonFeatures, codec);
        _ioContext = context;
        _formatFeatures = nbtFeatures;
        _out = wrapStream(out);
        _writeContext = new NBTWriteContext(this, 0, null, _writeContext.getDupDetector(), null, _out);
    }

    @Override
    public NBTGenerator useDefaultPrettyPrinter() {
        return this;
    }

    @Override
    public NBTGenerator setPrettyPrinter(PrettyPrinter pp) {
        return this;
    }

    public void flush() throws IOException {
        if (_out instanceof Flushable) {
            ((Flushable) _out).flush();
        }
    }

    protected void _releaseBuffers() {
        //nothing
    }

    @Override
    public void close() throws IOException {
        if (isClosed()) {
            return;
        }

        super.close();
        flush();
        if (_out instanceof Closeable) {
            ((Closeable) _out).close();
        }
    }

    protected void _verifyValueWrite(String typeMsg) throws IOException {
        int status = _writeContext.writeValue();
        if (status == JsonWriteContext.STATUS_EXPECT_NAME) {
            _reportError("Can not " + typeMsg + ", expecting field name");
        }
    }

    public void writeStartArray() throws IOException {
        _verifyValueWrite("start an array");

        getOutputContext().writeValue(NbtType.LIST, null);
        _writeContext = _writeContext.createChildArrayContext();

        checkRootContext();
    }

    public void writeEndArray() throws IOException {
        if (!_writeContext.inArray()) {
            _reportError("Current context not Array but " + _writeContext.typeDesc());
        }

        getOutputContext().end();
        _writeContext = _writeContext.getParent();
    }

    public void writeStartObject() throws IOException {
        _verifyValueWrite("start an object");

        getOutputContext().writeValue(NbtType.COMPOUND, null);
        _writeContext = _writeContext.createChildObjectContext();

        checkRootContext();
    }

    public void writeEndObject() throws IOException {
        if (!_writeContext.inObject()) {
            _reportError("Current context not Object but " + _writeContext.typeDesc());
        }

        getOutputContext().end();
        _writeContext = _writeContext.getParent();
    }

    public void writeFieldName(String name) throws IOException {
        if (_writeContext.writeFieldName(name) == JsonWriteContext.STATUS_EXPECT_VALUE) {
            _reportError("Can not write a field name, expecting a value");
        }
    }

    public void writeString(String text) throws IOException {
        _verifyValueWrite("write String value");
        checkRootContext();

        getOutputContext().writeValue(NbtType.STRING, text);
        getOutputContext().getOutput().writeUTF(text);
    }

    public void writeString(char[] text, int offset, int len) throws IOException {
        writeString(new String(text, offset, len));
    }

    public void writeRawUTF8String(byte[] text, int offset, int length) throws IOException {
        writeString(new String(text, offset, length));
    }

    public void writeUTF8String(byte[] text, int offset, int length) throws IOException {
        writeString(new String(text, offset, length));
    }

    public void writeRaw(String text) throws IOException {
        writeString(text);
    }

    public void writeRaw(String text, int offset, int len) throws IOException {
        writeString(new String(text.toCharArray(), offset, len));
    }

    public void writeRaw(char[] text, int offset, int len) throws IOException {
        writeString(new String(text, offset, len));
    }

    public void writeRaw(char c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void writeBinary(byte[] data) throws IOException {
        _verifyValueWrite("write byte[] value");
        checkRootContext();

        getOutputContext().writeValue(NbtType.BYTE_ARRAY, data);
        getOutputContext().getOutput().writeInt(data.length);
        getOutputContext().getOutput().write(data);
    }

    @Override
    public void writeBinary(byte[] data, int offset, int len) throws IOException {
        writeBinary(Arrays.copyOfRange(data, offset, offset + len));
    }

    public void writeBinary(Base64Variant bv, byte[] data, int offset, int len) throws IOException {
        writeBinary(data, offset, len);
    }

    public void writeNumber(int v) throws IOException {
        _verifyValueWrite("write int value");
        checkRootContext();

        getOutputContext().writeValue(NbtType.INT, v);
        getOutputContext().getOutput().writeInt(v);
    }

    public void writeNumber(long v) throws IOException {
        _verifyValueWrite("write long value");
        checkRootContext();

        getOutputContext().writeValue(NbtType.LONG, v);
        getOutputContext().getOutput().writeLong(v);
    }

    public void writeNumber(BigInteger v) {
        throw new UnsupportedOperationException();
    }

    public void writeNumber(double v) throws IOException {
        _verifyValueWrite("write double value");
        checkRootContext();

        getOutputContext().writeValue(NbtType.DOUBLE, v);
        getOutputContext().getOutput().writeDouble(v);
    }

    public void writeNumber(float v) throws IOException {
        _verifyValueWrite("write float value");
        checkRootContext();

        getOutputContext().writeValue(NbtType.FLOAT, v);
        getOutputContext().getOutput().writeFloat(v);
    }

    @Override
    public void writeNumber(short v) throws IOException {
        _verifyValueWrite("write short value");
        checkRootContext();

        getOutputContext().writeValue(NbtType.SHORT, v);
        getOutputContext().getOutput().writeShort(v);
    }

    public void writeNumber(byte v) throws IOException {
        _verifyValueWrite("write byte value");
        checkRootContext();

        getOutputContext().writeValue(NbtType.BYTE, v);
        getOutputContext().getOutput().writeByte(v);
    }

    public void writeNumber(BigDecimal v) {
        throw new UnsupportedOperationException();
    }

    public void writeNumber(String encodedValue) {
        throw new UnsupportedOperationException();
    }

    public void writeBoolean(boolean state) throws IOException {
        writeNumber((byte) (state ? 1 : 0));
    }

    public void writeNull() {
        //Do nothing here
    }

    @Override
    public void writeArray(int[] array, int offset, int length) throws IOException {
        _verifyValueWrite("write int[] value");
        checkRootContext();

        array = Arrays.copyOfRange(array, offset, offset + length);
        getOutputContext().writeValue(NbtType.INT_ARRAY, array);
        getOutputContext().getOutput().writeInt(length);
        for (int v : array) {
            getOutputContext().getOutput().writeInt(v);
        }
    }

    @Override
    public void writeArray(long[] array, int offset, int length) throws IOException {
        _verifyValueWrite("write long[] value");
        checkRootContext();

        array = Arrays.copyOfRange(array, offset, offset + length);
        getOutputContext().writeValue(NbtType.LONG_ARRAY, array);
        getOutputContext().getOutput().writeInt(length);
        for (long v : Arrays.copyOfRange(array, offset, offset + length)) {
            getOutputContext().getOutput().writeLong(v);
        }
    }

    @Override
    public NBTWriteContext getOutputContext() {
        return (NBTWriteContext) super.getOutputContext();
    }

    protected void _writeSimpleObject(Object value) throws IOException {
        Thread.dumpStack();
        /* 31-Dec-2009, tatu: Actually, we could just handle some basic
         *    types even without codec. This can improve interoperability,
         *    and specifically help with TokenBuffer.
         */
        if (value == null) {
            writeNull();
            return;
        }
        if (value instanceof String) {
            writeString((String) value);
            return;
        }
        if (value instanceof Number) {
            Number n = (Number) value;
            if (n instanceof Integer) {
                writeNumber(n.intValue());
                return;
            } else if (n instanceof Long) {
                writeNumber(n.longValue());
                return;
            } else if (n instanceof Double) {
                writeNumber(n.doubleValue());
                return;
            } else if (n instanceof Float) {
                writeNumber(n.floatValue());
                return;
            } else if (n instanceof Short) {
                writeNumber(n.shortValue());
                return;
            } else if (n instanceof Byte) {
                writeNumber(n.byteValue());
                return;
            } else if (n instanceof BigInteger) {
                writeNumber((BigInteger) n);
                return;
            } else if (n instanceof BigDecimal) {
                writeNumber((BigDecimal) n);
                return;

                // then Atomic types
            } else if (n instanceof AtomicInteger) {
                writeNumber(((AtomicInteger) n).get());
                return;
            } else if (n instanceof AtomicLong) {
                writeNumber(((AtomicLong) n).get());
                return;
            }
        } else if (value instanceof byte[]) {
            writeBinary((byte[]) value);
            return;
        } else if (value instanceof Boolean) {
            writeBoolean((Boolean) value);
            return;
        } else if (value instanceof AtomicBoolean) {
            writeBoolean(((AtomicBoolean) value).get());
            return;
        }
        throw new IllegalStateException("No ObjectCodec defined for the generator, can only serialize simple wrapper types (type passed "
                + value.getClass().getName() + ")");
    }

    protected void checkRootContext() {
        if (rootContext == null) {
            rootContext = (NBTWriteContext) _writeContext;
        }
    }

    public DataOutput wrapStream(OutputStream stream) throws IOException {
        if (NBTFactory.Feature.GZIP.enabledIn(_formatFeatures)) {
            stream = IOUtils.createGZIPWriter(stream);
        }

        DataOutput out;

        if (NBTFactory.Feature.LITTLE_ENDIAN.enabledIn(_formatFeatures)) {
            out = IOUtils.createWriterLE(stream);
        } else if (NBTFactory.Feature.NETWORK.enabledIn(_formatFeatures)) {
            out = IOUtils.createNetworkWriter(stream);
        } else {
            out = IOUtils.createWriter(stream);
        }

        return out;
    }
}
