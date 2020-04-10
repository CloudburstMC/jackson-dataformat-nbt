package com.nukkitx.jackson.dataformat.nbt.parser;

import com.fasterxml.jackson.core.JsonToken;
import com.nukkitx.nbt.tag.Tag;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class CompoundTagReader extends NBTReader {

    protected Iterator<Entry<String, Tag<?>>> values;

    protected Tag<?> value;

    public CompoundTagReader(Map<String, Tag<?>> value, NBTReader parent) {
        super(parent);
        this.values = value.entrySet().iterator();
    }

    @Override
    public JsonToken get() {
        if (value != null) {
            JsonToken r = decodeValue(value);
            value = null;
            return r;
        }

        if (values.hasNext()) {
            Entry<String, Tag<?>> current = values.next();

            currentName = current.getKey();
            value = current.getValue();

            return JsonToken.FIELD_NAME;
        }

        return null;
    }

    @Override
    public JsonToken start() {
        return JsonToken.START_OBJECT;
    }

    @Override
    public JsonToken end() {
        return JsonToken.END_OBJECT;
    }
}
