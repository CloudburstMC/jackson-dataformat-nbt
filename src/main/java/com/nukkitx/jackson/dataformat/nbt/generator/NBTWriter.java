package com.nukkitx.jackson.dataformat.nbt.generator;

import com.nukkitx.nbt.tag.Tag;

public abstract class NBTWriter<T> {

    protected String name;

    public NBTWriter(String name) {
        this.name = name;
    }

    public abstract void write(Object tag);

    public abstract Tag<?> getTag();

    public String getName() {
        return name;
    }
}
