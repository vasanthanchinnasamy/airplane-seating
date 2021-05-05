package com.innovate.controller;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.json.JSONArray;
import org.json.JSONObject;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.innovate.entity.AirplaneEntity;

/**
 * AirplaneSeatingController is used to handle airplane seating functions
 *  
 * @author Vasanthan C
 */
@RestController
@RequestMapping("/airplane")
public class AirplaneSeatingController {
	
	/** The Constant ROW_INDEX represents index where number of rows is stored in blueprint*/
	private static final int ROW_INDEX = 1;
	/** The Constant COLUMN_INDEX represents index where number of columns is stored  in blueprint*/
	private static final int COLUMN_INDEX = 0;	
	/** The Constant INVALID_SEAT represents invalid seat in result array */
	private static final int INVALID_SEAT = 0;
	
	/**
	 * calculateSeatingPosition method calculates seating position for numberOfPassengers given in input in given blueprint 
	 * @param inputData contains inputArray and numberOfPassengers
	 * inputArray defines the airplane seating blueprint
	 * numberOfPassengers is the number of passengers for whom seating need to be calculated
	 * @return resultMap
	 */
	@GetMapping("/calculateSeatingPosition")  
	public Map<String,Object> calculateSeatingPosition(@RequestBody Map<String,Object> inputData) {
		
		JSONObject jsonObject = new JSONObject(inputData);
		int numberOfPassengers = jsonObject.getInt("numberOfPassengers");
		int [][] inputArray = parseArray(jsonObject.getJSONArray("inputArray"));

		List<Integer> aisleColumns = new ArrayList<>();
		List<Integer> windowColumns = new ArrayList<>();
		List<Integer> centreColumns = new ArrayList<>();
		Map<String,Object> resultMap = new HashMap<>();
		
		
		AirplaneEntity airplaneEntity = determineSeatCategory(inputArray, aisleColumns, windowColumns, centreColumns);
		int maxRow=airplaneEntity.getMaxRow(); int columnSum=airplaneEntity.getColumnSum();		
		int[][] resultArray = new int[maxRow][columnSum];		
		
		AirplaneEntity seatCountEntity = determineSeatCount(inputArray, resultArray, aisleColumns, windowColumns, centreColumns);
		seatCountEntity = seatCountEntity.copyRowColumn(airplaneEntity);
		
		if(numberOfPassengers>seatCountEntity.getAllSeatCount()) {
			resultMap.put("message", "Passenger count is greater than available seats.");
			resultMap.put("status", Boolean.FALSE);
			return resultMap;
		}
		
		resultMap= fillPassengers(resultArray, aisleColumns, windowColumns, centreColumns,seatCountEntity,numberOfPassengers);
		
		return resultMap;
	}
	


	/**
	 * determineSeatCategory method categories the seats into Aisle,Window and Center
	 * @param inputArray which defines the airplane seating blueprint
	 * @param aisleColumns column numbers which come into Aisle category
	 * @param windowColumns column numbers which come into Window category
	 * @param centreColumns column numbers which come into Center category
	 * @return AirplaneEntity
	 */
	private AirplaneEntity determineSeatCategory(int[][] inputArray,
			List<Integer> aisleColumns,List<Integer> windowColumns,List<Integer> centreColumns) {
		
		int maxRow = Integer.MIN_VALUE;
		int columnSum = 0;
		
		for(int segmentIterator = 0; segmentIterator < inputArray.length;segmentIterator++) { 
			if(inputArray[segmentIterator][ROW_INDEX]> maxRow) maxRow = inputArray[segmentIterator][ROW_INDEX];
			int columnCount = inputArray[segmentIterator][COLUMN_INDEX];
			if(segmentIterator==0)	windowColumns.add(columnSum);
			if(segmentIterator>0)	aisleColumns.add(columnSum);
			if(columnCount>2) centreColumns.addAll(IntStream.rangeClosed(columnSum+1, columnSum+columnCount-2).boxed().collect(Collectors.toList()));
			columnSum += columnCount;
			if(segmentIterator<inputArray.length-1 && columnCount>1) aisleColumns.add(columnSum-1);
			if(segmentIterator==inputArray.length-1 && inputArray.length>1) windowColumns.add(columnSum-1);
		}
		return new AirplaneEntity(maxRow, columnSum);
	}
	
	/**
	 * determineSeatCount method calculates seat count of all three categories (Aisle,Window and Center)
	 * @param inputArray which defines the airplane seating blueprint
	 * @param resultArray which defines seating position of each passenger
	 * @param aisleColumns column numbers which come into Aisle category
	 * @param windowColumns column numbers which come into Window category
	 * @param centreColumns column numbers which come into Center category
	 * @return AirplaneEntity
	 */
	private AirplaneEntity determineSeatCount(int[][] inputArray,int[][] resultArray
			,List<Integer> aisleColumns,List<Integer> windowColumns,List<Integer> centreColumns) {
		
		int validColumnIterator=0;
		int validAisleCount=0;
		int validWindowCount=0;
		int validCentreCount=0;
		
		for(int segmentIterator = 0; segmentIterator < inputArray.length;segmentIterator++) { 
			for(int rowiterator=0;rowiterator<inputArray[segmentIterator][ROW_INDEX];rowiterator++) {
				for(int columnIterator=validColumnIterator;columnIterator<validColumnIterator+inputArray[segmentIterator][COLUMN_INDEX];columnIterator++) {
					resultArray[rowiterator][columnIterator] = Integer.MIN_VALUE;
					if(aisleColumns.contains(columnIterator)) validAisleCount++;
					if(windowColumns.contains(columnIterator)) validWindowCount++;
					if(centreColumns.contains(columnIterator)) validCentreCount++;
				}
			}
			validColumnIterator+=inputArray[segmentIterator][COLUMN_INDEX];
		}
		
		return new AirplaneEntity(validColumnIterator, validAisleCount, validWindowCount, validCentreCount);
	}
	
