package com.axisbank.request.statement.encryption;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import org.springframework.http.MediaType;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

public class CustomHttpServletRequestWrapper extends HttpServletRequestWrapper {

    private ByteArrayInputStream requestBody;

    public CustomHttpServletRequestWrapper(HttpServletRequest request, byte[] decryptedString) {
        super(request);
        try {
            requestBody = new ByteArrayInputStream(decryptedString);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getHeader(String headerName) {
        String headerValue = super.getHeader(headerName);
        if ("Accept".equalsIgnoreCase(headerName)) {
            return headerValue.replaceAll(MediaType.TEXT_PLAIN_VALUE, MediaType.APPLICATION_JSON_VALUE);
        } else if ("Content-Type".equalsIgnoreCase(headerName)) {
            return headerValue.replaceAll(MediaType.TEXT_PLAIN_VALUE, MediaType.APPLICATION_JSON_VALUE);
        }
        return headerValue;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public Enumeration getHeaders(final String headerName) {
        HttpServletRequest request = (HttpServletRequest) getRequest();
        List list = new ArrayList();
        Enumeration e = request.getHeaders(headerName);
        while (e.hasMoreElements()) {
            String header = e.nextElement().toString();
            if (header.equalsIgnoreCase(MediaType.TEXT_PLAIN_VALUE)) {
                header = MediaType.APPLICATION_JSON_VALUE;
            }
            list.add(header);
        }
        return Collections.enumeration(list);
    }

    @Override
    public String getContentType() {
        String contentTypeValue = super.getContentType();
        if (MediaType.TEXT_PLAIN_VALUE.equalsIgnoreCase(contentTypeValue)) {
            return MediaType.APPLICATION_JSON_VALUE;
        }
        return contentTypeValue;
    }

    @Override
    public BufferedReader getReader() throws UnsupportedEncodingException {
        return new BufferedReader(new InputStreamReader(requestBody, "UTF-8"));
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        return new ServletInputStream() {
            @Override
            public int read() {
                return requestBody.read();
            }

            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener listener) {
            }
        };
    }

}