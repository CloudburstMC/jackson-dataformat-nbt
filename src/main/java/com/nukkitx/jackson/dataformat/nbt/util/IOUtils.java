package com.nukkitx.jackson.dataformat.nbt.util;

import com.nukkitx.nbt.util.stream.LittleEndianDataInputStream;
import com.nukkitx.nbt.util.stream.LittleEndianDataOutputStream;
import com.nukkitx.nbt.util.stream.NetworkDataInputStream;
import com.nukkitx.nbt.util.stream.NetworkDataOutputStream;

import java.io.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import static java.util.Objects.requireNonNull;

public class IOUtils {

    public static DataInput createReader(InputStream stream) {
        requireNonNull(stream, "stream");
        return new DataInputStream(stream);
    }

    public static DataInput createReaderLE(InputStream stream) {
        requireNonNull(stream, "stream");
        return new LittleEndianDataInputStream(stream);
    }

    public static InputStream createGZIPReader(InputStream stream) throws IOException {
        return new GZIPInputStream(stream);
    }

    public static DataInput createNetworkReader(InputStream stream) {
        requireNonNull(stream, "stream");
        return new NetworkDataInputStream(stream);
    }

    public static DataOutput createWriter(OutputStream stream) {
        requireNonNull(stream, "stream");
        return new DataOutputStream(stream);
    }

    public static DataOutput createWriterLE(OutputStream stream) {
        requireNonNull(stream, "stream");
        return new LittleEndianDataOutputStream(stream);
    }

    public static OutputStream createGZIPWriter(OutputStream stream) throws IOException {
        return new GZIPOutputStream(stream);
    }

    public static DataOutput createNetworkWriter(OutputStream stream) {
        return new NetworkDataOutputStream(stream);
    }
}
