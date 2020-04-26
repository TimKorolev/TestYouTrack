package requests;

import constants.RequestType;
import requests.entities.BaseEntity;
import requests.entities.NewIssueEntity;

import java.util.HashMap;

import static constants.RequestType.*;

public enum Request {

    CREATE_NEW_ISSUE(POST, "api/issues", null, null, new NewIssueEntity()),
    DELETE_ISSUE(DELETE, "api/issues/%s", getNewHeader("Cache-Control", "No-cache"), null, null),
    GET_ISSUE_CONTENT(GET, "api/issues/%s", getNewHeader("Cache-Control", "No-cache"), "fields=$type,id,summary,description", null);

    private final String BASE_URL = System.getProperty("baseUrl");
    private String patternUrl;
    private final RequestType requestType;
    private String fullUrl;
    private HashMap<String, String> headers = getDefaultHeaders();
    private String params;
    private BaseEntity entity;

    Request(RequestType requestType, String patternUrl, HashMap<String, String> headers, String params, BaseEntity entity) {
        this.requestType = requestType;
        this.patternUrl = patternUrl;
        fullUrl = BASE_URL + patternUrl;
        if (headers != null) {
            headers.forEach((key, value) -> this.headers.put(key, value));
        }
        if (requestType == GET) {
            this.params = params;
        }
        if (entity != null && requestType != GET) {
            this.entity = entity;
        }
    }

    private static HashMap<String, String> getDefaultHeaders() {
        HashMap<String, String> headers = new HashMap();
        headers.put("Accept", "Application/json");
        headers.put("Content-type", "Application/json");
        headers.put("Authorization", "Bearer " + System.getProperty("token"));
        return headers;
    }

    private static HashMap<String, String> getNewHeader(String key, String value) {
        HashMap<String, String> paramMap = new HashMap<>();
        paramMap.put(key, value);
        return paramMap;
    }

    public RequestType getRequestType() {
        return requestType;
    }

    public String getFullUrl() {
        return fullUrl;
    }

    public HashMap<String, String> getHeaders() {
        return headers;
    }

    public String getParams() {
        return params;
    }

    public BaseEntity getEntity() {
        return entity;
    }

    public void setParamFullUrl(String paramFullUrl) {
        this.fullUrl = BASE_URL + String.format(patternUrl, paramFullUrl);
    }
}
