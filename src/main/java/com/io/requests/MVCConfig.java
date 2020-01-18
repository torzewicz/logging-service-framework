package com.io.requests;

import com.io.requests.component.RequestInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MVCConfig implements WebMvcConfigurer {

    private final RequestInterceptor requestInterceptor;

    public MVCConfig(RequestInterceptor requestInterceptor) {
        this.requestInterceptor = requestInterceptor;
    }

    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(requestInterceptor).addPathPatterns("/**");
    }

}
