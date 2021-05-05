package com.innovate.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.innovate.entity.AirplaneEntity;

@RestController
@RequestMapping("/airaplane")
public class AirplaneSeatingController {
	
	
	private static final int rowIndex = 1;
	private static final int columnIndex = 0;		
	
	@GetMapping("/calculateSeatingPosition")  
	public Map<String,Object> calculateSeatingPosition(@RequestBody int[][] inputArray,@RequestBody int numberOfPassengers) {

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
	
	private AirplaneEntity determineSeatCategory(int[][] inputArray,
			List<Integer> aisleColumns,List<Integer> windowColumns,List<Integer> centreColumns) {
		
		int maxRow = Integer.MIN_VALUE;
		int columnSum = 0;
		
		for(int segmentIterator = 0; segmentIterator < inputArray.length;segmentIterator++) { 
			if(inputArray[segmentIterator][rowIndex]> maxRow) maxRow = inputArray[segmentIterator][rowIndex];
			int columnCount = inputArray[segmentIterator][columnIndex];
			if(segmentIterator==0)	windowColumns.add(columnSum);
			if(segmentIterator>0)	aisleColumns.add(columnSum);
			if(columnCount>2) centreColumns.addAll(IntStream.rangeClosed(columnSum+1, columnSum+columnCount-2).boxed().collect(Collectors.toList()));
			columnSum += columnCount;
			if(segmentIterator<inputArray.length-1 && columnCount>1) aisleColumns.add(columnSum-1);
			if(segmentIterator==inputArray.length-1 && inputArray.length>1) windowColumns.add(columnSum-1);
		}
		return new AirplaneEntity(maxRow, columnSum);
	}
	
	private AirplaneEntity determineSeatCount(int[][] inputArray,int[][] resultArray
			,List<Integer> aisleColumns,List<Integer> windowColumns,List<Integer> centreColumns) {
		
		int validColumnIterator=0;
		int validAisleCount=0;
		int validWindowCount=0;
		int validCentreCount=0;
		
		for(int segmentIterator = 0; segmentIterator < inputArray.length;segmentIterator++) { 
			for(int rowiterator=0;rowiterator<inputArray[segmentIterator][rowIndex];rowiterator++) {
				for(int columnIterator=validColumnIterator;columnIterator<validColumnIterator+inputArray[segmentIterator][columnIndex];columnIterator++) {
					resultArray[rowiterator][columnIterator] = Integer.MIN_VALUE;
					if(aisleColumns.contains(columnIterator)) validAisleCount++;
					if(windowColumns.contains(columnIterator)) validWindowCount++;
					if(centreColumns.contains(columnIterator)) validCentreCount++;
				}
			}
			validColumnIterator+=inputArray[segmentIterator][columnIndex];
		}
		
		return new AirplaneEntity(validColumnIterator, validAisleCount, validWindowCount, validCentreCount);
	}
	
	private Map<String,Object> fillPassengers(int[][] resultArray,List<Integer> aisleColumns,List<Integer> windowColumns
			,List<Integer> centreColumns, AirplaneEntity airplaneEntity, int numberOfPassengers) {
		
		int validColumnIterator=airplaneEntity.getValidColumnIterator();
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
			
			if(resultArray[rowiterator][columnIterator] == 0) {
		    }else if(aisleFilledCount<(validAisleCount)) {
				if(aisleColumns.contains(columnIterator)) {
					resultArray[rowiterator][columnIterator] = ++passengersFilled;
					aisleFilledCount++;
					if(aisleFilledCount==(validAisleCount)) {
						rowiterator = 0;columnIterator = -1;
					}
				} 
			}else if(windowFilledCount<(validWindowCount)) {
						if(windowColumns.contains(columnIterator)) {
							resultArray[rowiterator][columnIterator] = ++passengersFilled;
							windowFilledCount++;
							if(windowFilledCount==(validWindowCount)) {
								rowiterator = 0;columnIterator = -1;
							}
						} 
			}else if(centreFilledCount<(validCentreCount)) {
				if(centreColumns.contains(columnIterator)) {
					resultArray[rowiterator][columnIterator] = ++passengersFilled;
					centreFilledCount++;
					if(centreFilledCount==(validCentreCount)) {
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
		
		resultMap.put("resultArray", resultArray);
		resultMap.put("status", Boolean.TRUE);
		return resultMap;
	}

}
