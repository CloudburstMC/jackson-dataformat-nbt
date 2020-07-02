package com.nukkitx.jackson.dataformat.nbt;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.io.IOException;
import java.util.*;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;


public class GeneratorTest {

    private static final ObjectMapper mapper = new NBTMapper();

    @Test
    public void byteTest() throws IOException {
        ByteClass b = new ByteClass((byte) 20);

        byte[] serialized = mapper.writeValueAsBytes(b);
        ByteClass deserialized = mapper.readValue(serialized, ByteClass.class);

        assertEquals(b, deserialized);
    }

    @Test
    public void byteListTest() throws IOException {
        List<ByteClass> b = Arrays.asList(new ByteClass((byte) 20), new ByteClass((byte) 10), new ByteClass((byte) 90));

        byte[] serialized = mapper.writeValueAsBytes(b);
        List<ByteClass> deserialized = mapper.readValue(serialized, new TypeReference<List<ByteClass>>() {
        });

        assertEquals(b, deserialized);
    }

    @Test
    public void simpleTest() throws IOException {
        SimpleData data = getSimpleData();

        byte[] serialized = mapper.writeValueAsBytes(data);
        System.out.println(Arrays.toString(serialized));

        SimpleData deserialized = mapper.readValue(serialized, SimpleData.class);

        assertEquals(data, deserialized);
    }

    @Test
    public void generatorTest() throws IOException {
        TestData data = getTestData(10);

        byte[] serialized = mapper.writeValueAsBytes(data);
        TestData deserialized = mapper.readValue(serialized, TestData.class);

        assertEquals(data, deserialized);
    }

    @Test
    public void listTest() throws IOException {
        List<TestData> data = new ArrayList<>();

        for (int i = 0; i < 2; i++) {
            data.add(getTestData(1));
        }

        byte[] serialized = mapper.writeValueAsBytes(data);

        List<TestData> deserialized = mapper.readValue(serialized, new TypeReference<List<TestData>>() {
        });

        assertEquals(data, deserialized);
    }

    @Test
    public void listTest2() throws IOException {
        List<ListTestData> data = new ArrayList<>();

        for (int i = 0; i < 2; i++) {
            data.add(getListData());
        }

        byte[] serialized = mapper.writeValueAsBytes(data);

        List<ListTestData> deserialized = mapper.readValue(serialized, new TypeReference<List<ListTestData>>() {
        });

        assertEquals(data, deserialized);
    }

    @Test
    public void listTest3() throws IOException {
        List<List<ByteClass>> data = new ArrayList<>();

        for (int i = 0; i < 2; i++) {
            data.add(Arrays.asList(new ByteClass((byte) 16)));
        }

        byte[] serialized = mapper.writeValueAsBytes(data);

        List<List<ByteClass>> deserialized = mapper.readValue(serialized, new TypeReference<List<List<ByteClass>>>() {
        });

        assertEquals(data, deserialized);
    }

    @Test
    public void primitiveArrayTest() throws IOException {
        int[] data = new int[100];

        Random r = new Random();
        for (int i = 0; i < data.length; i++) {
            data[i] = r.nextInt();
        }

        byte[] serialized = mapper.writeValueAsBytes(data);
        int[] deserialized = mapper.readValue(serialized, int[].class);

        assertArrayEquals(data, deserialized);
    }

    @Test
    public void primitiveByteArrayTest() throws IOException {
        byte[] data = new byte[100];

        Random r = new Random();
        for (int i = 0; i < data.length; i++) {
            data[i] = (byte) r.nextInt(256);
        }

        byte[] serialized = mapper.writeValueAsBytes(data);
        byte[] deserialized = mapper.readValue(serialized, byte[].class);

        assertArrayEquals(data, deserialized);
    }

    @Test
    public void valueTest() throws IOException {
        long value = new Random().nextLong();

        byte[] serialized = mapper.writeValueAsBytes(value);
        long deserialized = mapper.readValue(serialized, long.class);

        assertEquals(value, deserialized);
    }

