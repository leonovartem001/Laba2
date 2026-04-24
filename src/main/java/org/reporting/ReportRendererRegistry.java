package org.reporting;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.ServiceLoader;

public class ReportRendererRegistry {
    private final List<ReportRenderer> renderers = new ArrayList<>();

    public ReportRendererRegistry() {
        ServiceLoader.load(ReportRenderer.class).forEach(renderers::add);
        renderers.sort(Comparator.comparing(ReportRenderer::getName));
    }

    public List<ReportRenderer> getRenderers() {
        return renderers;
    }

    public ReportRenderer findByName(String name) {
        if (name == null || name.isBlank()) {
            return getDefault();
        }
        String normalized = name.trim().toLowerCase(Locale.ROOT);
        return renderers.stream()
                .filter(renderer -> renderer.getName().equalsIgnoreCase(normalized))
                .findFirst()
                .orElseGet(this::getDefault);
    }

    public ReportRenderer getDefault() {
        return renderers.stream()
                .filter(renderer -> renderer.getName().equalsIgnoreCase("summary"))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Не найден отчет summary"));
    }
}
