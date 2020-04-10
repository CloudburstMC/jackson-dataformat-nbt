package com.nukkitx.jackson.dataformat.nbt;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.*;

@DisplayName("Generator")
public class GeneratorTest {

    //    @Test
    void byteTest() throws IOException {
        ObjectMapper mapper = new NBTMapper();
//        ObjectMapper mapper = new JsonMapper();

        ByteClass b = new ByteClass((byte) 20);

        byte[] serialized = mapper.writeValueAsBytes(b);

        ByteClass deserialized = mapper.readValue(serialized, ByteClass.class);
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
    }

    @Test
    void testGenerator() throws IOException {
        try {
            NBTMapper mapper = new NBTMapper();
            TestData data = new TestData(
                    "test string",
                    true,
                    (byte) 17,
                    (short) 17,
                    17,
                    17.5f,
                    new ArrayList<>(),
                    new HashMap<>(),
                    new ArrayList<>()
            );

            for (int i = 0; i < 10; i++) {
                data.listTest.add(i);
                data.mapTest.put(Integer.toString(i), i);

                data.subTests.add(new SubTest(i * 10, false));
            }

            byte[] serialized = mapper.writeValueAsBytes(data);

            TestData deserialized = mapper.readValue(serialized, TestData.class);

            assert data.equals(deserialized);
        } catch (Throwable t) {
            t.printStackTrace();
            throw new RuntimeException(t);
        }
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

        public TestData() {

        }

        public TestData(String string, boolean boolTest, byte byteTest, short shortTest, int intTest, float floatTest, List<Integer> listTest, Map<String, Integer> mapTest, List<SubTest> subTests) {
            this.string = string;
            this.boolTest = boolTest;
            this.byteTest = byteTest;
            this.shortTest = shortTest;
            this.intTest = intTest;
            this.floatTest = floatTest;
            this.listTest = listTest;
            this.mapTest = mapTest;
            this.subTests = subTests;
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
}