    private TestData getTestData(int c) {
        TestData data = new TestData(
                "test string",
                true,
                (byte) 17,
                (short) 17,
                17,
                17.5f,
                new ArrayList<Integer>(),
                new HashMap<String, Integer>(),
                new ArrayList<SubTest>(),
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

    private SimpleData getSimpleData() {
        SimpleData data = new SimpleData(
                "test",
                true,
                (byte) 17,
                (short) 17,
                17,
                17.5f,
                Arrays.asList(1, 2, 3, 4, 5),
                new SubTest(15, true),
                Arrays.asList(new SubTest(101, false), new SubTest(202, true))
        );

        return data;
    }

    private ListTestData getListData() {
        Map<Integer, Integer> map = new HashMap<>();

        for (int i = 0; i < 6; i++) {
            map.put(i, i * 2);
        }

        return new ListTestData(
                17,
                Arrays.asList(1, 2, 3, 3, 2, 1),
                map,
                Arrays.asList(new SubTest(1, true))
        );
    }

    public static class ListTestData {
        public int intTest;
        public List<Integer> listTest;
        public Map<Integer, Integer> mapTest;
        public List<SubTest> subTests;

        public ListTestData() {

        }

        public ListTestData(int intTest, List<Integer> listTest, Map<Integer, Integer> mapTest, List<SubTest> subTests) {
            this.intTest = intTest;
            this.listTest = listTest;
            this.mapTest = mapTest;
            this.subTests = subTests;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ListTestData that = (ListTestData) o;
            return intTest == that.intTest &&
                    Objects.equals(listTest, that.listTest) &&
                    Objects.equals(mapTest, that.mapTest) &&
                    Objects.equals(subTests, that.subTests);
        }

        @Override
        public int hashCode() {
            return Objects.hash(intTest, listTest, mapTest, subTests);
        }

        @Override
        public String toString() {
            return "ListTestData{" +
                    "intTest=" + intTest +
                    ", listTest=" + listTest +
                    ", mapTest=" + mapTest +
                    ", subTests=" + subTests +
                    '}';
        }
    }

    public static class SimpleData {
        public String string;
        public boolean boolTest;
        public byte byteTest;
        public short shortTest;
        public int intTest;
        public float floatTest;
        public List<Integer> list;
        public SubTest subTest;
        public List<SubTest> subTestList;

        public SimpleData() {

        }

        public SimpleData(String string, boolean boolTest, byte byteTest, short shortTest, int intTest, float floatTest, List<Integer> list, SubTest subTest, List<SubTest> subTestList) {
            this.string = string;
            this.boolTest = boolTest;
            this.byteTest = byteTest;
            this.shortTest = shortTest;
            this.intTest = intTest;
            this.floatTest = floatTest;
            this.list = list;
            this.subTest = subTest;
            this.subTestList = subTestList;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            SimpleData that = (SimpleData) o;
            return boolTest == that.boolTest &&
                    byteTest == that.byteTest &&
                    shortTest == that.shortTest &&
                    intTest == that.intTest &&
                    Float.compare(that.floatTest, floatTest) == 0 &&
                    Objects.equals(string, that.string) &&
                    Objects.equals(list, that.list) &&
                    Objects.equals(subTest, that.subTest) &&
                    Objects.equals(subTestList, that.subTestList);
        }

        @Override
        public int hashCode() {
            return Objects.hash(string, boolTest, byteTest, shortTest, intTest, floatTest, list, subTest, subTestList);
        }

        @Override
        public String toString() {
            return "SimpleData{" +
                    "string='" + string + '\'' +
                    ", boolTest=" + boolTest +
                    ", byteTest=" + byteTest +
                    ", shortTest=" + shortTest +
                    ", intTest=" + intTest +
                    ", floatTest=" + floatTest +
                    ", list=" + list +
                    ", subTest=" + subTest +
                    ", subTestList=" + subTestList +
                    '}';
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
