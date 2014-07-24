package com.timur.tseries;


public class Regression {

	// private HashMap<Date, Double> data;
	private Double[] data;

	// public Regression(HashMap<Date, Double> data) {
	// this.data = data;
	// }
	public Regression(Double[] data) {
		this.data = data;
	}

	public void predictNext(int numOfTests) {
		// if(numOfTests > data.values().size()){
		if (numOfTests > data.length) {
			System.out.println("There must be less tests than pieces of data.");
			return;
		}
		Double predict = test(numOfTests);
		System.out.println("Prediction for last point using AR: " + predict);
	}
	
	private Double test(int numOfTests) {
		// Double[] values = data.values().toArray(new Double[0]);
		int ARorder = numOfTests;
		Double[] coeffs = TSeries.AR(data, ARorder);

		Double[] test = new Double[numOfTests];
		int start = test.length;
		int end = 0;

		for (int i = 0; i < test.length; i++) {
			test[--start] = data[data.length - ++end];
		}

		return TSeries.predictNextAR(test, coeffs);
	}

	// public HashMap<Date, Double> getData(){
	// return data;
	// }
	public Double[] getData() {
		return data;
	}

}
