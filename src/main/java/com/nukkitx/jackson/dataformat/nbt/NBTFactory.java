package com.nukkitx.jackson.dataformat.nbt;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.format.InputAccessor;
import com.fasterxml.jackson.core.format.MatchStrength;
import com.fasterxml.jackson.core.io.IOContext;

import java.io.*;
import java.net.URL;

public class NBTFactory extends JsonFactory {

    public static final String FORMAT_NAME_NBT = "NBT";
    public static final Version VERSION = new Version(1, 0, 0, "SNAPSHOT", "src/test/com.nukkitx.jackson.dataformat.nbt", "nbt");

    /**
     * Bitfield (set of flags) of all parser features that are enabled
     * by default.
     */
    protected final static int DEFAULT_NBT_PARSER_FEATURE_FLAGS = Feature.collectDefaults();

    /**
     * Bitfield (set of flags) of all generator features that are enabled
     * by default.
     */
    protected final static int DEFAULT_NBT_GENERATOR_FEATURE_FLAGS = Feature.collectDefaults();

    protected int _nbtParserFeatures = DEFAULT_NBT_PARSER_FEATURE_FLAGS;

    protected int _nbtGeneratorFeatures = DEFAULT_NBT_GENERATOR_FEATURE_FLAGS;

    public NBTFactory() {
        this(null);
    }

    public NBTFactory(ObjectCodec oc) {
        super(oc);
    }

    public NBTFactory(NBTFactory factory, ObjectCodec oc) {
        super(oc);
        _nbtGeneratorFeatures = factory._nbtGeneratorFeatures;
        _nbtParserFeatures = factory._nbtParserFeatures;
    }

    @Override
    protected Object readResolve() {
        return new NBTFactory();
    }

    @Override
    public Version version() {
        return VERSION;
    }

    @Override
    public boolean canUseCharArrays() {
        return false;
    }

    @Override
    public String getFormatName() {
        return super.getFormatName();
    }

    @Override
    public MatchStrength hasFormat(InputAccessor acc) throws IOException {
        if (!acc.hasMoreBytes()) {
            return MatchStrength.INCONCLUSIVE;
        }

        return MatchStrength.FULL_MATCH;
    }

    public final NBTFactory configure(Feature f, boolean state) {
        if (state) {
            enable(f);
        } else {
            disable(f);
        }
        return this;
    }

    public NBTFactory enable(Feature f) {
        _nbtGeneratorFeatures |= f.getMask();
        _nbtParserFeatures |= f.getMask();
        return this;
    }

    public NBTFactory disable(Feature f) {
        _nbtGeneratorFeatures &= ~f.getMask();
        _nbtParserFeatures &= ~f.getMask();
        return this;
    }

    /*
    /**********************************************************
    /* Configuration, parser settings
    /**********************************************************
     */

    /**
     * Method for enabling or disabling specified parser feature
     * (check {@link Feature} for list of features)
     */
    public final NBTFactory configureParser(Feature f, boolean state) {
        if (state) {
            enableParser(f);
        } else {
            disableParser(f);
        }
        return this;
    }

    /**
     * Method for enabling specified parser feature
     * (check {@link Feature} for list of features)
     */
    public NBTFactory enableParser(Feature f) {
        _nbtParserFeatures |= f.getMask();
        return this;
    }

    /**
     * Method for disabling specified parser features
     * (check {@link Feature} for list of features)
     */
    public NBTFactory disableParser(Feature f) {
        _nbtParserFeatures &= ~f.getMask();
        return this;
    }

    /**
     * Checked whether specified parser feature is enabled.
     */
    public final boolean isEnabledParser(Feature f) {
        return (_nbtParserFeatures & f.getMask()) != 0;
    }

    @Override
    public int getFormatParserFeatures() {
        return _nbtParserFeatures;
    }

    /*
    /**********************************************************
    /* Configuration, generator settings
    /**********************************************************
     */

    /**
     * Method for enabling or disabling specified generator feature
     * (check {@link Feature} for list of features)
     */
    public final NBTFactory configureGen(Feature f, boolean state) {
        if (state) {
            enableGen(f);
        } else {
            disableGen(f);
        }
        return this;
    }


