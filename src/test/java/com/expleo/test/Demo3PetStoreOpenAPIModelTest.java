package com.expleo.test;


import com.expleo.openapimodel.Pet;
import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

public class Demo3PetStoreOpenAPIModelTest {

    @Test
    public void demo3GetPetByIdPojoclass() {
        RestAssured.baseURI="https://petstore.swagger.io/v2";

        Pet petObj = RestAssured
                .given()
                .pathParam("petId", 556)
                .when().get("/pet/{petId}")
                .then().statusCode(200).extract().as(Pet.class);

        System.out.println(petObj.getId());
        System.out.println(petObj.getCategory().getId());
        System.out.println(petObj.getTags().get(0).getId());
    }

    @Test
    public void demo2FindByStatusPetTest() {
        RestAssured.baseURI="https://petstore.swagger.io/v2";

        Pet[] petObj = RestAssured
                .given()
                .when().get("/pet/findByStatus?status=sold")
                .then().statusCode(200).extract().as(Pet[].class);

        System.out.println(petObj[0].getId());

    }

    @Test
    public void demo2FindByStatusPetListTest() {
        RestAssured.baseURI="https://petstore.swagger.io/v2";

        List<Pet> listOfPets = RestAssured
                .given()
                .when().get("/pet/findByStatus?status=sold")
                .then().statusCode(200).extract().as(new TypeRef<List<Pet>>() {});

        System.out.println(listOfPets.get(0).getId());
        System.out.println(listOfPets.get(1).getId());

        System.out.println(listOfPets.get(0).getStatus());
        System.out.println(listOfPets.get(1).getStatus());

        for(int i=0;i<listOfPets.size();i++)
        {
            System.out.println(listOfPets.get(i).getStatus());
        }
    }

    @Test
    public void demo3Git() {
        RestAssured.baseURI="https://api.github.com";

         RestAssured
                .given()
                //.auth().preemptive().basic("","")
               // .headers("Authorization","brearer ")
                .auth().basic("dbala-cloud","")
                .when().get("/user/repos")
                .then().statusCode(200).log().all();


    }

}
