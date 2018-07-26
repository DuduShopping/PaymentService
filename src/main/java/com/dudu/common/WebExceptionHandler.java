package com.dudu.common;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Instant;

@ControllerAdvice
public class WebExceptionHandler extends ResponseEntityExceptionHandler {
    private static final Logger logger = LogManager.getLogger(WebExceptionHandler.class);

    @ExceptionHandler(value = {HttpClientErrorException.class, HttpServerErrorException.class})
    public ResponseEntity<Object> handleHttpClientErrorException(HttpStatusCodeException httpException, WebRequest request) {
        if (httpException instanceof HttpClientErrorException)
            logger.warn(httpException);
        else if (httpException instanceof HttpServerErrorException)
            logger.error(httpException);

        var body = new JSONObject();
        body.put("timeshtamp", Instant.now().toString());
        body.put("status", httpException.getStatusCode().value());
        body.put("message", httpException.getMessage());

        try {
            var urlStr = ((ServletWebRequest) request).getRequest().getRequestURL().toString();
            var url = new URL(urlStr);
            var path = url.getPath();
            body.put("path", path);
        } catch (MalformedURLException e) {
            logger.error(e);
        }
        var headers = new HttpHeaders();
        headers.set("Content-Type", MediaType.APPLICATION_JSON.toString());

        return handleExceptionInternal(httpException, body.toString(), headers, httpException.getStatusCode(), request);
    }
}
