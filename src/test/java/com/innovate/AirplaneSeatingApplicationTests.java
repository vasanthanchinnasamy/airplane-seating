package com.innovate;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import com.innovate.controller.AirplaneSeatingController;

@SpringBootTest
class AirplaneSeatingApplicationTests {
	
	
	@Autowired
	private AirplaneSeatingController   airplaneSeatingController;

 	
	
	@Test
	void testOverflowCondition() {
		
		int[][] inputArray = {
				{3,2},
				{4,3},
				{2,3},
				{3,4}};
		Map<String,Object> resultMap = airplaneSeatingController.calculateSeatingPosition(inputArray, 40);
		Assert.isTrue(!(Boolean) resultMap.get("status"), "Passenger count is greater than available seats.");
	}
	
	@Test
	void testCalculateSeatingPosition() {
		
		int[][] inputArray = {
				{3,2},
				{4,3},
				{2,3},
				{3,4}};
		Map<String,Object> resultMap = airplaneSeatingController.calculateSeatingPosition(inputArray, 30);
		System.out.println(resultMap);
		
	}

}
