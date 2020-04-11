package com.nukkitx.jackson.dataformat.nbt.generator;

import com.nukkitx.nbt.tag.ListTag;
import com.nukkitx.nbt.tag.Tag;

import java.util.LinkedList;
import java.util.List;

public class ListTagWriter<T extends Tag<T>> extends NBTWriter<T> {

    protected final Class<T> typeClass;
    protected final List<T> values = new LinkedList<>();

    public ListTagWriter(String name, Class<T> type) {
        super(name);
        this.typeClass = type;
    }

    @Override
    public void write(Tag<?> value) {
        if (typeClass != value.getClass()) {
            throw new IllegalArgumentException("Expected " + typeClass + " got " + value.getClass());
        }

        values.add((T) value);
    }

    @Override
    public Tag<?> getTag() {
        return new ListTag<>(name, typeClass, values);
    }
}
