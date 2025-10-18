package com.expleo.test;

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

        RestAssured.baseURI="https://api.github.com";
        RestAssured
                .given()
                .headers("Authorization","Bearer "+token)
                .when().get("/user/repos")
                .then().statusCode(200).log().all();
    }
}
