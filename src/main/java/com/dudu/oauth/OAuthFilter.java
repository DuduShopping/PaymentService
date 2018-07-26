package com.dudu.oauth;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URL;

@Component
public class OAuthFilter extends GenericFilterBean {

    public static final String USER_TOKEN = "LOGGED_USER";
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
        logger.debug(endpoint + ", " + method);

        if (permissionManager.isPublic(endpoint, method)) {
            // this is public url
            chain.doFilter(request, response);
            return;
        }

        // check token
        String authHeader = httpRequest.getHeader("Authorization");
        if (authHeader == null)
            throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED);

        if (!authHeader.startsWith("Bearer"))
            throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED, "Expect Bearer authentication");

        var token = authHeader.substring("Bearer".length()).trim();
        Claims claims;
        try {
            claims = tokenDecoder.getClaims(token);
        } catch (Exception e) {
            logger.debug(e.toString());
            throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED, "Invalid token");
        }

        // check if the user is permitted to this url
        if (claims.getScopes() == null || claims.getScopes().size() == 0)
            throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED, "Token contains no scope");

        var permitted = false;
        for (var scope : claims.getScopes()) {
            if (permissionManager.isPermitted(scope, endpoint, method)) {
                permitted = true;
                break;
            }
        }

        if (!permitted)
            throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED, "Insufficient scope");

        request.setAttribute(USER_TOKEN, token);
        chain.doFilter(request, response);
    }
}
