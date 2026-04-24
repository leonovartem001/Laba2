package org.parsers;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class XmlToMapConverter {
    private XmlToMapConverter() {}

    public static Map<String, Object> convertRoot(Element root) {
        Object value = convertElement(root);
        if (!(value instanceof Map<?, ?> map)) {
            throw new IllegalArgumentException("Корневой XML-элемент должен содержать структуру объекта");
        }
        @SuppressWarnings("unchecked")
        Map<String, Object> result = (Map<String, Object>) map;
        return result;
    }

    private static Object convertElement(Element element) {
        List<Element> children = childElements(element);
        if (children.isEmpty()) {
            return element.getTextContent().trim();
        }

        boolean sameTag = children.stream().map(Element::getTagName).distinct().count() == 1;
        if (sameTag) {
            List<Object> list = new ArrayList<>();
            for (Element child : children) {
                list.add(convertElement(child));
            }
            return list;
        }

        Map<String, Object> map = new LinkedHashMap<>();
        Map<String, List<Element>> grouped = new LinkedHashMap<>();
        for (Element child : children) {
            grouped.computeIfAbsent(child.getTagName(), key -> new ArrayList<>()).add(child);
        }
        for (Map.Entry<String, List<Element>> entry : grouped.entrySet()) {
            if (entry.getValue().size() == 1) {
                map.put(entry.getKey(), convertElement(entry.getValue().get(0)));
            } else {
                List<Object> list = new ArrayList<>();
                for (Element child : entry.getValue()) {
                    list.add(convertElement(child));
                }
                map.put(entry.getKey(), list);
            }
        }
        return map;
    }

    private static List<Element> childElements(Element element) {
        List<Element> result = new ArrayList<>();
        NodeList nodes = element.getChildNodes();
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                result.add((Element) node);
            }
        }
        return result;
    }
}
