package com.expleo.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import org.testng.annotations.Test;

public class Demo1GetPetDetailsTest {
    public String baseUrl="https://petstore.swagger.io/v2/";

    @Test
    public void findValidPetIdTest() throws JsonProcessingException {
        int petId=5;
        String resource="pet/"+petId;

        String responseStr=RestAssured
                .given()
                .when().get(baseUrl+resource)
                .then().statusCode(200).extract().asString();

        System.out.println(responseStr);
        //expect true
       // Assert.assertTrue(responseStr.contains(":5"),"Not matching id");

        ObjectMapper mapper=new ObjectMapper();
        JsonNode jsonNodeobj= mapper.readTree(responseStr);

        System.out.println(jsonNodeobj.get("id"));
        System.out.println(jsonNodeobj.get("name").asText());
        System.out.println(jsonNodeobj.get("category").get("id"));
        System.out.println(jsonNodeobj.get("tags").get(0).get("id").asInt());
    }

    //findPetByStatus --> sold
    //verify all pet received are with status sold
}
