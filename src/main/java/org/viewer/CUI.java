package org.viewer;

import org.service.MissionAnalyzerService;
import org.service.MissionLoadResult;

import java.util.Scanner;

public class CUI {
    private final MissionAnalyzerService service;

    public CUI(MissionAnalyzerService service) {
        this.service = service;
    }

    public void showInterface(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Стабилизированный анализатор миссий магов");
        System.out.println("Поддерживаемые форматы: " + service.supportedFormats());
        System.out.println("Поддерживаемые отчеты: " + service.supportedReports());

        while (true) {
            System.out.print(args.length > 0 ? "" : "Введите путь к файлу миссии или exit: ");
            String rawPath = args.length > 0 ? args[0] : scanner.nextLine();
            if (rawPath == null || rawPath.equalsIgnoreCase("exit")) break;

            String reportType;
            if (args.length > 1) {
                reportType = args[1];
            } else if (args.length > 0) {
                reportType = "summary";
            } else {
                System.out.print("Тип отчета (summary/detailed, Enter = summary): ");
                String input = scanner.nextLine();
                reportType = input == null || input.isBlank() ? "summary" : input.trim();
            }

            try {
                MissionLoadResult result = service.analyze(rawPath, reportType);
                System.out.println("Формат распознан: " + result.parserFormat());
                System.out.println("Тип отчета: " + result.reportType());
                System.out.println(result.reportText());
            } catch (Exception e) {
                System.out.println("Ошибка: " + e.getMessage());
            }

            if (args.length > 0) break;
        }
    }
}
