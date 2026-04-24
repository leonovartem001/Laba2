# Лабораторная работа №2 — Стабилизированный анализатор миссий магов

Проект является развитием лабораторной работы №1. Приложение по-прежнему читает файлы миссий и выводит сводку, но внутренняя архитектура переработана с применением паттернов проектирования.

## Что изменено

- **Factory + Builder** — создание объектов доменной модели централизовано в `MissionFactory` и `MissionBuilder`.
- **Template Method** — общий алгоритм парсинга сосредоточен в `AbstractMissionParser`.
- **Strategy** — формирование отчета вынесено в `ReportRenderer`.
- **Registry + ServiceLoader** — новые форматы входных данных и новые типы отчетов подключаются без изменения основной логики приложения.

## Поддерживаемые форматы

- TXT (старый плоский формат из лабы 1 и новый секционный формат)
- JSON
- XML
- YAML / YML
- событийный протокол (`MISSION_CREATED|...`)

## Поддерживаемые типы отчета

- `summary` — краткая сводка (по умолчанию)
- `detailed` — детализированный отчет

## Как запустить

### Из IntelliJ IDEA
Открыть проект как Maven-проект и запустить `org.Main`.

### Через Maven
```bash
mvn package
```

После сборки исполняемый JAR будет создан в папке `target`.

### Передача файла при запуске
```bash
java -jar target/mission-analyzer-lab2-2.0.jar src/main/resources/examples/mission_lab2.yaml
```

### Выбор типа отчета
```bash
java -jar target/mission-analyzer-lab2-2.0.jar src/main/resources/examples/mission_lab2.xml detailed
```
