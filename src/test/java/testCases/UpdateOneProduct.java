package testCases;

import static io.restassured.RestAssured.given;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class UpdateOneProduct {
	
	/*
	 04. UpdateOneProduct
http method=PUT 
EndPointUrl=https://techfios.com/api-prod/api/product/update.php
Authorization:(basic auth)
username=demo@techfios.com
password=abc123
Header/s:
Content-Type=application/json; charset=UTF-8
http status code=200
responseTime= <=1500ms
Payload/Body: 
  
{
    "id": "6055",
    "name": "Amazing Headset 2.0 By MD",
    "description": "The best Updated Headset for amazing programmers.",
    "price": "299",
    "category_id": "2",
    "category_name": "Electronics"
} 
	 */
	
	String baseURI;
	SoftAssert softAssert;
	HashMap<String, String> updatePayload;
	
	public UpdateOneProduct() {
		
		baseURI= "https://techfios.com/api-prod/api/product";
		softAssert = new SoftAssert();
		updatePayload = new HashMap<String, String>();
	}
		
	public Map<String, String> updatePayloadMap(){
		
		updatePayload.put("id", "6314");
		updatePayload.put("name", "Amazing Headset 7.0 By Shalini");
		updatePayload.put("description", "Best Headset for amazing programmers.");
		updatePayload.put("price", "799");
		updatePayload.put("category_id", "2");
		updatePayload.put("category_name", "Electronics");
		
		return updatePayload;
				}
	
	@Test (priority = 1)
	public void updateOneProduct() {
						
		Response response =
		given()
				.baseUri(baseURI)
				.header("Content-Type","application/json; charset=UTF-8" )
				.auth().preemptive().basic("demo@techfios.com", "abc123")		
				.body(updatePayloadMap()).
		when()
				.put("/update.php").
		then()
				.log().all()
				.extract().response();
		
		long responseTime = response.getTimeIn(TimeUnit.MILLISECONDS);
		  System.out.println("Response Time " + responseTime);
		  
		   if (responseTime<=2500) {
		    System.out.println("Response time is within range");
		   } else 
		    System.out.println("Response time is out of range");
		  
		  int responseStatusCode = response.getStatusCode();
		  softAssert.assertEquals(responseStatusCode, 200, "Status Codes are not matching!");
		  System.out.println("Response Status Code: " + responseStatusCode);
		  			  
		  String responseHeaderContentType = response.getHeader("Content-Type");
		  softAssert.assertEquals(responseHeaderContentType, "application/json; charset=UTF-8", "Response Header Content Types are not matching!");
		  System.out.println("Response Header ContentType: " + responseHeaderContentType);
		  		  
		  String responseBody = response.getBody().asString();
		  System.out.println("Response Body: " + responseBody);
		  
		  JsonPath jp = new JsonPath(responseBody);
		  
		  String productMessage =jp.getString("message");
		  softAssert.assertEquals(productMessage, "Product was updated.", "Product Message is not matching!");
		  System.out.println("Product Message:" + productMessage); 
		  		  
		  softAssert.assertAll();				
	}
	
	@Test (priority=2)
	public void readOneUpdatedProduct () {
		
		Response response =
		given()
				.baseUri(baseURI)
				.header("Content-Type","application/json" )
				.auth().preemptive().basic("demo@techfios.com", "abc123")
				.queryParam("id", updatePayloadMap().get("id")).
		when()
				.get("/read_one.php").
		then()
				.log().all()
				.extract().response();		    
		  
		  String actualResponseBody = response.getBody().asString();
		  System.out.println("Response Body: " + actualResponseBody);
		  
		  JsonPath jp = new JsonPath(actualResponseBody);
		  
		  String actualProductName = jp.getString("name");
		  String expectedProductName = updatePayloadMap().get("name");
		  softAssert.assertEquals(actualProductName, expectedProductName, "Product names are not matching!");
		  System.out.println("Actual Product Name: " + actualProductName);
		  		  
		  String actualProductDescription = jp.getString("description");
		  String expectedProductDescription = updatePayloadMap().get("description");
		  softAssert.assertEquals(actualProductDescription, expectedProductDescription, "Product descriptions are not matching!");
		  System.out.println("Actual Product Description: " + actualProductDescription);
		  		  
		  String actualProductPrice = jp.getString("price");
		  String expectedProductPrice = updatePayloadMap().get("price");
		  softAssert.assertEquals(actualProductPrice, expectedProductPrice, "Product prices are not matching!");
		  System.out.println("Actual Product Price: " + actualProductPrice);
		  
		  softAssert.assertAll();				
	}
	
}
