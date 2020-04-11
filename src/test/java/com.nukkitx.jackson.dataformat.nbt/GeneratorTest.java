package com.nukkitx.jackson.dataformat.nbt;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Generator")
public class GeneratorTest {

    private final ObjectMapper mapper = new NBTMapper();

    @Test
    @DisplayName("byte")
    void byteTest() throws IOException {
        ByteClass b = new ByteClass((byte) 20);

        byte[] serialized = mapper.writeValueAsBytes(b);
        ByteClass deserialized = mapper.readValue(serialized, ByteClass.class);

        assertEquals(b, deserialized);
    }

    @Test
    @DisplayName("test")
    void testGenerator() throws IOException {
        TestData data = getTestData(10);

        byte[] serialized = mapper.writeValueAsBytes(data);
        TestData deserialized = mapper.readValue(serialized, TestData.class);

        assertEquals(data, deserialized);
    }

    @Test
    @DisplayName("list")
    void testList() throws IOException {
        List<TestData> data = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            data.add(getTestData(3));
        }

        byte[] serialized = mapper.writeValueAsBytes(data);

        List<TestData> deserialized = mapper.readValue(serialized, new TypeReference<List<TestData>>() {
        });
        assertEquals(data, deserialized);
    }

    @Test
    @DisplayName("array")
    void primitiveArrayTest() throws IOException {
        int[] data = new int[100];

        Random r = new Random();
        for (int i = 0; i < data.length; i++) {
            data[i] = r.nextInt();
        }

        byte[] serialized = mapper.writeValueAsBytes(data);
        int[] deserialized = mapper.readValue(serialized, int[].class);

        Assertions.assertArrayEquals(data, deserialized);
    }

    @Test
    @DisplayName("byte array")
    void primitiveByteArrayTest() throws IOException {
        byte[] data = new byte[100];

        Random r = new Random();
        for (int i = 0; i < data.length; i++) {
            data[i] = (byte) r.nextInt(256);
        }

        byte[] serialized = mapper.writeValueAsBytes(data);
        byte[] deserialized = mapper.readValue(serialized, byte[].class);

        Assertions.assertArrayEquals(data, deserialized);
    }

    @Test
    @DisplayName("value")
    void valueTest() throws IOException {
        long value = new Random().nextLong();

        byte[] serialized = mapper.writeValueAsBytes(value);
        long deserialized = mapper.readValue(serialized, long.class);

        Assertions.assertEquals(value, deserialized);
    }

    private TestData getTestData(int c) {
        TestData data = new TestData(
                "test string",
                true,
                (byte) 17,
                (short) 17,
                17,
                17.5f,
                new ArrayList<>(),
                new HashMap<>(),
                new ArrayList<>(),
                new int[10],
                new byte[10]
        );

        for (int i = 0; i < c; i++) {
            data.listTest.add(i);
            data.mapTest.put(Integer.toString(i), i);

            data.subTests.add(new SubTest(i * 10, false));
            data.intArrayTest[i] = i * 20;
            data.byteArrayTest[i] = (byte) (i * 2);
        }

        return data;
    }

    public static class TestData {
        public String string;
        public boolean boolTest;
        public byte byteTest;
        public short shortTest;
        public int intTest;
        public float floatTest;
        public List<Integer> listTest;
        public Map<String, Integer> mapTest;
        public List<SubTest> subTests;
        public int[] intArrayTest;
        public byte[] byteArrayTest;

        public TestData() {

        }

        public TestData(
                String string,
                boolean boolTest,
                byte byteTest,
                short shortTest,
                int intTest,
                float floatTest,
                List<Integer> listTest,
                Map<String, Integer> mapTest,
                List<SubTest> subTests,
                int[] intArrayTest,
                byte[] byteArrayTest
        ) {
            this.string = string;
            this.boolTest = boolTest;
            this.byteTest = byteTest;
            this.shortTest = shortTest;
            this.intTest = intTest;
            this.floatTest = floatTest;
            this.listTest = listTest;
            this.mapTest = mapTest;
            this.subTests = subTests;
            this.intArrayTest = intArrayTest;
            this.byteArrayTest = byteArrayTest;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            TestData testData = (TestData) o;
            return boolTest == testData.boolTest &&
                    byteTest == testData.byteTest &&
                    shortTest == testData.shortTest &&
                    intTest == testData.intTest &&
                    Float.compare(testData.floatTest, floatTest) == 0 &&
                    Objects.equals(string, testData.string) &&
                    Objects.equals(listTest, testData.listTest) &&
                    Objects.equals(mapTest, testData.mapTest) &&
                    Objects.equals(subTests, testData.subTests);
        }

        @Override
        public int hashCode() {
            return Objects.hash(string, boolTest, byteTest, shortTest, intTest, floatTest, listTest, mapTest, subTests);
        }

        @Override
        public String toString() {
            return "TestData{" +
                    "string='" + string + '\'' +
                    ", boolTest=" + boolTest +
                    ", byteTest=" + byteTest +
                    ", shortTest=" + shortTest +
                    ", intTest=" + intTest +
                    ", floatTest=" + floatTest +
                    ", listTest=" + listTest +
                    ", mapTest=" + mapTest +
                    ", subTests=" + subTests +
                    '}';
        }
    }

    public static class SubTest {
        public boolean boolTest2;
        public long l;

        public SubTest(long l, boolean boolTest2) {
            this.l = l;
            this.boolTest2 = boolTest2;
        }

        public SubTest() {

        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            SubTest subTest = (SubTest) o;
            return boolTest2 == subTest.boolTest2 &&
                    l == subTest.l;
        }

        @Override
        public int hashCode() {
            return Objects.hash(boolTest2, l);
        }

        @Override
        public String toString() {
            return "SubTest{" +
                    "boolTest2=" + boolTest2 +
                    ", l=" + l +
                    '}';
        }
    }

    public static class ByteClass {

        public byte value;

        public ByteClass(byte value) {
            this.value = value;
        }

        public ByteClass() {

        }

        @Override
        public String toString() {
            return "ByteClass{" +
                    "value=" + value +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ByteClass byteClass = (ByteClass) o;
            return value == byteClass.value;
        }

        @Override
        public int hashCode() {
            return Objects.hash(value);
        }
    }
}
