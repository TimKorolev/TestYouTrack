package requests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import requests.entities.BaseEntity;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.logging.Logger;

public class RequestBuilder {

    public static HttpRequestBase getHttpRequest(Request request) {
        return getRequestBase(request);
    }

    private static HttpRequestBase getRequestBase(Request request) {
        HttpRequestBase httpRequestBase = null;
        switch (request.getRequestType()) {

            case GET:
                httpRequestBase = new HttpGet(request.getFullUrl() + getRequestParam(request));
                break;
            case DELETE:
                httpRequestBase = new HttpDelete(request.getFullUrl() + getRequestParam(request));
                break;
            case POST:
                HttpPost httpRequestPost = new HttpPost(request.getFullUrl());
                httpRequestPost.setEntity(getEntity(request));
                httpRequestBase = httpRequestPost;
                break;
            default:
                throw new IllegalStateException("Некорректный тип запроса");
        }
        for (Map.Entry<String, String> header : request.getHeaders().entrySet()) {
            httpRequestBase.setHeader(header.getKey(), header.getValue());
        }
        try {
            Logger.getLogger("main").info("\n>>>>> " + request.getRequestType() + " " + httpRequestBase.getURI()
                    + "\n" + request.getHeaders().toString()
                    + "\n" + ((EntityUtils.toString(getEntity(request)) != null) ? EntityUtils.toString(getEntity(request)) : ""));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return httpRequestBase;
    }

    private static String getRequestParam(Request request) {
        return request.getParams() != null ? "?" + request.getParams() : "";
    }

    private static StringEntity getEntity(Request request) {
        try {
            return new StringEntity(getEntityAsString(request.getEntity()));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String getEntityAsString(BaseEntity entityObj) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(entityObj);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "";
    }

}
