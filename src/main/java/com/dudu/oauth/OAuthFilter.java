package com.dudu.oauth;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.time.Instant;
import java.util.Date;

@Component
public class OAuthFilter extends GenericFilterBean {

    public static final String USER = "USER";
    private static final Logger logger = LogManager.getLogger(OAuthFilter.class);

    private PermissionManager permissionManager;
    private TokenDecoder tokenDecoder;

    public OAuthFilter(PermissionManager permissionManager, TokenDecoder tokenDecoder) {
        this.permissionManager = permissionManager;
        this.tokenDecoder = tokenDecoder;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        logger.info("Oauth filter");

        var httpRequest = (HttpServletRequest) request;

        String contextPath = httpRequest.getContextPath();

        String urlStr = httpRequest.getRequestURL().toString();
        URL url = new URL(urlStr);
        var path = url.getPath();

        var endpoint = path.substring(contextPath.length());
        var method = httpRequest.getMethod();

        if (!permissionManager.contains(endpoint, method)) {
            makeResponse(response, HttpStatus.NOT_FOUND, "API is not found");
            return;
        }

        if (permissionManager.isPublic(endpoint, method)) {
            // this is public url
            chain.doFilter(request, response);
            return;
        }

        // check token
        String authHeader = httpRequest.getHeader("Authorization");
        if (authHeader == null) {
            makeResponse(response, HttpStatus.UNAUTHORIZED, "");
            return;
        }

        if (!authHeader.startsWith("Bearer")) {
            makeResponse(response, HttpStatus.UNAUTHORIZED, "Expect Bearer authentication");
            return;
        }

        var token = authHeader.substring("Bearer".length()).trim();
        Claims claims;
        try {
            claims = tokenDecoder.getClaims(token);
        } catch (Exception e) {
            logger.debug("", e);
            makeResponse(response, HttpStatus.UNAUTHORIZED, "Invalid token");
            return;
        }

        // check if the user is permitted to this url
        if (claims.getScopes() == null || claims.getScopes().size() == 0) {
            makeResponse(response, HttpStatus.UNAUTHORIZED, "Token contains no scope");
            return;
        }

        var permitted = false;
        for (var scope : claims.getScopes()) {
            if (permissionManager.isPermitted(scope, endpoint, method)) {
                permitted = true;
                break;
            }
        }

        if (!permitted) {
            makeResponse(response, HttpStatus.UNAUTHORIZED, "Insufficient scope");
            return;
        }

        User user = new User();
        user.setUserId(claims.getUserId());
        user.setExpiredAt(new Date(claims.getExp() * 1000));
        user.setIssuedAt(new Date(claims.getIat() *1000));

        request.setAttribute(USER, user);
        chain.doFilter(request, response);
    }

    private void makeResponse(ServletResponse response, HttpStatus httpStatus, String message) {
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        httpServletResponse.setStatus(httpStatus.value());

        JSONObject body = new JSONObject();
        body.put("timestamp", Instant.now().toString());
        body.put("status", httpStatus.value());
        body.put("message", message);
        var bodyJson = body.toString();

        httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
        httpServletResponse.setContentLengthLong(bodyJson.length());

        try (PrintWriter writer = httpServletResponse.getWriter()) {
            writer.write(body.toString());
        } catch (IOException e) {
            logger.warn("", e);
        }

    }
}
