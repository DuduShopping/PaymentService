package com.dudu.oauth;

import java.util.Objects;
import java.util.Set;

public class Scope {
    private String scopeName;
    private Set<ApiEndpoint> endpoints;

    public String getScopeName() {
        return scopeName;
    }

    public void setScopeName(String scopeName) {
        this.scopeName = scopeName;
    }

    public Set<ApiEndpoint> getEndpoints() {
        return endpoints;
    }

    public void setEndpoints(Set<ApiEndpoint> endpoints) {
        this.endpoints = endpoints;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Scope scope = (Scope) o;
        return Objects.equals(scopeName, scope.scopeName);
    }

    @Override
    public int hashCode() {

        return Objects.hash(scopeName);
    }
}