	/**
	 * fillPassengers method fills the passengers based on the following conditions
	 * Top to Bottom,left to right, isle
	 * @param resultArray
	 * @param aisleColumns
	 * @param windowColumns
	 * @param centreColumns
	 * @param airplaneEntity
	 * @param numberOfPassengers
	 * @return
	 */
	private Map<String,Object> fillPassengers(int[][] resultArray,List<Integer> aisleColumns,List<Integer> windowColumns
			,List<Integer> centreColumns, AirplaneEntity airplaneEntity, int numberOfPassengers) {
		
		int validAisleCount=airplaneEntity.getValidAisleCount();
		int validWindowCount=airplaneEntity.getValidWindowCount();
		int validCentreCount=airplaneEntity.getValidCentreCount();
		int maxRow=airplaneEntity.getMaxRow(); int columnSum=airplaneEntity.getColumnSum();	
		int passengersFilled = 0;
		int aisleFilledCount = 0;
		int windowFilledCount = 0;
		int centreFilledCount = 0;
		int rowiterator = 0;
		int columnIterator = 0;
		Map<String,Object> resultMap = new HashMap<>();
		
		while(passengersFilled < numberOfPassengers) {
			
			if(resultArray[rowiterator][columnIterator] == INVALID_SEAT) {
		    }else if(aisleFilledCount<validAisleCount) {  // When Aisle seats are available to fill
				if(aisleColumns.contains(columnIterator)) { // When current column is valid Aisle Column
					resultArray[rowiterator][columnIterator] = ++passengersFilled;
					aisleFilledCount++;
					if(aisleFilledCount==validAisleCount) {
						rowiterator = 0;columnIterator = -1;
					}
				} 
			}else if(windowFilledCount<validWindowCount) { // When Window seats are available to fill
						if(windowColumns.contains(columnIterator)) { // When current column is valid Window Column
							resultArray[rowiterator][columnIterator] = ++passengersFilled;
							windowFilledCount++;
							if(windowFilledCount==validWindowCount) {
								rowiterator = 0;columnIterator = -1;
							}
						} 
			}else if(centreFilledCount<validCentreCount) { // When Center seats are available to fill
				if(centreColumns.contains(columnIterator)) { // When current column is valid Center Column
					resultArray[rowiterator][columnIterator] = ++passengersFilled;
					centreFilledCount++;
					if(centreFilledCount==validCentreCount) {
						rowiterator = 0;columnIterator = -1;
					}
				} 
			}
			
			columnIterator++;
			int columnRemaining = columnIterator / columnSum;
			columnIterator %= columnSum;
			rowiterator+= columnRemaining;
			rowiterator%= maxRow; 
		}
		
		resultMap.put("status", Boolean.TRUE);
		resultMap.put("prettyResult", prettyPrintResult(resultArray, aisleColumns, windowColumns, centreColumns));
		return resultMap;
	}

	
	/**
	 * prettyPrintResult converts the result array into pretty json string  
	 * @param resultArray
	 * @return
	 */
	private List<String> prettyPrintResult(int[][] resultArray,List<Integer> aisleColumns,List<Integer> windowColumns
			,List<Integer> centreColumns) {
		String resultString = "";
		String seatCategory = "";
		List<String> resultStringList = new LinkedList<>();
		
		for(int rowiterator=0;rowiterator<resultArray.length;rowiterator++) {
			for(int columnIterator=0;columnIterator<resultArray[rowiterator].length;columnIterator++) {
				
				if(aisleColumns.contains(columnIterator)) seatCategory="A";
				else if(windowColumns.contains(columnIterator)) seatCategory="W";
				else if(centreColumns.contains(columnIterator)) seatCategory="C";
				
				if(resultArray[rowiterator][columnIterator] ==0)
					resultString+="       ";
				else if(resultArray[rowiterator][columnIterator]==Integer.MIN_VALUE)
					resultString+=seatCategory+new DecimalFormat("00").format(0)+"    ";
				else
					resultString+=seatCategory+new DecimalFormat("00").format(resultArray[rowiterator][columnIterator])+"    ";
			} 
			resultStringList.add(resultString);
			resultString="";
		}
		return resultStringList;
	}
	
	/**
	 * parseArray parses json array into java 2d array 
	 * @param jsonArray
	 * @return
	 */
	private int[][] parseArray(JSONArray jsonArray) {
		int [][] inputArray = new int[jsonArray.length()][2];
		for(int rowIterator=0;rowIterator<jsonArray.length();rowIterator++) {
			JSONArray innerJsonArray = jsonArray.getJSONArray(rowIterator);
			int innerArray[] = new int[innerJsonArray.length()];
			for(int columnIterator=0;columnIterator<innerJsonArray.length();columnIterator++) {
				innerArray[columnIterator] = innerJsonArray.getInt(columnIterator);
			}
			inputArray[rowIterator] = innerArray;
		}
		return inputArray;
	}

}
