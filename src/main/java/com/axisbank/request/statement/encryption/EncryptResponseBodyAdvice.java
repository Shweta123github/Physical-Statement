package com.axisbank.request.statement.encryption;

import java.util.logging.Level;

import com.axisbank.request.statement.util.ApplicationUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.java.Log;

@ControllerAdvice
@Log
public class EncryptResponseBodyAdvice implements ResponseBodyAdvice<Object> {

    private final ObjectMapper objectMapper;

    @Autowired
    private AESEncryptionUtil aesEncryptionUtil;

    @Autowired
    public EncryptResponseBodyAdvice(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request,
                                  ServerHttpResponse response) {
        try {
            String isEcrypted = response.getHeaders().getFirst("isEcrypted");
            if (body != null && Boolean.parseBoolean(isEcrypted)) {
                String channel = response.getHeaders().getFirst("channel");
                if (StringUtils.isNotBlank(channel)) {
                    String responseBodyString = ApplicationUtils.convertToStringOrJson(body, objectMapper);
                    response.getHeaders().setContentType(MediaType.TEXT_PLAIN);
                    return aesEncryptionUtil.encrypt(responseBodyString,channel);
                }
            }
        } catch (Exception e) {
            log.log(Level.SEVERE, "Error while encrypting response body", e);
        }
        return body;
    }

}
