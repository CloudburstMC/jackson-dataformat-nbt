package com.nukkitx.jackson.dataformat.nbt;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nukkitx.jackson.dataformat.nbt.NBTFactory.Feature;
import com.nukkitx.nbt.NbtMap;
import com.nukkitx.nbt.NbtType;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.util.List;

public class UntypedTest {
    private static final ObjectMapper mapper = new NBTMapper().enable(Feature.LITTLE_ENDIAN);

    @Test
    public void testHeterogeneousMap() throws IOException {
        NbtMap input = NbtMap.builder()
            .putInt("foo", 1)
            .putList("bar", NbtType.STRING, "a", "b")
            .putCompound("nested", NbtMap.builder()
                .putInt("xx", 12)
                .putString("hello", "world")
                .build()
            )
            .build();

        byte[] serialized = mapper.writeValueAsBytes(input);

        // help test whether it's serialization or deserialization that had a problem
        assertArrayEquals(new byte[]{
            10, // tag compound
            0, 0, // name (zero length)
            3, // tag int
            3, 0, 'f', 'o', 'o', // name (length, value)
            1, 0, 0, 0, // int value
            9, // tag list
            3, 0, 'b', 'a', 'r', // name
            8, // type of list = tag string
            2, 0, 0, 0, // length of list
            1, 0, 'a', // first string
            1, 0, 'b', // second string
            10, // tag compound
            6, 0, 'n', 'e', 's', 't', 'e', 'd', // name
            3, // tag int
            2, 0, 'x', 'x', // name
            12, 0, 0, 0, // value
            8, // tag string
            5, 0, 'h', 'e', 'l', 'l', 'o', // name
            5, 0, 'w', 'o', 'r', 'l', 'd', // value
            0, // end compound (nested)
            0 // end compound (outer)
        }, 
        serialized);
        
        Map<String, Object> deserialized = mapper.readValue(serialized, new TypeReference<Map<String,Object>>(){});

        assertEquals(input, deserialized);
    }

}