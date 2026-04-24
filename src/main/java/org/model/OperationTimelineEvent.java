package org.model;

import java.time.LocalDateTime;

public class OperationTimelineEvent {
    private LocalDateTime timestamp;
    private String type;
    private String description;

    public OperationTimelineEvent() {}

    public OperationTimelineEvent(LocalDateTime timestamp, String type, String description) {
        this.timestamp = timestamp;
        this.type = type;
        this.description = description;
    }

    public LocalDateTime getTimestamp() { return timestamp; }
    public String getType() { return type; }
    public String getDescription() { return description; }

    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    public void setType(String type) { this.type = type; }
    public void setDescription(String description) { this.description = description; }
}
