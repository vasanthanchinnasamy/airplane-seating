package com.innovate.entity;

public class AirplaneEntity {
	
	private int maxRow;
	private int columnSum;
	private int validColumnIterator;
	private int validAisleCount;
	private int validWindowCount;
	private int validCentreCount;
	
	public int getMaxRow() {
		return maxRow;
	}
	public void setMaxRow(int maxRow) {
		this.maxRow = maxRow;
	}
	public int getColumnSum() {
		return columnSum;
	}
	public void setColumnSum(int columnSum) {
		this.columnSum = columnSum;
	}
	
	public int getValidColumnIterator() {
		return validColumnIterator;
	}
	public void setValidColumnIterator(int validColumnIterator) {
		this.validColumnIterator = validColumnIterator;
	}
	public int getValidAisleCount() {
		return validAisleCount;
	}
	public void setValidAisleCount(int validAisleCount) {
		this.validAisleCount = validAisleCount;
	}
	public int getValidWindowCount() {
		return validWindowCount;
	}
	public void setValidWindowCount(int validWindowCount) {
		this.validWindowCount = validWindowCount;
	}
	public int getValidCentreCount() {
		return validCentreCount;
	}
	public void setValidCentreCount(int validCentreCount) {
		this.validCentreCount = validCentreCount;
	}
	
	public AirplaneEntity(int maxRow, int columnSum) {
		super();
		this.maxRow = maxRow;
		this.columnSum = columnSum;
	}
	
	public AirplaneEntity(int validColumnIterator, int validAisleCount, int validWindowCount, int validCentreCount) {
		super();
		this.validColumnIterator = validColumnIterator;
		this.validAisleCount = validAisleCount;
		this.validWindowCount = validWindowCount;
		this.validCentreCount = validCentreCount;
	}
	
	public AirplaneEntity copyRowColumn(AirplaneEntity rowColumnEntity) {
		this.maxRow = rowColumnEntity.maxRow;
		this.columnSum = rowColumnEntity.columnSum;
		return this;
	}
	
	
	
	

}
