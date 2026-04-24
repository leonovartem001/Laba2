package org.parsers;

import java.nio.file.Path;
import java.util.Map;

public class JsonMissionParser extends AbstractMissionParser {
    @Override
    public boolean canParse(Path path, String content) {
        return ParserUtils.ext(path.getFileName().toString()).equals("json")
                || ParserUtils.firstNonBlankLine(content).startsWith("{");
    }

    @Override
    protected Map<String, Object> toNormalizedMap(Path path, String content) {
        return SimpleJsonParser.parseObject(content);
    }

    @Override
    public String getFormatName() { return "JSON"; }

    @Override
    public int priority() { return 20; }
}
