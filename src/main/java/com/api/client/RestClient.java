package com.api.client;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;

public class RestClient {

	public HttpResponse get(String url) throws IOException {
		HttpClient httpClient = HttpClients.createDefault();
		HttpGet httpget = new HttpGet(url);
		HttpResponse httpResponse = httpClient.execute(httpget);
		return httpResponse;
	}

	public HttpResponse get(String url, HashMap<String, String> headerMap) throws IOException {
		HttpClient httpClient = HttpClients.createDefault();
		HttpGet httpget = new HttpGet(url);

		for (Map.Entry<String, String> entry : headerMap.entrySet()) {
			httpget.addHeader(entry.getKey(), entry.getValue());
		}
		HttpResponse httpResponse = httpClient.execute(httpget);
		return httpResponse;

	}

	public HttpResponse post(String url, String entityString, HashMap<String, String> headerMap)
			throws IOException {
		HttpClient httpClient = HttpClients.createDefault();
		HttpPost httppost = new HttpPost(url);
		httppost.setEntity(new StringEntity(entityString));

		for (Map.Entry<String, String> entry : headerMap.entrySet()) {
			httppost.addHeader(entry.getKey(), entry.getValue());
		}

		HttpResponse httpResponse = httpClient.execute(httppost);
		return httpResponse;
	}
}
