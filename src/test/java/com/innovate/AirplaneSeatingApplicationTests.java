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
	void testSeatingPosition() {
		
		int[][] inputArray = {
				{3,2},
				{4,3},
				{2,3},
				{3,4}};
		int[][] expectedResultArray = {
				{19, 25, 1, 2, 26, 27, 3, 4, 5, 6, 28, 20},
				{21, 29, 7, 8, 30, -2147483648, 9, 10, 11, 12, -2147483648, 22},
				{0, 0, 0, 13, -2147483648, -2147483648, 14, 15, 16, 17, -2147483648, 23},
				{0, 0, 0, 0, 0, 0, 0, 0, 0, 18, -2147483648, 24}};
		Map<String,Object> resultMap = airplaneSeatingController.calculateSeatingPosition(inputArray, 30);
		int[][] resultArray = (int[][]) resultMap.get("resultArray");
		Assert.isTrue(compareArray(resultArray,expectedResultArray), "Passengers are not filled correctly.");
		System.out.println(resultMap.get("prettyResult"));
	}
	
	@Test
	void testWhenPassengerCountisZero() {
		
		int[][] inputArray = {
				{3,2},
				{4,3},
				{2,3},
				{3,4}};
		int[][] expectedResultArray = {
				{-2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648},
				{-2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648},
				{0, 0, 0, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648},
				{0, 0, 0, 0, 0, 0, 0, 0, 0, -2147483648, -2147483648, -2147483648}};
		Map<String,Object> resultMap = airplaneSeatingController.calculateSeatingPosition(inputArray, 0);
		int[][] resultArray = (int[][]) resultMap.get("resultArray");
		Assert.isTrue(compareArray(resultArray,expectedResultArray), "Passengers are not filled correctly.");
		System.out.println(resultMap.get("prettyResult"));
	}

	private boolean compareArray(int[][] resultArray, int[][] expectedResultArray) {
		Boolean equals=true;
		if(resultArray.length != expectedResultArray.length) return false;
		for(int rowiterator=0;rowiterator<resultArray.length;rowiterator++) {
			if(resultArray[rowiterator].length != expectedResultArray[rowiterator].length) return false;
			for(int columnIterator=0;columnIterator<resultArray[rowiterator].length;columnIterator++) {
				if(resultArray[rowiterator][columnIterator] != expectedResultArray[rowiterator][columnIterator]) return false;
			} 
		}
		return equals;
	}

}
