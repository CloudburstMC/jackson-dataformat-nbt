// Generated 12-Apr-2020 using Moditect maven plugin
module com.nukkitx.jackson.dataformat.nbt {
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires com.nukkitx.nbt;

    exports com.nukkitx.jackson.dataformat.nbt;

    provides com.fasterxml.jackson.core.JsonFactory with
            com.nukkitx.jackson.dataformat.nbt.NBTFactory;
}