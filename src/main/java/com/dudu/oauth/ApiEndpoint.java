package com.dudu.oauth;

import java.util.Objects;

public class ApiEndpoint {
    private long apiEndpointId;
    private String method;
    private String endpoint;
    private boolean isPrivate;
    private boolean isPublic;

    public long getApiEndpointId() {
        return apiEndpointId;
    }

    public void setApiEndpointId(long apiEndpointId) {
        this.apiEndpointId = apiEndpointId;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ApiEndpoint that = (ApiEndpoint) o;
        return apiEndpointId == that.apiEndpointId;
    }

    @Override
    public int hashCode() {

        return Objects.hash(apiEndpointId);
    }
}
