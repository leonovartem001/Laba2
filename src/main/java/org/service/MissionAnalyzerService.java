package org.service;

import org.factory.MissionFactory;
import org.model.Mission;
import org.parsers.MissionParser;
import org.parsers.ParserRegistry;
import org.reporting.ReportRenderer;
import org.reporting.ReportRendererRegistry;
import org.source.MissionFileSource;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class MissionAnalyzerService {
    private final MissionFileSource fileSource;
    private final ParserRegistry parserRegistry;
    private final ReportRendererRegistry rendererRegistry;
    private final MissionFactory missionFactory;

    public MissionAnalyzerService(MissionFileSource fileSource,
                                  ParserRegistry parserRegistry,
                                  ReportRendererRegistry rendererRegistry,
                                  MissionFactory missionFactory) {
        this.fileSource = fileSource;
        this.parserRegistry = parserRegistry;
        this.rendererRegistry = rendererRegistry;
        this.missionFactory = missionFactory;
    }

    public MissionLoadResult analyze(String rawPath, String rendererName) throws Exception {
        Path path = fileSource.readPath(rawPath);
        String content = fileSource.readContent(path);
        MissionParser parser = parserRegistry.getParsers().stream()
                .filter(candidate -> candidate.canParse(path, content))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Нет подходящего парсера для файла: " + path));

        Mission mission = parser.parse(path, content, missionFactory);
        ReportRenderer renderer = rendererRegistry.findByName(rendererName);
        return new MissionLoadResult(mission, parser.getFormatName(), renderer.getName(), renderer.render(mission));
    }

    public String supportedFormats() {
        return parserRegistry.getParsers().stream()
                .map(MissionParser::getFormatName)
                .distinct()
                .collect(Collectors.joining(", "));
    }

    public String supportedReports() {
        return rendererRegistry.getRenderers().stream()
                .map(ReportRenderer::getName)
                .collect(Collectors.joining(", "));
    }
}
