package com.api.tests;

import com.api.base.TestBase;
import com.api.client.Errors;
import com.api.client.RestClient;
import com.api.pojo.Products;
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

public class ProductsTest extends TestBase {

    String url;
    RestClient restClient;
    HttpResponse httpResponse;
    ObjectMapper mapper = new ObjectMapper();

    @BeforeMethod
    public void setUp() {
        restClient = new RestClient();
        String serviceUrl = prop.getProperty("URL");
        String apiUrl = prop.getProperty("productURL");
        url = format("%s%s", serviceUrl, apiUrl);
    }

    @Test
    public void verifyCreateProductWithValidData() throws IOException {
        var name = "Mobile";
        var description = "Testing product";
        var upc = "10001";
        var type = "Electronics";
        var model = "iPhone";
        var products = new Products(name, description, upc, type, model);
        mapper.setSerializationInclusion(NON_EMPTY);
        var body = mapper.writeValueAsString(products);
        httpResponse = restClient.post(url, body);

        assertThat(httpResponse.getStatusLine().getStatusCode()).isEqualTo(SC_CREATED);
        mapper.setSerializationInclusion(ALWAYS);
        var response = EntityUtils.toString(httpResponse.getEntity(), StandardCharsets.UTF_8);
        var productResponse = mapper.readValue(response, Products.class);
        assertThat(productResponse)
            .hasFieldOrPropertyWithValue("name", productResponse.name())
            .hasFieldOrPropertyWithValue("model", productResponse.model());
    }

    @Test
    public void verifyCreateProductWithBlankData() throws IOException {
        var body = "";
        httpResponse = restClient.post(url, body);

        assertThat(httpResponse.getStatusLine().getStatusCode()).isEqualTo(SC_BAD_REQUEST);
        var response = EntityUtils.toString(httpResponse.getEntity(), StandardCharsets.UTF_8);
        var errorResponse = new JSONObject(response);
        var expected = Arrays.asList(Errors.products);
        var actual = errorResponse.getJSONArray("errors").toList();
        assertThat(actual).hasSameElementsAs(expected);
    }

    @Test
    public void verifyGetAllProductsByDefault() throws IOException {
        var defaultCat = "10";
        httpResponse = restClient.get(url);

        assertThat(httpResponse.getStatusLine().getStatusCode()).isEqualTo(SC_OK);
        var response = EntityUtils.toString(httpResponse.getEntity(), StandardCharsets.UTF_8);
        assertThat(response).contains(format("\"limit\":%s", defaultCat));
    }

    @Test
    public void verifyGetProductByID() throws IOException {
        var proId = "127687";
        httpResponse = restClient.get(format("%s%s%s", url, File.separator, proId));

        assertThat(httpResponse.getStatusLine().getStatusCode()).isEqualTo(SC_OK);
        var response = EntityUtils.toString(httpResponse.getEntity(), StandardCharsets.UTF_8);
        var content = Files.readString(Paths.get(Objects.requireNonNull(getClass()
            .getClassLoader()
            .getResource("testfiles/products/getproductid.json"))
            .getFile()), StandardCharsets.UTF_8);
        var type = new TypeReference<HashMap<String, Object>>() {};
        var actual = mapper.readValue(new JSONObject(response).toString(), type);
        var expected = mapper.readValue(new JSONObject(content).toString(), type);
        var difference = Maps.difference(actual, expected);
        assertThat(difference.areEqual()).isTrue();
    }

    @Test
    public void verifyGetProductFilterByLimit() throws IOException {
        var limit = "2";
        httpResponse = restClient.get(format("%s%s?$limit=%s", url, File.separator, limit));

        assertThat(httpResponse.getStatusLine().getStatusCode()).isEqualTo(SC_OK);
        var response = EntityUtils.toString(httpResponse.getEntity(), StandardCharsets.UTF_8);
        assertThat(response).contains(format("\"limit\":%s", limit));
    }

    @Test
    public void verifyGetInvalidProduct() throws IOException {
        var invalidProduct = "423424543";
        httpResponse = restClient.get(format("%s%s%s", url, File.separator, invalidProduct));
        assertThat(httpResponse.getStatusLine().getStatusCode()).isEqualTo(SC_NOT_FOUND);
    }
}
