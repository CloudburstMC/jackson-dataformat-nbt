package com.nukkitx.jackson.dataformat.nbt;

import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.cfg.MapperBuilder;
import com.fasterxml.jackson.databind.ser.BasicSerializerFactory;
import com.nukkitx.jackson.dataformat.nbt.NBTFactory.Feature;
import com.nukkitx.jackson.dataformat.nbt.ser.ByteSerializer;

import java.lang.reflect.Field;
import java.util.Map;

public class NBTMapper extends ObjectMapper {

    public NBTMapper() {
        this(new NBTFactory());
    }

    public NBTMapper(NBTFactory factory) {
        super(factory);
    }

    public NBTMapper(NBTMapper mapper) {
        super(mapper);
    }

    public static Builder builder() {
        return new Builder(new NBTMapper());
    }

    public static Builder builder(NBTFactory factory) {
        return new Builder(new NBTMapper(factory));
    }

    static {
        try {
            Field field = BasicSerializerFactory.class.getDeclaredField("_concrete");
            field.setAccessible(true);

            Map<String, JsonSerializer<?>> concrete = (Map) field.get(null);

            concrete.put(Byte.class.getName(), ByteSerializer.instance);
            concrete.put(Byte.TYPE.getName(), ByteSerializer.instance);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ObjectMapper copy() {
        _checkInvalidCopy(NBTMapper.class);
        return new NBTMapper(this);
    }

    public NBTMapper configure(Feature f, boolean state) {
        return state ? enable(f) : disable(f);
    }

    public NBTMapper configureParser(Feature f, boolean state) {
        return state ? enableParser(f) : disableParser(f);
    }

    public NBTMapper configureGen(Feature f, boolean state) {
        return state ? enableGen(f) : disableGen(f);
    }

    public NBTMapper enableParser(Feature f) {
        ((NBTFactory) _jsonFactory).enableParser(f);
        return this;
    }

    public NBTMapper enableGen(Feature f) {
        ((NBTFactory) _jsonFactory).enableGen(f);
        return this;
    }

    public NBTMapper disableParser(Feature f) {
        ((NBTFactory) _jsonFactory).disableParser(f);
        return this;
    }

    public NBTMapper disableGen(Feature f) {
        ((NBTFactory) _jsonFactory).disableGen(f);
        return this;
    }

    public NBTMapper enable(Feature f) {
        ((NBTFactory) _jsonFactory).enable(f);
        return this;
    }

    public NBTMapper disable(Feature f) {
        ((NBTFactory) _jsonFactory).disable(f);
        return this;
    }

    @Override
    public NBTFactory getFactory() {
        return (NBTFactory) _jsonFactory;
    }

    public static class Builder extends MapperBuilder<NBTMapper, Builder> {

        public Builder(NBTMapper mapper) {
            super(mapper);
        }

        public Builder enable(Feature... features) {
            for (Feature f : features) {
                _mapper.enable(f);
            }
            return this;
        }

        public Builder disable(Feature... features) {
            for (Feature f : features) {
                _mapper.disable(f);
            }
            return this;
        }

        public Builder configure(Feature f, boolean state) {
            if (state) {
                _mapper.enable(f);
            } else {
                _mapper.disable(f);
            }
            return this;
        }
    }
}
