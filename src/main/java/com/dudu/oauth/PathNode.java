package com.dudu.oauth;

import java.util.Map;
import java.util.Set;

/**
 *
 */
public class PathNode {
    // null on it is a leaf
    private Map<String, PathNode> children;

    // path node value. * indicates wildcard.
    private String value;

    // nullable.
    private ApiEndpoint apiEndpoint;

    private Set<String> scopes;

    public PathNode(String value) {
        this.value = value;
    }

    public Map<String, PathNode> getChildren() {
        return children;
    }

    public void setChildren(Map<String, PathNode> children) {
        this.children = children;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public ApiEndpoint getApiEndpoint() {
        return apiEndpoint;
    }

    public void setApiEndpoint(ApiEndpoint apiEndpoint) {
        this.apiEndpoint = apiEndpoint;
    }

    public Set<String> getScopes() {
        return scopes;
    }

    public void setScopes(Set<String> scopes) {
        this.scopes = scopes;
    }
}
