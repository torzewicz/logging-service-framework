package com.io.requests.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LogData {

    private String appName;
    private long timestamp;
    private String method;
    private long elapsedTime;
    private String path;
    private int statusCode;
    private int maxRpm;
    private String requestId;
}
