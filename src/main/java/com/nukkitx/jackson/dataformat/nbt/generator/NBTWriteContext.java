package com.nukkitx.jackson.dataformat.nbt.generator;

import com.fasterxml.jackson.core.json.DupDetector;
import com.fasterxml.jackson.core.json.JsonWriteContext;
import com.nukkitx.nbt.NbtType;

import java.io.DataOutput;
import java.io.IOException;

public class NBTWriteContext extends JsonWriteContext {

    protected String name;
    public NBTWriter writer;
    private DataOutput output;
    private final int level;

    public NBTWriteContext(int type, NBTWriteContext parent, DupDetector dups, String name, DataOutput output) {
        this(type, parent, dups, name, output, 0);
    }

    public NBTWriteContext(int type, NBTWriteContext parent, DupDetector dups, String name, DataOutput output, int level) {
        super(type, parent, dups);
        System.out.println("WRITE CONTEXT: " + output);
        this.level = level;
        this.output = output;
        if (name == null) {
            name = "";
        }

        this.name = name;

        if (inRoot()) {
            writer = new SingleTagWriter(name, output);
        } else if (!inArray()) {
            writer = new CompoundTagWriter(name, output);
        }
    }

    private void initListType(NbtType<?> type) {
//        NbtType<?> type;
//
//        if (value instanceof Iterable) {
//            type = NbtType.LIST;
//        } else if (value instanceof Map) {
//            type = NbtType.COMPOUND;
//        } else {
//            type = NbtType.byClass(value.getClass());
//        }

        writer = new ListTagWriter(name == null || name.isEmpty() ? Integer.toString(level) : name, type, output);
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

    protected NBTWriteContext reset(int type, String name, DataOutput output) {
        super.reset(type);
        this.output = output;
        this.name = name;

        if (!inArray()) {
            writer = new CompoundTagWriter(name, output);
        } else {
            writer = null;
        }

        return this;
    }

    @Override
    protected NBTWriteContext reset(int type, Object currValue) {
        throw new UnsupportedOperationException();
    }

    public void writeValue(NbtType<?> type, Object value) throws IOException {
        if (inArray() && _index == 0) {
            initListType(type);
        }

        writer.write(type, _currentName, value);
    }

    public void end() throws IOException {
        if (writer == null && inArray()) {
            throw new IllegalStateException("Cannot write empty list");
//            writer = new ListTagWriter(name, NbtType.END, output); //TODO: check
        }

        writer.end();
    }

    public boolean isEnded() {
        return writer != null && writer.isEnded();
    }

    private NBTWriteContext createOrReset(int type) {
        NBTWriteContext ctxt = (NBTWriteContext) _child;
        if (ctxt == null) {
            _child = ctxt = new NBTWriteContext(type, this,
                    (_dups == null) ? null : _dups.child(), _currentName, writer.output, level + 1);
            return ctxt;
        }

        return ctxt.reset(type, _currentName, writer.output);
    }

    public DataOutput getOutput() {
        return writer.output;
    }
}
