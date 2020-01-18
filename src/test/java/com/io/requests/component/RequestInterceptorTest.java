package com.io.requests.component;

import com.io.requests.component.stub.CustomEnvironment;
import com.io.requests.component.stub.CustomServletRequest;
import com.io.requests.component.stub.CustomServletResponse;
import com.io.requests.model.LogData;
import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class RequestInterceptorTest {

    private RequestInterceptor requestInterceptor;
    private HttpServletRequest httpServletRequest;
    private HttpServletResponse httpServletResponse;

    @Mock
    RestTemplate restTemplate;

    @Before
    public void setUp() {
        initMocks(this);
        when(restTemplate.postForEntity(any(), any(), any())).thenReturn(ResponseEntity.accepted().body("OK"));
        this.requestInterceptor = new RequestInterceptor(new CustomEnvironment());
        this.requestInterceptor.setRestTemplate(restTemplate);
        httpServletRequest = new CustomServletRequest();
        httpServletResponse = new CustomServletResponse();
    }

    @Test
    public void shouldAddPropertyToRequest() {
        //given
        //when
        requestInterceptor.preHandle(httpServletRequest, httpServletResponse, null);

        //then
        assertThat(httpServletRequest.getAttribute("startTime")).isNotNull();
        assertThat(httpServletRequest.getAttribute("requestId")).isNotNull();
    }

    @Test
    @SneakyThrows
    public void shouldCollectDataAndPostToLogService() {
        //given
        long timestamp = System.currentTimeMillis();
        httpServletRequest.setAttribute("startTime", timestamp);
        String requestId = UUID.randomUUID().toString();
        httpServletRequest.setAttribute("requestId", requestId);

        ArgumentCaptor<LogData> responseLogData = ArgumentCaptor.forClass(LogData.class);

        //when
        requestInterceptor.afterCompletion(httpServletRequest, httpServletResponse, null, null);

        //then
        verify(restTemplate, times(1)).postForEntity(eq("123"), responseLogData.capture(), eq(null));
        assertThat(responseLogData.getValue().getAppName()).isEqualTo("test");
        assertThat(responseLogData.getValue().getMethod()).isEqualTo("GET");
        assertThat(responseLogData.getValue().getRequestURI()).isEqualTo("URI");
        assertThat(responseLogData.getValue().getRequestTimestamp()).isGreaterThan(0);
        assertThat(responseLogData.getValue().getRequestId()).isEqualTo(requestId);
    }

}
