package org.parsers;

import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Map;

public class XmlMissionParser extends AbstractMissionParser {
    @Override
    public boolean canParse(Path path, String content) {
        return ParserUtils.ext(path.getFileName().toString()).equals("xml")
                || ParserUtils.firstNonBlankLine(content).startsWith("<mission");
    }

    @Override
    protected Map<String, Object> toNormalizedMap(Path path, String content) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setIgnoringComments(true);
        factory.setNamespaceAware(false);
        Document doc = factory.newDocumentBuilder().parse(new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8)));
        doc.getDocumentElement().normalize();
        return XmlToMapConverter.convertRoot(doc.getDocumentElement());
    }

    @Override
    public String getFormatName() { return "XML"; }

    @Override
    public int priority() { return 40; }
}
