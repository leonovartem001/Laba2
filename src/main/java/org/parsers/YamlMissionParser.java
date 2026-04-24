package org.parsers;

import java.nio.file.Path;
import java.util.Map;

public class YamlMissionParser extends AbstractMissionParser {
    private final SimpleYamlParser yamlParser = new SimpleYamlParser();

    @Override
    public boolean canParse(Path path, String content) {
        String ext = ParserUtils.ext(path.getFileName().toString());
        return ext.equals("yaml") || ext.equals("yml");
    }

    @Override
    protected Map<String, Object> toNormalizedMap(Path path, String content) {
        return yamlParser.parse(content);
    }

    @Override
    public String getFormatName() { return "YAML"; }

    @Override
    public int priority() { return 30; }
}
