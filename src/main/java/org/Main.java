package org;

import org.factory.DefaultMissionFactory;
import org.factory.MissionFactory;
import org.parsers.ParserRegistry;
import org.reporting.ReportRendererRegistry;
import org.service.MissionAnalyzerService;
import org.source.MissionFileSource;
import org.viewer.CUI;

public class Main {
    public static void main(String[] args) {
        MissionFactory factory = new DefaultMissionFactory();
        ParserRegistry parserRegistry = new ParserRegistry();
        ReportRendererRegistry rendererRegistry = new ReportRendererRegistry();
        MissionFileSource fileSource = new MissionFileSource();
        MissionAnalyzerService service = new MissionAnalyzerService(fileSource, parserRegistry, rendererRegistry, factory);
        new CUI(service).showInterface(args);
    }
}
