package Temp_pack;

import java.util.Date;
import java.util.Random;

import org.testng.annotations.Test;

import io.opentelemetry.sdk.metrics.data.Data;

public class timestamp {
	
	@Test
	public String reusableemail()
	{
	//	Date date = new Date();
	//	System.out.println(date);
		
	return new  Date().toString().replace(" ", "").replace(":", "") + "@gmail.com";
	//	System.out.println(emailwithoutspace);
		
		//String email = emailwithoutspace + "@gmail.com";
	//	System.out.println(email);
		//return email;
	}

	
 @Test
 public String resuablemobile()
 {
	 return "9" + (100000000L + (long) (new Random().nextDouble() * 900000000L));

 }
 
 
 @Test
 public void replcespace()
 {
	 String tc = "Verify different ways of navigating to Register Account page".replace(" ", "_");
	 
	 System.out.println(tc);
	 
 }
}
