package api;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;

public class ApiHandler {

    static DefaultHttpClient httpClient;

    public ApiHandler() {
    }

    public CloseableHttpResponse execute(HttpRequestBase request) {
        httpClient = new DefaultHttpClient();
        try {
            CloseableHttpResponse response = httpClient.execute(request);
            httpClient.close();
            return response;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
