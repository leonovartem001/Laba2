package org.parsers;

import org.factory.MissionBuilder;
import org.factory.MissionFactory;
import org.model.Mission;

import java.nio.file.Path;
import java.util.Map;

public abstract class AbstractMissionParser implements MissionParser {
    private final NormalizedMissionMapper mapper = new NormalizedMissionMapper();

    @Override
    public final Mission parse(Path path, String content, MissionFactory factory) throws Exception {
        MissionBuilder builder = factory.newBuilder();
        Map<String, Object> normalized = toNormalizedMap(path, content);
        mapper.map(normalized, builder);
        return builder.build();
    }

    protected abstract Map<String, Object> toNormalizedMap(Path path, String content) throws Exception;
}
