package com.api.tests;

import com.api.base.TestBase;
import com.api.client.Errors;
import com.api.client.RestClient;
import com.api.pojo.Stores;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.ALWAYS;
import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;
import static java.lang.String.format;
import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_NOT_FOUND;
import static org.apache.http.HttpStatus.SC_OK;
import static org.assertj.core.api.Assertions.assertThat;

public class StoresTest extends TestBase {

    String url;
    RestClient restClient;
    HttpResponse httpResponse;
    ObjectMapper mapper = new ObjectMapper();

    @BeforeMethod
    public void setUp() {
        restClient = new RestClient();
        String serviceUrl = prop.getProperty("URL");
        String apiUrl = prop.getProperty("storeURL");
        url = format("%s%s", serviceUrl, apiUrl);
    }

    @Test
    public void verifyCreateStoreWithValidData() throws IOException {
        var name = "Test1 Store";
        var address = "321 Street 4";
        var city = "Gurugram";
        var state = "HR";
        var zip = "122001";
        var Stores = new Stores(name, address, city, state, zip);

        mapper.setSerializationInclusion(NON_EMPTY);
        var body = mapper.writeValueAsString(Stores);
        httpResponse = restClient.post(url, body);

        assertThat(httpResponse.getStatusLine().getStatusCode()).isEqualTo(SC_CREATED);
        mapper.setSerializationInclusion(ALWAYS);
        var response = EntityUtils.toString(httpResponse.getEntity(), StandardCharsets.UTF_8);
        var storeResponse = mapper.readValue(response, com.api.pojo.Stores.class);
        assertThat(storeResponse)
            .hasFieldOrPropertyWithValue("name", storeResponse.name())
            .hasFieldOrPropertyWithValue("city", storeResponse.city());
    }

    @Test
    public void verifyCreateStoreWithBlankData() throws IOException {
        var body = "";
        httpResponse = restClient.post(url, body);

        assertThat(httpResponse.getStatusLine().getStatusCode()).isEqualTo(SC_BAD_REQUEST);
        var response = EntityUtils.toString(httpResponse.getEntity(), StandardCharsets.UTF_8);
        var errorResponse = new JSONObject(response);
        var expected = Arrays.asList(Errors.stores);
        var actual = errorResponse.getJSONArray("errors").toList();
        assertThat(actual).hasSameElementsAs(expected);
    }

    @Test
    public void verifyGetAllStoreByDefault() throws IOException {
        var defaultCat = "10";
        httpResponse = restClient.get(url);

        assertThat(httpResponse.getStatusLine().getStatusCode()).isEqualTo(SC_OK);
        var response = EntityUtils.toString(httpResponse.getEntity(), StandardCharsets.UTF_8);
        assertThat(response).contains(format("\"limit\":%s", defaultCat));
    }

    @Test
    public void verifyGetStoreByID() throws IOException {
        var storeId = "8";
        httpResponse = restClient.get(format("%s%s%s", url, File.separator, storeId));

        assertThat(httpResponse.getStatusLine().getStatusCode()).isEqualTo(SC_OK);
        var response = EntityUtils.toString(httpResponse.getEntity(), StandardCharsets.UTF_8);
        var content = Files.readString(Paths.get(Objects.requireNonNull(getClass()
            .getClassLoader()
            .getResource("testfiles/stores/getstoreid.json"))
            .getFile()), StandardCharsets.UTF_8);
        var type = new TypeReference<HashMap<String, Object>>() {};
        var actual = mapper.readValue(new JSONObject(response).toString(), type);
        var expected = mapper.readValue(new JSONObject(content).toString(), type);
        var difference = Maps.difference(actual, expected);
        assertThat(difference.areEqual()).isTrue();
    }

    @Test
    public void verifyGetStoreFilterByLimit() throws IOException {
        var limit = "2";
        httpResponse = restClient.get(format("%s%s?$limit=%s", url, File.separator, limit));

        assertThat(httpResponse.getStatusLine().getStatusCode()).isEqualTo(SC_OK);
        var response = EntityUtils.toString(httpResponse.getEntity(), StandardCharsets.UTF_8);
        assertThat(response).contains(format("\"limit\":%s", limit));
    }

    @Test
    public void verifyGetInvalidStore() throws IOException {
        var invalidStore = "423424543";
        httpResponse = restClient.get(format("%s%s%s", url, File.separator, invalidStore));
        assertThat(httpResponse.getStatusLine().getStatusCode()).isEqualTo(SC_NOT_FOUND);
    }
}
