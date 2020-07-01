package com.nukkitx.jackson.dataformat.nbt.parser;

import com.fasterxml.jackson.core.JsonToken;

import java.io.DataInput;
import java.io.IOException;

public abstract class PrimitiveArrayReader extends ArrayReader {

    public PrimitiveArrayReader(DataInput input, NBTReader parent) throws IOException {
        super(input, parent);
        this.token = JsonToken.VALUE_NUMBER_INT;
        this.length = input.readInt();
    }

    public static class ByteArrayReader extends PrimitiveArrayReader {

        public ByteArrayReader(DataInput input, NBTReader parent) throws IOException {
            super(input, parent);
        }

        @Override
        void readValue() throws IOException {
            this.currentValue = this.byteValue = input.readByte();
        }
    }

    public static class IntArrayReader extends PrimitiveArrayReader {

        public IntArrayReader(DataInput input, NBTReader parent) throws IOException {
            super(input, parent);
        }

        @Override
        void readValue() throws IOException {
            this.currentValue = this.intValue = input.readInt();
        }
    }

    public static class LongArrayReader extends PrimitiveArrayReader {

        public LongArrayReader(DataInput input, NBTReader parent) throws IOException {
            super(input, parent);
        }

        @Override
        void readValue() throws IOException {
            this.currentValue = this.longValue = input.readLong();
        }
    }
}
