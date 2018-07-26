package com.dudu.oauth;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Configuration
@EnableConfigurationProperties(OAuthProperties.class)
public class OAuthConfiguration {
    private static final Logger logger = LogManager.getLogger(PermissionManager.class);
    private OAuthProperties properties;

    public OAuthConfiguration(OAuthProperties properties) {
        this.properties = properties;
    }

    @Bean
    public PermissionManager getPermissionManager() throws Exception {
        logger.info("Setting up permissions system");
        var manager = new PermissionManager(properties.getPermissionsFile());
        return manager;
    }


    @Bean
    public TokenDecoder getTokenDecoder() throws Exception {
        if (properties.getPubKeyFile() == null)
            return null;

        try (InputStream in = new FileInputStream(properties.getPubKeyFile())) {
            return new TokenDecoder(new String(in.readAllBytes(), StandardCharsets.US_ASCII));
        }
    }
}
