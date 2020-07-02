package com.nukkitx.jackson.dataformat.nbt;

import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.base.ParserBase;
import com.fasterxml.jackson.core.io.IOContext;
import com.fasterxml.jackson.core.util.BufferRecycler;
import com.nukkitx.jackson.dataformat.nbt.parser.CompoundTagReader;
import com.nukkitx.jackson.dataformat.nbt.parser.NBTReader;
import com.nukkitx.jackson.dataformat.nbt.util.IOUtils;

import java.io.Closeable;
import java.io.DataInput;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class NBTParser extends ParserBase {

    /**
     * Codec used for data binding when (if) requested.
     */
    protected ObjectCodec _objectCodec;

    protected int _formatFeatures;

    protected DataInput _in;

    protected NBTReader reader;

    protected boolean end = false;

    public NBTParser(IOContext context, BufferRecycler nr, int parserFeatures, int nbtFeatures, ObjectCodec codec, InputStream in) throws IOException {
        super(context, parserFeatures);
        this._objectCodec = codec;
        this._formatFeatures = nbtFeatures;

        if (NBTFactory.Feature.GZIP.enabledIn(nbtFeatures)) {
            in = IOUtils.createGZIPReader(in);
        }

        if (NBTFactory.Feature.LITTLE_ENDIAN.enabledIn(nbtFeatures)) {
            _in = IOUtils.createReaderLE(in);
        } else if (NBTFactory.Feature.NETWORK.enabledIn(nbtFeatures)) {
            _in = IOUtils.createNetworkReader(in);
        } else {
            _in = IOUtils.createReader(in);
        }
    }

    @Override
    public boolean isExpectedStartObjectToken() {
        return super.isExpectedStartObjectToken();
    }

    protected void _closeInput() throws IOException {
        if (_in instanceof Closeable) {
            ((Closeable) _in).close();
        }
    }

    public JsonToken nextToken() throws IOException {
        if (end) {
            return null;
        }

        if (reader == null) {
            reader = NBTReader.getRoot(_in);
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
            reader = new CompoundTagReader(_in, reader);
        } else if (next == JsonToken.START_ARRAY) {
            reader = NBTReader.getByType(reader.getCurrentType(), reader, _in);
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
