package com.nukkitx.jackson.dataformat.nbt;

import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.base.ParserBase;
import com.fasterxml.jackson.core.io.IOContext;
import com.fasterxml.jackson.core.util.BufferRecycler;
import com.nukkitx.jackson.dataformat.nbt.parser.CompoundTagReader;
import com.nukkitx.jackson.dataformat.nbt.parser.ListTagReader;
import com.nukkitx.jackson.dataformat.nbt.parser.NBTReader;
import com.nukkitx.jackson.dataformat.nbt.parser.PrimitiveArrayReader.ByteArrayReader;
import com.nukkitx.jackson.dataformat.nbt.parser.PrimitiveArrayReader.IntArrayReader;
import com.nukkitx.jackson.dataformat.nbt.parser.PrimitiveArrayReader.LongArrayReader;
import com.nukkitx.nbt.NbtUtils;
import com.nukkitx.nbt.stream.NBTInputStream;
import com.nukkitx.nbt.tag.CompoundTag;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;

public class NBTParser extends ParserBase {

    /**
     * Codec used for data binding when (if) requested.
     */
    protected ObjectCodec _objectCodec;

    protected int _formatFeatures;

    protected CompoundTag nbt;

    protected NBTReader reader;

    public NBTParser(IOContext context, BufferRecycler nr, int parserFeatures, int nbtFeatures, ObjectCodec codec, InputStream in) {
        super(context, parserFeatures);
        this._objectCodec = codec;
        this._formatFeatures = nbtFeatures;

        NBTInputStream nbtIn;
        if (NBTFactory.Feature.GZIP.enabledIn(nbtFeatures)) {
            nbtIn = NbtUtils.createReaderLE(in);
        } else if (NBTFactory.Feature.LITTLE_ENDIAN.enabledIn(nbtFeatures)) {
            nbtIn = NbtUtils.createReaderLE(in);
        } else if (NBTFactory.Feature.NETWORK.enabledIn(nbtFeatures)) {
            nbtIn = NbtUtils.createNetworkReader(in);
        } else {
            nbtIn = NbtUtils.createReader(in);
        }

        try {
            nbt = (CompoundTag) nbtIn.readTag();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                nbtIn.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public boolean isExpectedStartObjectToken() {
        return super.isExpectedStartObjectToken();
    }

    protected void _closeInput() {
        //nothing to close?
    }

    public JsonToken nextToken() {
        if (reader == null) {
            reader = new CompoundTagReader(nbt.getValue(), null);
            return (_currToken = JsonToken.START_OBJECT);
        }

        JsonToken next = reader.get();

        if (next == null) {
            JsonToken end = reader.end();
            reader = reader.getParent();

            if (reader == null) {
                return (_currToken = null);
            }

            return (_currToken = end);
        }

        if (next == JsonToken.START_OBJECT) {
            reader = new CompoundTagReader(reader.getCompoundValue(), reader);
        } else if (next == JsonToken.START_ARRAY) {
            Object value = reader.getCurrentValue();

            if (value instanceof List) {
                reader = new ListTagReader((List) value, reader);
            } else if (value instanceof byte[]) {
                reader = new ByteArrayReader((byte[]) value, reader);
            } else if (value instanceof int[]) {
                reader = new IntArrayReader((int[]) value, reader);
            } else if (value instanceof long[]) {
                reader = new LongArrayReader((long[]) value, reader);
            }
        }

        return (_currToken = next);
    }

    @Override
    public String getCurrentName() {
        return reader.getCurrentName();
    }

    public String getText() {
        return Objects.toString(reader.getCurrentValue());
    }

    public char[] getTextCharacters() {
        String text = getText();
        return (text == null) ? null : text.toCharArray();
    }

    public int getTextLength() {
        String text = getText();
        return (text == null) ? 0 : text.length();
    }

    public int getTextOffset() {
        return 0;
    }

    public ObjectCodec getCodec() {
        return _objectCodec;
    }

    public void setCodec(ObjectCodec c) {
        this._objectCodec = c;
    }

    @Override
    public int getIntValue() {
        return reader.getIntValue();
    }

    @Override
    public long getLongValue() {
        return reader.getLongValue();
    }

    @Override
    public short getShortValue() {
        return reader.getShortValue();
    }

    @Override
    public byte getByteValue() {
        return reader.getByteValue();
    }

    @Override
    public double getDoubleValue() {
        return reader.getDoubleValue();
    }

    @Override
    public float getFloatValue() {
        return reader.getFloatValue();
    }

    @Override
    public boolean getBooleanValue() {
        return reader.getBooleanValue();
    }
}
