package com.timur.tseries;

import java.io.File;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

public class SpreadSheet {

	private String filePath;
	private Double[] data;

	// private static HashMap<Date, Double> dateAndValue;

	public SpreadSheet(String fileName) {
		filePath = "./res/GoodDatasets/" + fileName;
		// dateAndValue = new HashMap<>();
		extractData();
	}

	private void extractData() {
		try {
			Workbook workbook = Workbook.getWorkbook(new File(filePath));
			Sheet sheet = workbook.getSheet(0);
			data = new Double[sheet.getRows()];
			for (int i = 1; i < sheet.getRows(); i++) {
				Cell cellValue = sheet.getCell(1, i);
				data[i] = Double.parseDouble(cellValue.getContents());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Double[] getData() {
		return data;
	}

	// private void extractData() {
	// try {
	// Workbook workbook = Workbook.getWorkbook(new File(filePath));
	// Sheet sheet = workbook.getSheet(0);
	// for (int i = 1; i < sheet.getRows(); i++) {
	// Cell cellDate = sheet.getCell(0, i);
	// Cell cellValue = sheet.getCell(1, i);
	// SimpleDateFormat sdf = new SimpleDateFormat("EE dd-MMM-YY");
	// try{
	// Date thisDate = sdf.parse(cellDate.getContents());
	// double thisValue = Double.parseDouble(cellValue.getContents());
	// dateAndValue.put(thisDate, thisValue);
	// }catch(ParseException e){
	// continue;
	// }
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
	//
	// public HashMap<Date, Double> getDateAndValue() {
	// return dateAndValue;
	// }

}
