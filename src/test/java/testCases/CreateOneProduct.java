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

public class CreateOneProduct {
	
	/*
	 03.CreateOneProduct
http method=POST 
EndPointUrl=https://techfios.com/api-prod/api/product/create.php
Authorization:(basic auth)
username=demo@techfios.com
password=abc123
Header/s:
Content-Type=application/json; charset=UTF-8
http status code=201
responseTime= <=1500ms
Payload/Body: 
  
{
    "name": "Amazing Headset 1.0 By MD",
    "description": "The best Headset for amazing programmers.",
    "price": "199",
    "category_id": "2",
    "category_name": "Electronics"
}
	 */
	
	String baseURI;
	SoftAssert softAssert;
	String createPayloadPath;
	HashMap<String, String> createPayload;
	String firstProductId;
	
	public CreateOneProduct() {
		
		baseURI= "https://techfios.com/api-prod/api/product";
		softAssert = new SoftAssert();
		createPayloadPath = "src/main/java/data/CreatePayload.json";
		createPayload = new HashMap<String, String>();
	}
	
	public Map<String, String> createPayloadMap(){
		
		createPayload.put("name", "Most Amazing Headset 4.0 By SRao");
		createPayload.put("description", "The very best Headset for amazing programmers.");
		createPayload.put("price", "499");
		createPayload.put("category_id", "2");
		createPayload.put("category_name", "Electronics");
		
		return createPayload;
			
	}
	
	@Test (priority = 1)
	public void createOneProduct() {
		
		System.out.println("Create Payload Map:" + createPayloadMap());
		
		Response response =
		given()
				.baseUri(baseURI)
				.header("Content-Type","application/json; charset=UTF-8" )
				.auth().preemptive().basic("demo@techfios.com", "abc123")
//				.body(new File(createPayloadPath)).
				.body(createPayloadMap()).
		when()
				.post("/create.php").
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
		  softAssert.assertEquals(responseStatusCode, 201, "Status Codes are not matching!");
		  System.out.println("Response Status Code: " + responseStatusCode);
		  			  
		  String responseHeaderContentType = response.getHeader("Content-Type");
		  softAssert.assertEquals(responseHeaderContentType, "application/json; charset=UTF-8", "Response Header Content Types are not matching!");
		  System.out.println("Response Header ContentType: " + responseHeaderContentType);
		  		  
		  String responseBody = response.getBody().asString();
		  System.out.println("Response Body: " + responseBody);
		  
		  JsonPath jp = new JsonPath(responseBody);
		  
		  String productMessage =jp.getString("message");
		  softAssert.assertEquals(productMessage, "Product was created.", "Product Message is not matching!");
		  System.out.println("Product Message:" + productMessage); 
		  		  
		  softAssert.assertAll();
				
	}

	@Test (priority = 2)
	public void readAllProducts() {
		
		Response response =
		given()
				.baseUri(baseURI)
				.header("Content-Type","application/json; charset=UTF-8" )
				.auth().preemptive().basic("demo@techfios.com", "abc123").
		when()
				.get("/read.php").
		then()
				.log().all()
				.extract().response();
						  
		  String responseBody = response.getBody().asString();
		  System.out.println("Response Body: " + responseBody);
		  
		  JsonPath jp = new JsonPath(responseBody);
		  firstProductId = jp.getString("records[0].id");
		  System.out.println("First Product ID " + firstProductId); 		
				
	}
	
	@Test (priority=3)
	public void readOneProduct() {
		
		Response response =
		given()
				.baseUri(baseURI)
				.header("Content-Type","application/json" )
				.auth().preemptive().basic("demo@techfios.com", "abc123")
				.queryParam("id", firstProductId).
		when()
				.get("/read_one.php").
		then()
				.log().all()
				.extract().response();		    
		  
		  String actualResponseBody = response.getBody().asString();
		  System.out.println("Response Body: " + actualResponseBody);
		  
		  JsonPath jp = new JsonPath(actualResponseBody);
		  
		  String actualProductName = jp.getString("name");
		  String expectedProductName = createPayloadMap().get("name");
		  softAssert.assertEquals(actualProductName, expectedProductName, "Product names are not matching!");
		  System.out.println("Actual Product Name: " + actualProductName);
		  		  
		  String actualProductDescription = jp.getString("description");
		  String expectedProductDescription = createPayloadMap().get("description");
		  softAssert.assertEquals(actualProductDescription, expectedProductDescription, "Product descriptions are not matching!");
		  System.out.println("Actual Product Description: " + actualProductDescription);
		  		  
		  String actualProductPrice = jp.getString("price");
		  String expectedProductPrice = createPayloadMap().get("price");
		  softAssert.assertEquals(actualProductPrice, expectedProductPrice, "Product prices are not matching!");
		  System.out.println("Actual Product Price: " + actualProductPrice);
		  
		  softAssert.assertAll();				
	}
	
}