    /**
     * Method for enabling specified generator features
     * (check {@link Feature} for list of features)
     */
    public NBTFactory enableGen(Feature f) {
        _nbtGeneratorFeatures |= f.getMask();
        return this;
    }

    /**
     * Method for disabling specified generator feature
     * (check {@link Feature} for list of features)
     */
    public NBTFactory disableGen(Feature f) {
        _nbtGeneratorFeatures &= ~f.getMask();
        return this;
    }

    /**
     * Check whether specified generator feature is enabled.
     */
    public final boolean isEnabledGen(Feature f) {
        return (_nbtGeneratorFeatures & f.getMask()) != 0;
    }

    @Override
    public int getFormatGeneratorFeatures() {
        return _nbtGeneratorFeatures;
    }

    //parser

    @Override
    public NBTParser createParser(File f) throws IOException {
        FileInputStream fis = new FileInputStream(f);
        IOContext context = _createContext(f, true);

        return _createParser(_decorate(fis, context), context);
    }

    @Override
    public NBTParser createParser(URL url) throws IOException {
        IOContext context = _createContext(url, true);

        return _createParser(_decorate(_optimizedStreamFromURL(url), context), context);
    }

    @Override
    public NBTParser createParser(byte[] data) throws IOException {
        return createParser(data, 0, data.length);
    }

    @Override
    public NBTParser createParser(byte[] data, int offset, int len) throws IOException {
        IOContext context = _createContext(data, true);

        if (_inputDecorator != null) {
            InputStream in = _inputDecorator.decorate(context, data, offset, len);
            if (in != null) {
                return _createParser(in, context);
            }
        }

        return _createParser(new ByteArrayInputStream(data, offset, len), context);
    }

    @Override
    public NBTParser createParser(Reader r) {
        throw new UnsupportedOperationException();
    }

    @Override
    public NBTParser createParser(String content) {
        throw new UnsupportedOperationException();
    }

    @Override
    public NBTParser createParser(char[] content, int offset, int len) {
        throw new UnsupportedOperationException();
    }

    @Override
    public NBTParser createParser(char[] content) {
        throw new UnsupportedOperationException();
    }

    //generator


    @Override
    public NBTGenerator createGenerator(OutputStream out, JsonEncoding enc) throws IOException {
        return createGenerator(out);
    }

    @Override
    public NBTGenerator createGenerator(OutputStream out) throws IOException {
        return _createGenerator(out, _createContext(out, false));
    }

    @Override
    public NBTGenerator createGenerator(Writer w) {
        throw new UnsupportedOperationException();
    }

    @Override
    public NBTGenerator createGenerator(File f, JsonEncoding enc) throws IOException {
        FileOutputStream fos = new FileOutputStream(f);
        IOContext context = _createContext(f, true);

        return _createGenerator(_decorate(fos, context), context);
    }

    protected NBTParser _createParser(InputStream in, IOContext context) throws IOException {
        return new NBTParser(context, _getBufferRecycler(), _parserFeatures, _nbtParserFeatures, _objectCodec, in);
    }

    private NBTGenerator _createGenerator(OutputStream out, IOContext context) throws IOException {
        return new NBTGenerator(context, _generatorFeatures, _nbtGeneratorFeatures, _objectCodec, out);
    }

    public enum Feature implements FormatFeature {
        BIG_ENDIAN(true),
        LITTLE_ENDIAN(false),
        NETWORK(false),
        GZIP(false);

        protected final boolean _defaultState;
        protected final int _mask;

        Feature(boolean defaultState) {
            this._defaultState = defaultState;
            this._mask = 1 << ordinal();
        }

        public boolean enabledByDefault() {
            return _defaultState;
        }

        public boolean enabledIn(int flags) {
            return (flags & _mask) != 0;
        }

        public int getMask() {
            return _mask;
        }

        /**
         * Method that calculates bit set (flags) of all features that
         * are enabled by default.
         */
        public static int collectDefaults() {
            int flags = 0;
            for (Feature f : values()) {
                if (f.enabledByDefault()) {
                    flags |= f.getMask();
                }
            }
            return flags;
        }
    }
}
