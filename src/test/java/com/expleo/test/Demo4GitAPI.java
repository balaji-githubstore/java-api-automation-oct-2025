package com.expleo.test;

import com.atlassian.oai.validator.restassured.OpenApiValidationFilter;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.testng.annotations.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Demo4GitAPI {

    @Test
    public void demo4GitListRepoForUserBearer() throws FileNotFoundException {

        FileInputStream file=new FileInputStream("src/test/resources/secret.json");
        JsonPath jsonPath=new JsonPath(file);
        String token=jsonPath.get("token");

        OpenApiValidationFilter validationFilter
                =new OpenApiValidationFilter("src/test/resources/demo.yaml");

        RestAssured.baseURI="https://api.github.com";
        RestAssured
                .given()
                .filter(validationFilter)
                .headers("Authorization","Bearer "+token)
                .when().get("/user/repos")
                .then().statusCode(200).log().all();
    }


}
