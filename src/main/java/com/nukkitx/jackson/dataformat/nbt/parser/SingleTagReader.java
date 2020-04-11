package com.nukkitx.jackson.dataformat.nbt.parser;

import com.fasterxml.jackson.core.JsonToken;
import com.nukkitx.nbt.tag.Tag;

public class SingleTagReader extends NBTReader {

    protected Tag<?> tag;

    public SingleTagReader(Tag<?> tag) {
        super(null);
        this.tag = tag;
    }

    @Override
    public JsonToken get() {
        return null;
    }

    @Override
    public JsonToken start() {
        return decodeValue(tag);
    }

    @Override
    public JsonToken end() {
        return null;
    }
}
