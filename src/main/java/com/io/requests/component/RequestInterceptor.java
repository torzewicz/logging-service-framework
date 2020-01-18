package com.io.requests.component;

import com.google.gson.Gson;
import com.io.requests.model.LogData;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Component
public class RequestInterceptor extends HandlerInterceptorAdapter implements EnvironmentAware {
    private Environment environment;

    private RestTemplate restTemplate;
    private String appName;
    private String baseUrl;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String requestId = UUID.randomUUID().toString();
        request.setAttribute("startTime", System.currentTimeMillis());
        request.setAttribute("requestId", requestId);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        super.afterCompletion(request, response, handler, ex);
        long startTime = (Long) request.getAttribute("startTime");

        if (baseUrl != null) {
            try {
                LogData logData = LogData.builder()
                        .requestId(request.getAttribute("requestId").toString())
                        .method(request.getMethod())
                        .requestURI(request.getRequestURI())
                        .appName(appName != null ? appName : "Default App")
                        .requestTimestamp(startTime)
                        .time(System.currentTimeMillis() - startTime)
                        .statusCode(response.getStatus())
                        .build();

                restTemplate.postForEntity(baseUrl, logData, null);
            } catch (Exception e) {
                System.out.println("Couldn't post logs: " + e.getMessage());
            }
        }

    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    RequestInterceptor(Environment environment) {
        this.environment = environment;
        this.appName = environment.getProperty("log.appName");
        this.baseUrl = environment.getProperty("log.baseUrl");

        if (baseUrl == null) {
            System.out.println("Log base Url not set - logs will not be published");
        }

        restTemplate = new RestTemplate();
    }

    void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}

