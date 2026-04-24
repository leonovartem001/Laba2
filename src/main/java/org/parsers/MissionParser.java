package org.parsers;

import org.factory.MissionFactory;
import org.model.Mission;

import java.nio.file.Path;

public interface MissionParser {
    boolean canParse(Path path, String content);
    Mission parse(Path path, String content, MissionFactory factory) throws Exception;
    String getFormatName();
    default int priority() { return 100; }
}
