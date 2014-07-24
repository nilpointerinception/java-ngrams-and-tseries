package com.timur.tseries;

import java.util.Scanner;

public class Main {

	static SpreadSheet googStockPriceDaily, hfStockPriceDaily, cpiEdu, mens400m;
//	static HashMap<Date, Double> googData, hfData, eduData, mens400mData;
	static Double[] googData, hfData, eduData, mens400mData;

	public static void main(String[] args) {

		googStockPriceDaily = new SpreadSheet("2DailyStockPrice_goog.xls");
		hfStockPriceDaily = new SpreadSheet("2DailyStockPrice_hf.xls");
		cpiEdu = new SpreadSheet("CPI_Education.xls");
		mens400m = new SpreadSheet("winning-times-for-the-mens-400-m.xls");

		googData = googStockPriceDaily.getData();

		System.out.println("Google Daily Stock Market Closing Prices");
		Regression reg = new Regression(googData);

		Scanner scan = new Scanner(System.in);
		System.out.print("How many tests would you like to perform? (" + googData.length
				+ " pieces of data): ");
		reg.predictNext(scan.nextInt());
		scan.close();
	}
}
