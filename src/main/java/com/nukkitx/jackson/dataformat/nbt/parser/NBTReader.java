package com.nukkitx.jackson.dataformat.nbt.parser;

import com.fasterxml.jackson.core.JsonToken;
import com.nukkitx.jackson.dataformat.nbt.parser.PrimitiveArrayReader.ByteArrayReader;
import com.nukkitx.jackson.dataformat.nbt.parser.PrimitiveArrayReader.IntArrayReader;
import com.nukkitx.jackson.dataformat.nbt.parser.PrimitiveArrayReader.LongArrayReader;
import com.nukkitx.nbt.tag.*;

import java.util.List;
import java.util.Map;

public abstract class NBTReader {

    protected NBTReader parent;

    protected String stringValue;
    protected byte[] dataValue;
    protected byte byteValue;
    protected short shortValue;
    protected int intValue;
    protected long longValue;
    protected float floatValue;
    protected double doubleValue;

    protected int[] intArrayValue;
    protected long[] longArrayValue;

    protected Map<String, Tag<?>> compoundValue;
    protected List<Tag<?>> listValue;

    protected String currentName;
    protected Object currentValue;

    public NBTReader(NBTReader parent) {
        this.parent = parent;

        if (parent != null) {
            this.currentName = parent.currentName;
        }
    }

    public abstract JsonToken get();

    public abstract JsonToken start();

    public abstract JsonToken end();

    public NBTReader getParent() {
        return parent;
    }

    protected JsonToken decodeValue(Tag<?> tag) {
        if (tag instanceof StringTag) {
            currentValue = stringValue = ((StringTag) tag).getValue();
            return JsonToken.VALUE_STRING;
        }

        if (tag instanceof ByteTag) {
            currentValue = byteValue = ((ByteTag) tag).getPrimitiveValue();
            return JsonToken.VALUE_NUMBER_INT;
        }

        if (tag instanceof ShortTag) {
            currentValue = shortValue = ((ShortTag) tag).getPrimitiveValue();
            return JsonToken.VALUE_NUMBER_INT;
        }

        if (tag instanceof IntTag) {
            currentValue = intValue = ((IntTag) tag).getPrimitiveValue();
            return JsonToken.VALUE_NUMBER_INT;
        }

        if (tag instanceof LongTag) {
            currentValue = longValue = ((LongTag) tag).getPrimitiveValue();
            return JsonToken.VALUE_NUMBER_INT;
        }

        if (tag instanceof FloatTag) {
            currentValue = floatValue = ((FloatTag) tag).getPrimitiveValue();
            return JsonToken.VALUE_NUMBER_FLOAT;
        }

        if (tag instanceof DoubleTag) {
            currentValue = doubleValue = ((DoubleTag) tag).getPrimitiveValue();
            return JsonToken.VALUE_NUMBER_FLOAT;
        }

        if (tag instanceof ByteArrayTag) {
            currentValue = dataValue = ((ByteArrayTag) tag).getValue();
            return JsonToken.START_ARRAY;
        }

        if (tag instanceof IntArrayTag) {
            currentValue = intArrayValue = ((IntArrayTag) tag).getValue();
            return JsonToken.START_ARRAY;
        }

        if (tag instanceof LongArrayTag) {
            currentValue = longArrayValue = ((LongArrayTag) tag).getValue();
            return JsonToken.START_ARRAY;
        }

        if (tag instanceof CompoundTag) {
            currentValue = compoundValue = ((CompoundTag) tag).getValue();
            return JsonToken.START_OBJECT;
        }

        if (tag instanceof ListTag) {
            currentValue = listValue = ((ListTag) tag).getValue();
            return JsonToken.START_ARRAY;
        }

        currentValue = null;
        return null;
    }

    public String getStringValue() {
        return stringValue;
    }

    public byte[] getDataValue() {
        return dataValue;
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

    public int[] getIntArrayValue() {
        return intArrayValue;
    }

    public long[] getLongArrayValue() {
        return longArrayValue;
    }

    public Map<String, Tag<?>> getCompoundValue() {
        return compoundValue;
    }

    public List<Tag<?>> getListValue() {
        return listValue;
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

    public static NBTReader getByTag(Tag<?> tag, NBTReader parent) {
        NBTReader reader = getByValue(tag.getValue(), parent);

        if (reader == null) {
            return new SingleTagReader(tag);
        }

        return reader;
    }

    public static NBTReader getByValue(Object value, NBTReader parent) {
        if (value instanceof Map) {
            return new CompoundTagReader((Map) value, parent);
        }

        if (value instanceof List) {
            return new ListTagReader((List) value, parent);
        }

        if (value instanceof byte[]) {
            return new ByteArrayReader((byte[]) value, parent);
        }

        if (value instanceof int[]) {
            return new IntArrayReader((int[]) value, parent);
        }

        if (value instanceof long[]) {
            return new LongArrayReader((long[]) value, parent);
        }

        return null;
    }
}
