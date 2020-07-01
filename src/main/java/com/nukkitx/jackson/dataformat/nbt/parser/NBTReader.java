package com.nukkitx.jackson.dataformat.nbt.parser;

import com.fasterxml.jackson.core.JsonToken;
import com.nukkitx.jackson.dataformat.nbt.parser.PrimitiveArrayReader.ByteArrayReader;
import com.nukkitx.jackson.dataformat.nbt.parser.PrimitiveArrayReader.IntArrayReader;
import com.nukkitx.jackson.dataformat.nbt.parser.PrimitiveArrayReader.LongArrayReader;
import com.nukkitx.nbt.NbtType;

import java.io.DataInput;
import java.io.IOException;

public abstract class NBTReader {

    protected final DataInput input;
    protected final NBTReader parent;

    protected String stringValue;
    protected byte byteValue;
    protected short shortValue;
    protected int intValue;
    protected long longValue;
    protected float floatValue;
    protected double doubleValue;

    protected String currentName;
    protected Object currentValue;
    protected NbtType<?> currentType;

    public NBTReader(DataInput input, NBTReader parent) {
        this.input = input;
        this.parent = parent;

        if (parent != null) {
            this.currentName = parent.currentName;
        }
    }

    public abstract JsonToken get() throws IOException;

    public abstract JsonToken start() throws IOException;

    public abstract JsonToken end();

    public NBTReader getParent() {
        return parent;
    }

    protected JsonToken decodeValue(NbtType<?> type) throws IOException {
        currentType = type;

        if (type == NbtType.STRING) {
            currentValue = stringValue = input.readUTF();
            System.out.println("read value string");
            return JsonToken.VALUE_STRING;
        }

        if (type == NbtType.BYTE) {
            currentValue = byteValue = input.readByte();
            return JsonToken.VALUE_NUMBER_INT;
        }

        if (type == NbtType.SHORT) {
            currentValue = shortValue = input.readShort();
            return JsonToken.VALUE_NUMBER_INT;
        }

        if (type == NbtType.INT) {
            currentValue = intValue = input.readInt();
            return JsonToken.VALUE_NUMBER_INT;
        }

        if (type == NbtType.LONG) {
            currentValue = longValue = input.readLong();
            return JsonToken.VALUE_NUMBER_INT;
        }

        if (type == NbtType.FLOAT) {
            currentValue = floatValue = input.readFloat();
            return JsonToken.VALUE_NUMBER_FLOAT;
        }

        if (type == NbtType.DOUBLE) {
            currentValue = doubleValue = input.readDouble();
            return JsonToken.VALUE_NUMBER_FLOAT;
        }

        if (type == NbtType.BYTE_ARRAY) {
            currentValue = null;
            return JsonToken.START_ARRAY;
        }

        if (type == NbtType.INT_ARRAY) {
            currentValue = null;
            return JsonToken.START_ARRAY;
        }

        if (type == NbtType.LONG_ARRAY) {
            currentValue = null;
            return JsonToken.START_ARRAY;
        }

        if (type == NbtType.COMPOUND) {
            currentValue = null;
            return JsonToken.START_OBJECT;
        }

        if (type == NbtType.LIST) {
            currentValue = null;
            return JsonToken.START_ARRAY;
        }

        currentValue = null;
        return null;
    }

    public String getStringValue() {
        return stringValue;
    }

    public byte getByteValue() {
        return byteValue;
    }

    public short getShortValue() {
        return shortValue;
    }

    public int getIntValue() {
        return intValue;
    }

    public long getLongValue() {
        return longValue;
    }

    public float getFloatValue() {
        return floatValue;
    }

    public double getDoubleValue() {
        return doubleValue;
    }

    public Object getCurrentValue() {
        return currentValue;
    }

    public String getCurrentName() {
        return currentName;
    }

    public boolean getBooleanValue() {
        return byteValue != 0;
    }

    public boolean isRoot() {
        return parent == null;
    }

    public NbtType<?> getCurrentType() {
        return currentType;
    }

    public static NBTReader getRoot(DataInput input) throws IOException {
        NbtType<?> type = NbtType.byId(input.readUnsignedByte());
        input.readUTF(); // root tag name
        System.out.println("read root tag: " + type.getTypeName());

        NBTReader reader = getByType(type, null, input);

        if (reader == null) {
            return new SingleTagReader(input, type);
        }

        return reader;
    }

    public static NBTReader getByType(NbtType<?> type, NBTReader parent, DataInput input) throws IOException {
        if (type == NbtType.COMPOUND) {
            return new CompoundTagReader(input, parent);
        }

        if (type == NbtType.LIST) {
            return new ListTagReader(input, parent);
        }

        if (type == NbtType.BYTE_ARRAY) {
            return new ByteArrayReader(input, parent);
        }

        if (type == NbtType.INT_ARRAY) {
            return new IntArrayReader(input, parent);
        }

        if (type == NbtType.LONG_ARRAY) {
            return new LongArrayReader(input, parent);
        }

        return null;
    }
}
