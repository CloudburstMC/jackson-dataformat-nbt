package com.nukkitx.jackson.dataformat.nbt;

import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.base.ParserBase;
import com.fasterxml.jackson.core.io.IOContext;
import com.fasterxml.jackson.core.util.BufferRecycler;
import com.nukkitx.jackson.dataformat.nbt.parser.CompoundTagReader;
import com.nukkitx.jackson.dataformat.nbt.parser.NBTReader;
import com.nukkitx.nbt.NbtUtils;
import com.nukkitx.nbt.stream.NBTInputStream;
import com.nukkitx.nbt.tag.Tag;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class NBTParser extends ParserBase {

    /**
     * Codec used for data binding when (if) requested.
     */
    protected ObjectCodec _objectCodec;

    protected int _formatFeatures;

    protected Tag<?> nbt;

    protected NBTReader reader;

    protected boolean end = false;

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
            nbt = nbtIn.readTag();
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
        if (end) {
            return null;
        }

        if (reader == null) {
            reader = NBTReader.getByTag(nbt, null);
            return (_currToken = reader.start());
        }

        JsonToken next = reader.get();

        if (next == null) {
            JsonToken end = reader.end();
            reader = reader.getParent();

            if (reader == null) {
                this.end = true;
            }

            return (_currToken = end);
        }

        if (next == JsonToken.START_OBJECT) {
            reader = new CompoundTagReader(reader.getCompoundValue(), reader);
        } else if (next == JsonToken.START_ARRAY) {
            Object value = reader.getCurrentValue();

            reader = NBTReader.getByValue(value, reader);
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
