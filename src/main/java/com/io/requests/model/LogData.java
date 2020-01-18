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

    private String requestId;
    private String method;
    private String requestURI;
    private String appName;
    private long requestTimestamp;
    private long time;
    private int statusCode;
}
