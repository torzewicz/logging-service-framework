package com.io.requests.annotation;

import com.io.requests.MVCConfig;
import com.io.requests.component.RequestInterceptor;
import com.io.requests.model.LogData;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Configuration
@Import({RequestInterceptor.class, MVCConfig.class, LogData.class})
public @interface EnableLoggingService {


}


