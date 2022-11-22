package testCases;

import static io.restassured.RestAssured.given;

import java.util.concurrent.TimeUnit;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class ReadOneProduct {
	
	/*
	 02. ReadOneProduct
http method=GET 
EndPointUrl=https://techfios.com/api-prod/api/product/read_one.php
Authorization:(basic auth)
username=demo@techfios.com
password=abc123
Query Parameters:
id=6034
Header/s:
Content-Type=application/json
http status code=200
responseTime= <=1500ms
	 */
	
	String baseURI;
	SoftAssert softAssert;
	
	public ReadOneProduct() {
		
		baseURI= "https://techfios.com/api-prod/api/product";
		softAssert = new SoftAssert();
	}
	
	@Test
	public void readOneProduct() {
		
		Response response =
		given()
				.baseUri(baseURI)
				.header("Content-Type","application/json" )
				.auth().preemptive().basic("demo@techfios.com", "abc123")
				.queryParam("id", "6301").
		when()
				.get("/read_one.php").
		then()
				.log().all()
//				.statusCode(200)
//				.header("Content-Type","application/json; charset=UTF-8");
				.extract().response();
		
		long responseTime = response.getTimeIn(TimeUnit.MILLISECONDS);
		  System.out.println("Response Time " + responseTime);
		  
		   if (responseTime<=1500) {
		    System.out.println("Response time is within range");
		   } else 
		    System.out.println("Response time is out of range");
		  
		  int responseStatusCode = response.getStatusCode();
		  softAssert.assertEquals(responseStatusCode, 200, "Status Codes are not matching!");
		  System.out.println("Response Status Code: " + responseStatusCode);
		  
		  
		  
		  String responseHeaderContentType = response.getHeader("Content-Type");
		  softAssert.assertEquals(responseHeaderContentType, "application/json", "Response Header Content Types are not matching!");
		  System.out.println("Response Header ContentType: " + responseHeaderContentType);
		  
		  
		  
		  String responseBody = response.getBody().asString();
		  System.out.println("Response Body: " + responseBody);
		  
		  //creating and object to get the JSON PATH 
		  JsonPath jp = new JsonPath(responseBody);
		  
		  String productName = jp.getString("name");
		  softAssert.assertEquals(productName, "Amazing Headset 1.0 By MD", "Product names are not matching!");
		  System.out.println("Product Name: " + productName);
		  
		  
		  String productDescription = jp.getString("description");
		  softAssert.assertEquals(productDescription, "The best Headset for amazing programmers.", "Product descriptions are not matching!");
		  System.out.println("Product Description: " + productDescription);
		  
		  
		  String productPrice = jp.getString("price");
		  softAssert.assertEquals(productPrice, "199", "Product prices are not matching!");
		  System.out.println("Product Price: " + productPrice);
		  
		  softAssert.assertAll();
				
	}

}
