package com.api.client;

import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;

import static org.apache.http.entity.ContentType.APPLICATION_JSON;

public class RestClient {

    private CloseableHttpClient httpClient;

    public RestClient() {
        httpClient = HttpClients.custom().build();
    }

    public HttpResponse get(String url) throws IOException {
        HttpUriRequest request = RequestBuilder
            .get(url)
            .addHeader(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON.getMimeType())
            .build();

        return httpClient.execute(request);
    }

    public HttpResponse post(String url, String entity) throws IOException {
        HttpUriRequest request = RequestBuilder
            .post(url)
            .setEntity(new StringEntity(entity))
            .addHeader(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON.getMimeType())
            .build();

        return httpClient.execute(request);
    }
}
