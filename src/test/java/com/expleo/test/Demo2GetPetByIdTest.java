package com.expleo.test;

import com.atlassian.oai.validator.restassured.OpenApiValidationFilter;
import com.expleo.model.Pet;
import com.fasterxml.jackson.databind.JsonNode;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Deserialization - converting byte stream to proper object
 */
public class Demo2GetPetByIdTest {
    public String baseUrl = "https://petstore.swagger.io/v2";

    /**
     * JSONNode --> Tree like structure - felxible structure - avoid if planning schema validation
     * (Not type safety,no auto mapping nested objects, not easy to maintain, not suitable for api automation)
     */
    @Test
    public void demo1GetPetByIdJsonNode() {
        int petId = 5;
        String resource = "/pet/" + petId;

        JsonNode jsonNodeObj = RestAssured
                .given()
                .when().get(baseUrl + resource)
                .then().statusCode(200).extract().as(JsonNode.class);

        System.out.println(jsonNodeObj.get("id"));
        System.out.println(jsonNodeObj.get("name").asText());
        System.out.println(jsonNodeObj.get("category").get("id"));
        System.out.println(jsonNodeObj.get("tags").get(0).get("id").asInt());
        System.out.println(jsonNodeObj.get("photoUrls").get(0));

        Assert.assertEquals(jsonNodeObj.get("id").asInt(), 5);
    }

    /**
     * only advantage is json expression. (Not type safety,no auto mapping nested objects, not easy to maintain, not suitable for api automation)
     */
    @Test
    public void demo2GetPetByIdJsonPath() {
        int petId = 5;
        String resource = "/pet/" + petId;

        JsonPath jsonPathObj = RestAssured
                .given()
                .when().get(baseUrl + resource)
                .then().statusCode(200).extract().jsonPath();

        System.out.println(jsonPathObj.prettify());
        System.out.println(jsonPathObj.getInt("id"));
        System.out.println(jsonPathObj.getString("name"));
        System.out.println(jsonPathObj.getString("category.id"));
        //System.out.println(jsonPathObj.getList("tags[*]"));
    }

    /**
     * Pojo class - type safety, easy maintenance, stable for automation, schema validation
     */
    @Test
    public void demo3GetPetByIdPojoclass() {
        Pet petObj = RestAssured
                .given()
                .pathParam("petId", 5)
                .when().get(baseUrl + "/pet/{petId}")
                .then().statusCode(200).extract().as(Pet.class);

        System.out.println(petObj.getId());
        System.out.println(petObj.getCategory().getId());
        System.out.println(petObj.getTags().get(0).getId());
    }

    @Test
    public void demo4OpenAPISpecVerify() {
        //option 1 - pass yaml file directly
        OpenApiValidationFilter validationFilter = new OpenApiValidationFilter("src/test/resources/petstore.yaml");
        //option 2 - pass file object
        //option 3 - pass url

        Pet petObj = RestAssured
                .given()
                .filter(validationFilter)
                .pathParam("petId", 50)
                .when().get(baseUrl + "/pet/{petId}")
                .then().statusCode(200).extract().as(Pet.class);

        System.out.println(petObj.getId());
        System.out.println(petObj.getCategory().getId());
        System.out.println(petObj.getTags().get(0).getId());
    }

    @Test
    public void demo5FindInvalidPetTest() {
        OpenApiValidationFilter validationFilter = new OpenApiValidationFilter("src/test/resources/petstore.yaml");

        RestAssured
                .given()
                .filter(validationFilter)
                .pathParam("petId", 50787887)
                .when().get(baseUrl + "/pet/{petId}")
                .then().statusCode(404);
    }

    @Test
    public void demo6AddValidPet()
    {
        String jsonBody="{\n" +
                "  \"id\": 556,\n" +
                "  \"category\": {\n" +
                "    \"id\": 0,\n" +
                "    \"name\": \"string\"\n" +
                "  },\n" +
                "  \"name\": \"doggie\",\n" +
                "  \"photoUrls\": [\n" +
                "    \"string\"\n" +
                "  ],\n" +
                "  \"tags\": [\n" +
                "    {\n" +
                "      \"id\": 0,\n" +
                "      \"name\": \"string\"\n" +
                "    }\n" +
                "  ],\n" +
                "  \"status\": \"available\"\n" +
                "}";

        OpenApiValidationFilter validationFilter = new OpenApiValidationFilter("src/test/resources/petstore.yaml");

        Pet petResponse= RestAssured
                .given()
                .filter(validationFilter)
                .contentType("application/json")
                .body(jsonBody)
                .when()
                .post(baseUrl+"/pet")
                .then()
                .statusCode(200).extract().as(Pet.class);

        System.out.println(petResponse.getId());

        //verify @Test is pass or faile
        Assert.assertEquals(petResponse.getId(),556);
    }
}