package com.nukkitx.jackson.dataformat.nbt.generator;

import com.nukkitx.nbt.CompoundTagBuilder;
import com.nukkitx.nbt.tag.Tag;

public class CompoundTagWriter extends NBTWriter<Tag<?>> {

    protected CompoundTagBuilder builder = CompoundTagBuilder.builder();

    public CompoundTagWriter(String name) {
        super(name);
    }

    @Override
    public void write(Object tag) {
        builder.tag((Tag) tag);
    }

    @Override
    public Tag<?> getTag() {
        return builder.build(name);
    }
}
