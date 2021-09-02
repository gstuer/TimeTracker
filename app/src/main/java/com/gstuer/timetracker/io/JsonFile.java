package com.gstuer.timetracker.io;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Objects;

public class JsonFile {
    private static final Charset CHARSET = StandardCharsets.UTF_8;
    private final Path path;

    public JsonFile(Path path) {
        this.path = Objects.requireNonNull(path);
    }

    public <T> T read(Class<T> type) throws IOException {
        String serializedObject = Files.readString(this.path, CHARSET);
        return getConfiguredObjectMapper().readValue(serializedObject, type);
    }

    public <T> void write(T object) throws IOException {
        String serializedObject = getConfiguredObjectMapper().writeValueAsString(object);
        if (!exists()) {
            // Create all needed directories if they do not exist
            Files.createDirectories(this.path.getParent());
        }
        Files.writeString(this.path, serializedObject, CHARSET, StandardOpenOption.CREATE);
    }

    public boolean exists() {
        return Files.exists(this.path);
    }

    public static ObjectMapper getConfiguredObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        return mapper;
    }
}
