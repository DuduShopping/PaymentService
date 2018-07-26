package com.dudu.oauth;

import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.validation.constraints.NotEmpty;

@ConfigurationProperties("dudu.oauth")
public class OAuthProperties {
    @NotEmpty
    private String pubKeyFile;

    @NotEmpty
    private String permissionsFile;

    public String getPubKeyFile() {
        return pubKeyFile;
    }

    public void setPubKeyFile(String pubKeyFile) {
        this.pubKeyFile = pubKeyFile;
    }

    public String getPermissionsFile() {
        return permissionsFile;
    }

    public void setPermissionsFile(String permissionsFile) {
        this.permissionsFile = permissionsFile;
    }
}
