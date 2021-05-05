package com.innovate.entity;

/**
 * AirplaneEntity holds all the essential information needed for airplane seating operation
 * 
 * @author Vasanthan C
 */
public class AirplaneEntity {
	
	/** maxRow represents number of rows in airplane */
	private int maxRow;
	/** columnSum represents maximum number of columns in airplane */
	private int columnSum;
	/** validColumnIterator represents valid number of seats in airplane */
	private int validColumnIterator;
	/** validAisleCount represents number of Aisle seats in airplane */
	private int validAisleCount;
	/** validWindowCount represents number of Window seats in airplane */
	private int validWindowCount;
	/** validCentreCount represents number of center seats in airplane */
	private int validCentreCount;
	
	/**
	 * getMaxRow gets maxRow
	 * @return maxRow
	 */
	public int getMaxRow() {
		return maxRow;
	}
	
	/**
	 * getColumnSum gets columnSum
	 * @return columnSum
	 */
	public int getColumnSum() {
		return columnSum;
	}
	
	/**
	 * getValidColumnIterator gets validColumnIterator
	 * @return validColumnIterator
	 */
	public int getValidColumnIterator() {
		return validColumnIterator;
	}
	
	/**
	 * getValidAisleCount gets validAisleCount
	 * @return validAisleCount
	 */
	public int getValidAisleCount() {
		return validAisleCount;
	}
	
	/**
	 * getValidWindowCount gets validWindowCount
	 * @return
	 */
	public int getValidWindowCount() {
		return validWindowCount;
	}
	
	/**
	 * getValidCentreCount gets validCentreCount
	 * @return
	 */
	public int getValidCentreCount() {
		return validCentreCount;
	}
	
	/**
	 * AirplaneEntity constructs AirplaneEntity with following data members 
	 * @param maxRow
	 * @param columnSum
	 */
	public AirplaneEntity(int maxRow, int columnSum) {
		super();
		this.maxRow = maxRow;
		this.columnSum = columnSum;
	}
	
	/**
	 * AirplaneEntity constructs AirplaneEntity with following data members 
	 * @param validColumnIterator
	 * @param validAisleCount
	 * @param validWindowCount
	 * @param validCentreCount
	 */
	public AirplaneEntity(int validColumnIterator, int validAisleCount, int validWindowCount, int validCentreCount) {
		super();
		this.validColumnIterator = validColumnIterator;
		this.validAisleCount = validAisleCount;
		this.validWindowCount = validWindowCount;
		this.validCentreCount = validCentreCount;
	}
	
	/**
	 * copyRowColumn copies maxRow and columnSum from the entity passed as parameter
	 * @param rowColumnEntity
	 * @return
	 */
	public AirplaneEntity copyRowColumn(AirplaneEntity rowColumnEntity) {
		this.maxRow = rowColumnEntity.maxRow;
		this.columnSum = rowColumnEntity.columnSum;
		return this;
	}
	
	/**
	 * getAllSeatCount returns the total number of seats in airplane
	 * @return
	 */
	public int getAllSeatCount() {
		return this.validAisleCount+this.validWindowCount+this.validCentreCount;
	}

}
