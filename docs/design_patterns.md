# Использованные паттерны

## 1. Factory + Builder
`MissionFactory` и `MissionBuilder` централизуют создание объектов доменной модели.

## 2. Template Method
`AbstractMissionParser` определяет общий шаблон разбора файла: чтение контента, создание builder, маппинг в модель, возврат результата.

## 3. Strategy
`ReportRenderer` задает стратегию формирования итогового текста. Сейчас есть `SummaryReportRenderer` и `DetailedReportRenderer`.

## 4. Registry / Plugin Extension Point
`ParserRegistry` и `ReportRendererRegistry` используют `ServiceLoader`, поэтому новые парсеры и новые рендереры можно добавлять как расширения.
