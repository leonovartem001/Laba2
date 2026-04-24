package org.parsers;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SimpleYamlParser {
    public Map<String, Object> parse(String content) {
        String[] lines = content.split("\\R");
        Map<String, Object> root = new LinkedHashMap<>();
        int i = 0;
        while (i < lines.length) {
            String line = lines[i];
            if (line.isBlank() || line.trim().startsWith("#")) {
                i++;
                continue;
            }
            if (indent(line) != 0) {
                i++;
                continue;
            }
            String trimmed = line.trim();
            int colon = trimmed.indexOf(':');
            if (colon < 0) {
                i++;
                continue;
            }
            String key = trimmed.substring(0, colon).trim();
            String rest = trimmed.substring(colon + 1).trim();
            if (!rest.isEmpty()) {
                root.put(key, ParserUtils.cleanScalar(rest));
                i++;
                continue;
            }

            int next = nextMeaningfulLine(lines, i + 1);
            if (next == -1 || indent(lines[next]) <= 0) {
                root.put(key, new LinkedHashMap<String, Object>());
                i++;
                continue;
            }
            if (lines[next].trim().startsWith("- ")) {
                ParseResult listResult = parseList(lines, next, indent(lines[next]));
                root.put(key, listResult.value());
                i = listResult.nextIndex();
            } else {
                ParseResult mapResult = parseMap(lines, next, indent(lines[next]));
                root.put(key, mapResult.value());
                i = mapResult.nextIndex();
            }
        }
        return root;
    }

    private ParseResult parseMap(String[] lines, int start, int baseIndent) {
        Map<String, Object> map = new LinkedHashMap<>();
        int i = start;
        while (i < lines.length) {
            String line = lines[i];
            if (line.isBlank() || line.trim().startsWith("#")) {
                i++;
                continue;
            }
            int indent = indent(line);
            if (indent < baseIndent) break;
            if (indent > baseIndent) {
                i++;
                continue;
            }
            String trimmed = line.trim();
            if (trimmed.startsWith("- ")) break;
            int colon = trimmed.indexOf(':');
            if (colon < 0) {
                i++;
                continue;
            }
            String key = trimmed.substring(0, colon).trim();
            String rest = trimmed.substring(colon + 1).trim();
            if (!rest.isEmpty()) {
                map.put(key, ParserUtils.cleanScalar(rest));
                i++;
                continue;
            }
            int next = nextMeaningfulLine(lines, i + 1);
            if (next == -1 || indent(lines[next]) <= indent) {
                map.put(key, new LinkedHashMap<String, Object>());
                i++;
                continue;
            }
            if (lines[next].trim().startsWith("- ")) {
                ParseResult listResult = parseList(lines, next, indent(lines[next]));
                map.put(key, listResult.value());
                i = listResult.nextIndex();
            } else {
                ParseResult mapResult = parseMap(lines, next, indent(lines[next]));
                map.put(key, mapResult.value());
                i = mapResult.nextIndex();
            }
        }
        return new ParseResult(map, i);
    }

    private ParseResult parseList(String[] lines, int start, int itemIndent) {
        List<Object> list = new ArrayList<>();
        int i = start;
        while (i < lines.length) {
            String line = lines[i];
            if (line.isBlank() || line.trim().startsWith("#")) {
                i++;
                continue;
            }
            int indent = indent(line);
            if (indent < itemIndent || !line.trim().startsWith("- ")) break;
            String itemText = line.trim().substring(2).trim();
            if (itemText.isEmpty()) {
                int next = nextMeaningfulLine(lines, i + 1);
                if (next == -1 || indent(lines[next]) <= indent) {
                    list.add("");
                    i++;
                    continue;
                }
                if (lines[next].trim().startsWith("- ")) {
                    ParseResult nestedList = parseList(lines, next, indent(lines[next]));
                    list.add(nestedList.value());
                    i = nestedList.nextIndex();
                } else {
                    ParseResult nestedMap = parseMap(lines, next, indent(lines[next]));
                    list.add(nestedMap.value());
                    i = nestedMap.nextIndex();
                }
                continue;
            }
            if (itemText.contains(":")) {
                Map<String, Object> itemMap = new LinkedHashMap<>();
                int colon = itemText.indexOf(':');
                itemMap.put(itemText.substring(0, colon).trim(), ParserUtils.cleanScalar(itemText.substring(colon + 1).trim()));
                i++;
                while (i < lines.length) {
                    String nextLine = lines[i];
                    if (nextLine.isBlank() || nextLine.trim().startsWith("#")) {
                        i++;
                        continue;
                    }
                    int nextIndent = indent(nextLine);
                    if (nextIndent <= itemIndent) break;
                    String nextTrim = nextLine.trim();
                    if (nextTrim.startsWith("- ")) break;
                    int nextColon = nextTrim.indexOf(':');
                    if (nextColon > -1) {
                        String key = nextTrim.substring(0, nextColon).trim();
                        String rest = nextTrim.substring(nextColon + 1).trim();
                        itemMap.put(key, ParserUtils.cleanScalar(rest));
                    }
                    i++;
                }
                list.add(itemMap);
            } else {
                list.add(ParserUtils.cleanScalar(itemText));
                i++;
            }
        }
        return new ParseResult(list, i);
    }

    private int nextMeaningfulLine(String[] lines, int index) {
        for (int i = index; i < lines.length; i++) {
            if (!lines[i].isBlank() && !lines[i].trim().startsWith("#")) {
                return i;
            }
        }
        return -1;
    }

    private int indent(String line) {
        int count = 0;
        while (count < line.length() && line.charAt(count) == ' ') count++;
        return count;
    }

    private record ParseResult(Object value, int nextIndex) {}
}
