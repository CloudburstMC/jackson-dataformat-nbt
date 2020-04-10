package com.nukkitx.jackson.dataformat.nbt.ser;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.Base;
import com.nukkitx.jackson.dataformat.nbt.NBTGenerator;

import java.io.IOException;

@JacksonStdImpl
public class ByteSerializer extends Base<Object> {

    public final static ByteSerializer instance = new ByteSerializer();

    public ByteSerializer() {
        super(Byte.class, JsonParser.NumberType.INT, "number");
    }

    @Override
    public void serialize(Object value, JsonGenerator gen,
                          SerializerProvider provider) throws IOException {
        byte byteValue = (Byte) value;

        if (gen instanceof NBTGenerator) {
            ((NBTGenerator) gen).writeNumber(byteValue);
        } else {
            gen.writeNumber(byteValue);
        }
    }
}
