package com.nukkitx.jackson.dataformat.nbt.generator;

import com.nukkitx.nbt.tag.Tag;

public abstract class NBTWriter<T> {

    protected String name;

    protected boolean end = false;

    public NBTWriter(String name) {
        this.name = name;
    }

    public abstract void write(Tag<?> tag);

    public abstract Tag<?> getTag();

    public String getName() {
        return name;
    }

    public void end() {
        this.end = true;
    }

    public boolean isEnded() {
        return end;
    }
}
