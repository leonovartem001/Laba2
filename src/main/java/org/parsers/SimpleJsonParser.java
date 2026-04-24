package org.parsers;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SimpleJsonParser {
    private final String text;
    private int index;

    public SimpleJsonParser(String text) {
        this.text = text;
    }

    public static Map<String, Object> parseObject(String text) {
        Object value = new SimpleJsonParser(text).parseValue();
        if (!(value instanceof Map<?, ?> map)) {
            throw new IllegalArgumentException("JSON-контент должен начинаться с объекта");
        }
        @SuppressWarnings("unchecked")
        Map<String, Object> result = (Map<String, Object>) map;
        return result;
    }

    private Object parseValue() {
        skipWhitespace();
        if (index >= text.length()) {
            throw new IllegalArgumentException("Неожиданный конец JSON");
        }
        char c = text.charAt(index);
        return switch (c) {
            case '{' -> parseObjectInternal();
            case '[' -> parseArray();
            case '"' -> parseString();
            case 't', 'f' -> parseBoolean();
            case 'n' -> parseNull();
            default -> parseNumberOrBareWord();
        };
    }

    private Map<String, Object> parseObjectInternal() {
        expect('{');
        Map<String, Object> map = new LinkedHashMap<>();
        skipWhitespace();
        if (peek('}')) {
            expect('}');
            return map;
        }
        while (true) {
            String key = parseString();
            skipWhitespace();
            expect(':');
            Object value = parseValue();
            map.put(key, value);
            skipWhitespace();
            if (peek('}')) {
                expect('}');
                return map;
            }
            expect(',');
        }
    }

    private List<Object> parseArray() {
        expect('[');
        List<Object> list = new ArrayList<>();
        skipWhitespace();
        if (peek(']')) {
            expect(']');
            return list;
        }
        while (true) {
            list.add(parseValue());
            skipWhitespace();
            if (peek(']')) {
                expect(']');
                return list;
            }
            expect(',');
        }
    }

    private String parseString() {
        expect('"');
        StringBuilder sb = new StringBuilder();
        while (index < text.length()) {
            char c = text.charAt(index++);
            if (c == '"') {
                return sb.toString();
            }
            if (c == '\\') {
                if (index >= text.length()) break;
                char next = text.charAt(index++);
                switch (next) {
                    case '"' -> sb.append('"');
                    case '\\' -> sb.append('\\');
                    case '/' -> sb.append('/');
                    case 'b' -> sb.append('\b');
                    case 'f' -> sb.append('\f');
                    case 'n' -> sb.append('\n');
                    case 'r' -> sb.append('\r');
                    case 't' -> sb.append('\t');
                    case 'u' -> {
                        String hex = text.substring(index, index + 4);
                        sb.append((char) Integer.parseInt(hex, 16));
                        index += 4;
                    }
                    default -> sb.append(next);
                }
            } else {
                sb.append(c);
            }
        }
        throw new IllegalArgumentException("Некорректная JSON-строка");
    }

    private Boolean parseBoolean() {
        if (text.startsWith("true", index)) {
            index += 4;
            return true;
        }
        if (text.startsWith("false", index)) {
            index += 5;
            return false;
        }
        throw new IllegalArgumentException("Некорректное boolean-значение JSON");
    }

    private Object parseNull() {
        if (!text.startsWith("null", index)) {
            throw new IllegalArgumentException("Некорректное null-значение JSON");
        }
        index += 4;
        return null;
    }

    private Object parseNumberOrBareWord() {
        int start = index;
        while (index < text.length()) {
            char c = text.charAt(index);
            if (Character.isLetterOrDigit(c) || c == '.' || c == '-' || c == '_' || c == ':') {
                index++;
            } else {
                break;
            }
        }
        String token = text.substring(start, index).trim();
        if (token.isEmpty()) {
            throw new IllegalArgumentException("Ожидалось значение JSON");
        }
        if (token.matches("-?\\d+")) {
            return Long.parseLong(token);
        }
        return token;
    }

    private boolean peek(char c) {
        skipWhitespace();
        return index < text.length() && text.charAt(index) == c;
    }

    private void expect(char c) {
        skipWhitespace();
        if (index >= text.length() || text.charAt(index) != c) {
            throw new IllegalArgumentException("Ожидался символ '" + c + "' в JSON");
        }
        index++;
    }

    private void skipWhitespace() {
        while (index < text.length() && Character.isWhitespace(text.charAt(index))) {
            index++;
        }
    }
}
