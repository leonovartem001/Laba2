package org.source;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MissionFileSource {
    public Path readPath(String rawPath) {
        Path path = Paths.get(rawPath.trim().replace("\"", ""));
        if (!Files.exists(path)) throw new IllegalArgumentException("Файл не найден: " + path);
        if (!Files.isRegularFile(path)) throw new IllegalArgumentException("Это не файл: " + path);
        return path;
    }

    public String readContent(Path path) throws Exception {
        return Files.readString(path, StandardCharsets.UTF_8);
    }
}
