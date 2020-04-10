package com.nukkitx.jackson.dataformat.nbt.parser;

import com.fasterxml.jackson.core.JsonToken;
import com.nukkitx.nbt.tag.Tag;

import java.util.Iterator;
import java.util.List;

public class ListTagReader extends ArrayReader {

    protected Iterator<Tag<?>> iterator;

    public ListTagReader(List<Tag<?>> list, NBTReader parent) {
        super(parent);
        this.iterator = list.iterator();
    }

    @Override
    public JsonToken get() {
        if (iterator.hasNext()) {
            return decodeValue(iterator.next());
        }

        return null;
    }
}
