package com.api.tests;

import com.api.base.TestBase;
import com.api.client.Errors;
import com.api.client.RestClient;
import com.api.pojo.Services;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.MapDifference;
import com.google.common.collect.Maps;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertTrue;

public class ServicesTest extends TestBase{
	
	String serviceUrl;
	String apiUrl;
	String url;
	RestClient restClient;
	HttpResponse httpResponse;
	ObjectMapper mapper = new ObjectMapper();
	HashMap<String, String> headerMap;
	
	@BeforeMethod
	public void setUp() {
		restClient = new RestClient();
		headerMap = new HashMap<String, String>();
		headerMap.put("Content-Type", "application/json");
		serviceUrl = prop.getProperty("URL");
		apiUrl = prop.getProperty("serviceURL");
		url = serviceUrl + apiUrl;		
	}
	
	@Test
	public void verifyCreateServiceWithValidData() throws IOException{
		String name="Mobile";
		Services services = new Services(name); 
		mapper.setSerializationInclusion(Include.NON_NULL);
		mapper.writeValue(new File("./src/test/resources/testfiles/services/services.json"), services);
		String usersJsonString = mapper.writeValueAsString(services);
		httpResponse = restClient.post(url, usersJsonString, headerMap); 
		int statusCode = httpResponse.getStatusLine().getStatusCode();
		Assert.assertEquals(statusCode, HttpStatus.SC_CREATED);
		mapper.setSerializationInclusion(Include.ALWAYS);
		String responseString = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
		Services catResObj = mapper.readValue(responseString, Services.class);
		Assert.assertTrue(services.getName().equals(catResObj.getName()));
	}
	
	@Test
	public void verifyCreateServiceWithBlankData() throws IOException{
		String blankString = ""; 
		httpResponse = restClient.post(url, blankString, headerMap); 
		int statusCode = httpResponse.getStatusLine().getStatusCode();
		Assert.assertEquals(statusCode, HttpStatus.SC_BAD_REQUEST);
		
		String responseString = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
		JSONObject responseJson = new JSONObject(responseString);
		List< String > expected=Arrays.asList(Errors.services);
		List<Object> actual=responseJson.getJSONArray("errors").toList();
		Assert.assertTrue(expected.stream().allMatch(num -> actual.contains(num)));
	}
	
	@Test
	public void verifyGetAllServicesByDefault() throws IOException {
		String defaultCat="10";
		httpResponse = restClient.get(url);
		int statusCode = httpResponse.getStatusLine().getStatusCode();	
		Assert.assertEquals(statusCode, HttpStatus.SC_OK, "Status code is not 200");
		String responseString = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
		assertTrue(responseString.contains("\"limit\":"+defaultCat));
	}
	
	@Test
	public void verifyGetServiceByID() throws IOException {
		String serviceId="8";
		httpResponse = restClient.get(url+File.separator+serviceId);
		int statusCode = httpResponse.getStatusLine().getStatusCode();
		Assert.assertEquals(statusCode, HttpStatus.SC_OK, "Status code is not 200");

		String responseString = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
		JSONObject responseJson = new JSONObject(responseString);
		String expectedResponse="./src/test/resources/testfiles/services/getserviceid.json";
		String content = new String(Files.readAllBytes(Paths.get(expectedResponse)),"UTF-8");
		JSONObject expectedResponseJson = new JSONObject(content);
		TypeReference<HashMap<String, Object>> type = new TypeReference<HashMap<String, Object>>() {};
		Map<String, Object> leftMap = mapper.readValue(responseJson.toString(), type);
		Map<String, Object> rightMap = mapper.readValue(expectedResponseJson.toString(), type);
		MapDifference<String, Object> difference = Maps.difference(leftMap, rightMap);
		assertTrue(difference.areEqual());
	}
	
	@Test
	public void verifyGetServiceFilterByLimit() throws IOException {
		String limit="2";
		httpResponse = restClient.get(url+File.separator+"?$limit="+limit);
		int statusCode = httpResponse.getStatusLine().getStatusCode();
		Assert.assertEquals(statusCode, HttpStatus.SC_OK, "Status code is not 200");

		String responseString = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
		assertTrue(responseString.contains("\"limit\":"+limit));
	}
	
	@Test
	public void verifyGetInvalidService() throws IOException {
		String invalidService="423424543";
		httpResponse = restClient.get(url+File.separator+invalidService);
		int statusCode = httpResponse.getStatusLine().getStatusCode();
		Assert.assertEquals(statusCode, HttpStatus.SC_NOT_FOUND, "Status code is not 404");
	}
}
