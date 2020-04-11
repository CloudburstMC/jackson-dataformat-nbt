package com.nukkitx.jackson.dataformat.nbt.generator;

import com.nukkitx.nbt.tag.Tag;

public class SingleTagWriter extends NBTWriter<Tag<?>> {

    protected Tag<?> tag;

    public SingleTagWriter(String name) {
        super(name);
    }

    @Override
    public void write(Tag<?> tag) {
        this.tag = tag;
        end();
    }

    @Override
    public Tag<?> getTag() {
        return tag;
    }
}
