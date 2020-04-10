package com.nukkitx.jackson.dataformat.nbt.parser;

import com.fasterxml.jackson.core.JsonToken;

public abstract class PrimitiveArrayReader extends ArrayReader {

    protected int index = 0;
    protected final int length;

    public PrimitiveArrayReader(NBTReader parent, int length) {
        super(parent);
        this.length = length;
    }

    @Override
    public JsonToken get() {
        if (index++ >= length) {
            return null;
        }

        loadValue();
        return JsonToken.VALUE_NUMBER_INT;
    }

    abstract void loadValue();

    public static class ByteArrayReader extends PrimitiveArrayReader {

        protected final byte[] data;

        public ByteArrayReader(byte[] data, NBTReader parent) {
            super(parent, data.length);
            this.data = data;
        }

        @Override
        void loadValue() {
            this.currentValue = this.byteValue = data[index];
        }
    }

    public static class IntArrayReader extends PrimitiveArrayReader {

        protected final int[] data;

        public IntArrayReader(int[] data, NBTReader parent) {
            super(parent, data.length);
            this.data = data;
        }

        @Override
        void loadValue() {
            this.currentValue = this.intValue = data[index];
        }
    }

    public static class LongArrayReader extends PrimitiveArrayReader {

        protected final long[] data;

        public LongArrayReader(long[] data, NBTReader parent) {
            super(parent, data.length);
            this.data = data;
        }

        @Override
        void loadValue() {
            this.currentValue = this.longValue = data[index];
        }
    }
}
