package org.parsers;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ServiceLoader;

public class ParserRegistry {
    private final List<MissionParser> parsers;

    public ParserRegistry() {
        this.parsers = new ArrayList<>();
        ServiceLoader.load(MissionParser.class).forEach(parsers::add);
        this.parsers.sort(Comparator.comparingInt(MissionParser::priority));
    }

    public List<MissionParser> getParsers() {
        return parsers;
    }
}
