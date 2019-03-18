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
import com.api.pojo.Categories;
import com.api.pojo.CategoriesResponse;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.MapDifference;
import com.google.common.collect.Maps;

public class CategoriesTest extends TestBase{

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
		apiUrl = prop.getProperty("categoryURL");
		url = serviceUrl + apiUrl;		
	}
	
	@Test
	public void verifyCreateCategoryWithValidData() throws JsonGenerationException, JsonMappingException, IOException{	
		String categoryId="cat"+randomNumber();
		String name="leader";
		Categories categories = new Categories(categoryId, name);
		mapper.writeValue(new File("./src/test/resources/testfiles/categories.json"), categories);
		String jsonString = mapper.writeValueAsString(categories);
		httpResponse = restClient.post(url, jsonString, headerMap); 

		int statusCode = httpResponse.getStatusLine().getStatusCode();
		Assert.assertEquals(statusCode, RESPONSE_STATUS_CODE_201);
		
		String responseString = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
		CategoriesResponse catResObj = mapper.readValue(responseString, CategoriesResponse.class);		
		Assert.assertTrue(categories.getId().equals(catResObj.getId()));
		Assert.assertTrue(categories.getName().equals(catResObj.getName()));
	}	
	 
	@Test
	public void verifyCreateCategoryWithBlankData() throws JsonGenerationException, JsonMappingException, IOException{
		String blankString = ""; 
		httpResponse = restClient.post(url, blankString, headerMap); 
		int statusCode = httpResponse.getStatusLine().getStatusCode();
		Assert.assertEquals(statusCode, RESPONSE_STATUS_CODE_400);
		
		String responseString = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
		JSONObject responseJson = new JSONObject(responseString);
		List< String > expected=Arrays.asList(Errors.categories);
		List<Object> actual=responseJson.getJSONArray("errors").toList();
		Assert.assertTrue(expected.stream().allMatch(num1 -> actual.contains(num1)));
	}
	
	@Test
	public void verifyGetAllCategoriesByDefault() throws ParseException, IOException {
		String defaultCat="10";
		httpResponse = restClient.get(url);
		int statusCode = httpResponse.getStatusLine().getStatusCode();	
		Assert.assertEquals(statusCode, RESPONSE_STATUS_CODE_200, "Status code is not 200");
		String responseString = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
		assertTrue(responseString.contains("\"limit\":"+defaultCat));
	}
	
	@Test
	public void verifyGetCategoryByID() throws ParseException, IOException {
		String catId="abcat0010000";
		httpResponse = restClient.get(url+File.separator+catId);
		int statusCode = httpResponse.getStatusLine().getStatusCode();
		Assert.assertEquals(statusCode, RESPONSE_STATUS_CODE_200, "Status code is not 200");

		String responseString = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
		JSONObject responseJson = new JSONObject(responseString);
		String expectedResponse="./src/test/resources/testfiles/getcategoryid.json";
		String content = new String(Files.readAllBytes(Paths.get(expectedResponse)),"UTF-8");
		JSONObject expectedResponseJson = new JSONObject(content);
		TypeReference<HashMap<String, Object>> type = new TypeReference<HashMap<String, Object>>() {};
		Map<String, Object> leftMap = mapper.readValue(responseJson.toString(), type);
		Map<String, Object> rightMap = mapper.readValue(expectedResponseJson.toString(), type);
		MapDifference<String, Object> difference = Maps.difference(leftMap, rightMap);
		assertTrue(difference.areEqual());
	}
	
	@Test
	public void verifyGetCategoryFilterByLimit() throws ParseException, IOException {
		String limit="2";
		httpResponse = restClient.get(url+File.separator+"?$limit="+limit);
		int statusCode = httpResponse.getStatusLine().getStatusCode();
		Assert.assertEquals(statusCode, RESPONSE_STATUS_CODE_200, "Status code is not 200");

		String responseString = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
		assertTrue(responseString.contains("\"limit\":"+limit));
	}
	
	@Test
	public void verifyGetInvalidCategory() throws ParseException, IOException {
		String invalidCat="423424543";
		httpResponse = restClient.get(url+File.separator+invalidCat);
		int statusCode = httpResponse.getStatusLine().getStatusCode();
		Assert.assertEquals(statusCode, RESPONSE_STATUS_CODE_404, "Status code is not 404");
	}
}
