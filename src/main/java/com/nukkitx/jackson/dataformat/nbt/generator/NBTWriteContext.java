package com.nukkitx.jackson.dataformat.nbt.generator;

import com.fasterxml.jackson.core.json.DupDetector;
import com.fasterxml.jackson.core.json.JsonWriteContext;
import com.nukkitx.nbt.tag.Tag;

public class NBTWriteContext extends JsonWriteContext {

    protected String name;
    protected NBTWriter<?> writer;
    protected boolean ended = false;

    public NBTWriteContext(int type, NBTWriteContext parent, DupDetector dups, String name) {
        super(type, parent, dups);
        this.name = name;

        if (!inArray()) {
            writer = new CompoundTagWriter(name);
        }
    }

    @SuppressWarnings("unchecked")
    private void initListType(Object value) {
        Class<?> objType = value.getClass();
        writer = new ListTagWriter(name, objType);
    }

    public NBTWriteContext createChildArrayContext() {
        return createOrReset(TYPE_ARRAY);
    }

    @Override
    public NBTWriteContext createChildArrayContext(Object currValue) {
        throw new UnsupportedOperationException();
    }

    @Override
    public NBTWriteContext createChildObjectContext() {
        return createOrReset(TYPE_OBJECT);
    }

    @Override
    public NBTWriteContext createChildObjectContext(Object currValue) {
        throw new UnsupportedOperationException();
    }

    protected NBTWriteContext reset(int type, String name) {
        super.reset(type);
        this.name = name;

        if (!inArray()) {
            writer = new CompoundTagWriter(name);
        } else {
            writer = null;
        }

        ended = false;
        return this;
    }

    @Override
    protected NBTWriteContext reset(int type, Object currValue) {
        throw new UnsupportedOperationException();
    }

    public void writeValue(Object value) {
        if (inArray() && _index == 0) {
            initListType(value);
        }

        writer.write(value);
    }

    public void end() {
        ended = true;

        if (!inRoot()) {
            Tag<?> tag = writer.getTag();
            ((NBTWriteContext) _parent).writeValue(tag);
        }
    }

    public Tag<?> getValue() {
        return writer.getTag();
    }

    public boolean isEnded() {
        return ended;
    }

    private NBTWriteContext createOrReset(int type) {
        NBTWriteContext ctxt = (NBTWriteContext) _child;
        if (ctxt == null) {
            _child = ctxt = new NBTWriteContext(type, this,
                    (_dups == null) ? null : _dups.child(), _currentName);
            return ctxt;
        }

        return ctxt.reset(type, _currentName);
    }
}
