package com.dudu.oauth;

import java.util.List;

public class Claims {
    private String iss;
    private long exp;
    private long iat;
    private long jti;

    private long userId;
    private List<String> scopes;

    public String getIss() {
        return iss;
    }

    public void setIss(String iss) {
        this.iss = iss;
    }

    public long getExp() {
        return exp;
    }

    public void setExp(long exp) {
        this.exp = exp;
    }

    public long getIat() {
        return iat;
    }

    public void setIat(long iat) {
        this.iat = iat;
    }

    public long getJti() {
        return jti;
    }

    public void setJti(long jti) {
        this.jti = jti;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public List<String> getScopes() {
        return scopes;
    }

    public void setScopes(List<String> scopes) {
        this.scopes = scopes;
    }
}
