package com.api.tests;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.api.base.TestBase;
import com.api.client.Errors;
import com.api.client.RestClient;
import com.api.pojo.Stores;
import com.api.pojo.StoresResponse;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.MapDifference;
import com.google.common.collect.Maps;

public class StoresTest extends TestBase{
	
	String serviceUrl;
	String apiUrl;
	String url;
	RestClient restClient;
	HttpResponse httpResponse;
	ObjectMapper mapper = new ObjectMapper();
	HashMap<String, String> headerMap;
	
	@BeforeMethod
	public void setUp() throws ClientProtocolException, IOException{
		restClient = new RestClient();
		headerMap = new HashMap<String, String>();
		headerMap.put("Content-Type", "application/json");
		serviceUrl = prop.getProperty("URL");
		apiUrl = prop.getProperty("storeURL");
		url = serviceUrl + apiUrl;		
	}
	
	@Test
	public void verifyCreateStoreWithValidData() throws JsonGenerationException, JsonMappingException, IOException{
		String name="Test1 Store";
		String address="321 Street 4";
		String city="Gurugram";
		String state="HR";
		String zip="122001";
		Stores Stores = new Stores(name, address, city, state, zip); 
		mapper.writeValue(new File("./src/test/resources/testfiles/stores.json"), Stores);
		String usersJsonString = mapper.writeValueAsString(Stores);
		httpResponse = restClient.post(url, usersJsonString, headerMap); 
		int statusCode = httpResponse.getStatusLine().getStatusCode();
		Assert.assertEquals(statusCode, RESPONSE_STATUS_CODE_201);
		
		String responseString = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
		StoresResponse catResObj = mapper.readValue(responseString, StoresResponse.class); 
		Assert.assertTrue(Stores.getName().equals(catResObj.getName()));
		Assert.assertTrue(Stores.getCity().equals(catResObj.getCity()));		
	}	
	
	@Test
	public void verifyCreateStoreWithBlankData() throws JsonGenerationException, JsonMappingException, IOException{
		String blankString = ""; 
		httpResponse = restClient.post(url, blankString, headerMap); 
		int statusCode = httpResponse.getStatusLine().getStatusCode();
		Assert.assertEquals(statusCode, RESPONSE_STATUS_CODE_400);
		
		String responseString = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
		JSONObject responseJson = new JSONObject(responseString);
		List< String > expected=Arrays.asList(Errors.stores);
		List<Object> actual=responseJson.getJSONArray("errors").toList();
		Assert.assertTrue(expected.stream().allMatch(num -> actual.contains(num)));
	}
	
	@Test
	public void verifyGetAllStoreByDefault() throws ParseException, IOException {
		String defaultCat="10";
		httpResponse = restClient.get(url);
		int statusCode = httpResponse.getStatusLine().getStatusCode();	
		Assert.assertEquals(statusCode, RESPONSE_STATUS_CODE_200, "Status code is not 200");
		String responseString = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
		assertTrue(responseString.contains("\"limit\":"+defaultCat));
	}
	
	@Test
	public void verifyGetStoreByID() throws ParseException, IOException {
		String storeId="8";
		httpResponse = restClient.get(url+File.separator+storeId);
		int statusCode = httpResponse.getStatusLine().getStatusCode();
		Assert.assertEquals(statusCode, RESPONSE_STATUS_CODE_200, "Status code is not 200");

		String responseString = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
		JSONObject responseJson = new JSONObject(responseString);
		String expectedResponse="./src/test/resources/testfiles/getstoreid.json";
		String content = new String(Files.readAllBytes(Paths.get(expectedResponse)),"UTF-8");
		JSONObject expectedResponseJson = new JSONObject(content);
		TypeReference<HashMap<String, Object>> type = new TypeReference<HashMap<String, Object>>() {};
		Map<String, Object> leftMap = mapper.readValue(responseJson.toString(), type);
		Map<String, Object> rightMap = mapper.readValue(expectedResponseJson.toString(), type);
		MapDifference<String, Object> difference = Maps.difference(leftMap, rightMap);
		assertTrue(difference.areEqual());
	}
	
	@Test
	public void verifyGetStoreFilterByLimit() throws ParseException, IOException {
		String limit="2";
		httpResponse = restClient.get(url+File.separator+"?$limit="+limit);
		int statusCode = httpResponse.getStatusLine().getStatusCode();
		Assert.assertEquals(statusCode, RESPONSE_STATUS_CODE_200, "Status code is not 200");

		String responseString = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
		assertTrue(responseString.contains("\"limit\":"+limit));
	}
	
	@Test
	public void verifyGetInvalidStore() throws ParseException, IOException {
		String invalidService="423424543";
		httpResponse = restClient.get(url+File.separator+invalidService);
		int statusCode = httpResponse.getStatusLine().getStatusCode();
		Assert.assertEquals(statusCode, RESPONSE_STATUS_CODE_404, "Status code is not 404");
	}
}
