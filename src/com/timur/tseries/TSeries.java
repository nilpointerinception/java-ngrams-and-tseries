package com.timur.tseries;

import java.util.Arrays;

// implements AutoRegressive (AR) Model or AutoRegressive Integrated (ARI) Model on a time series.

// Uses Burg's alogorithm as demonstrated in "Burg's Method, Algorithm, and Recursion" (November 2009) by Cedrick Collomb

public class TSeries {

	public static Double[] AR(Double[] y, int order) {

		Double[] coeffs = new Double[order];

		Double[] x = new Double[y.length];
		for (int i = 0; i < y.length; i++)
			x[i] = y[i];

		// GET SIZE FROM INPUT VECTORS
		int N = x.length - 1;
		int m = coeffs.length;

		// INITIALIZE Ak
		Double[] Ak = new Double[m + 1];
		Ak[0] = 1.0;

		// INITIALIZE f and b
		Double[] f = new Double[x.length];
		Double[] b = new Double[x.length];
		for (int i = 0; i < x.length; i++) {
			f[i] = b[i] = x[i];
		}

		// INITIALIZE Dk
		Double Dk = 0.0;
		for (int j = 0; j <= N; j++) {
			Dk += 2.0 * f[j] * f[j];
		}
		Dk -= f[0] * f[0] + b[N] * b[N];

		// BURG RECURSION
		for (int k = 0; k < m; k++) {
			// COMPUTE MU
			Double mu = 0.0;
			for (int n = 0; n <= N - k - 1; n++) {
				mu += f[n + k + 1] * b[n];
			}
			mu *= -2.0 / Dk;

			// UPDATE Ak
			for (int n = 0; n <= (k + 1) / 2; n++) {
				Double t1 = Ak[n] + mu * Ak[k + 1 - n];
				Double t2 = Ak[k + 1 - n] + mu * Ak[n];
				Ak[n] = t1;
				Ak[k + 1 - n] = t2;
			}

			// UPDATE f and b
			for (int n = 0; n <= N - k - 1; n++) {
				Double t1 = f[n + k + 1] + mu * b[n];
				Double t2 = b[n] + mu * f[n + k + 1];
				f[n + k + 1] = t1;
				b[n] = t2;
			}

			// UPDATE Dk
			Dk = (1.0 - mu * mu) * Dk - f[k + 1] * f[k + 1] - b[N - k - 1]
					* b[N - k - 1];
		}

		// ASSIGN COEFFICIENTS
		for (int i = 1; i < Ak.length; i++)
			coeffs[i - 1] = Ak[i];

		return coeffs;
	}

	public static Double[] ARI(Double[] y, int ARorder, int Iorder) {

		Double[] series = new Double[y.length];
		for (int i = 0; i < y.length; i++)
			series[i] = y[i];

		// Do differencing for the "I" portion of ARIMA
		for (int i = 0; i < Iorder; i++) {
			difference(series);
		}

		// Differencing means that a few points off the beginning of the series
		// must be removed
		// (Technically, Iorder # of points must be removed)
		Double[] newSeries = new Double[series.length - Iorder];
		for (int i = Iorder; i < series.length; i++)
			newSeries[i - Iorder] = series[i];

		series = newSeries;

		Double[] coef = AR(y, ARorder);

		return coef;
	}

	private static void difference(Double[] series) {
		for (int i = series.length - 1; i > 0; i--) {
			series[i] = series[i] - series[i - 1];
		}
	}

	@SuppressWarnings("unused")
	private static Double meanAdjust(Double[] data) {
		Double mean = getMean(data);

		if (mean != 0) {
			for (int i = 0; i < data.length; i++)
				data[i] = (data[i] - mean);
		}
		return mean;
	}

	private static Double getMean(Double[] data) {
		Double result = 0D;
		for (int i = 0; i < data.length; i++)
			result += data[i];
		return (result / data.length);
	}

	// Double[] prevValues hold the previous e values (for order e). Eg, for
	// order 3, it'd hold the values at: [t-3, t-2, t-1]
	public static Double predictNextAR(Double[] prevValues, Double[] coeffs) {
		if (prevValues.length < coeffs.length) {
			System.out.println("Model is order " + coeffs.length + "!");
			System.out
					.println("Need to pass predictNext a Double array of at least length "
							+ coeffs.length
							+ " containing the "
							+ coeffs.length + " previous values!");
			return -1D;
		}

		Double result = 0D;

		for (int i = 0; i < coeffs.length; i++) {
			result -= coeffs[i] * prevValues[prevValues.length - i - 1];

		}

		return result;
	}

	// Double[] prevValues hold the previous ARorder + Iorder values (for
	// ARorder and Iorder). Eg, for ARorder 3 and Iorder 1, it'd hold the values
	// at: [t-4, t-3, t-2, t-1]
	public static Double predictNextARI(Double[] prevValues, Double[] coeffs,
			int Iorder) {
		if (prevValues.length < (coeffs.length + Iorder)) {
			System.out.println("Model is AR order " + coeffs.length
					+ " and I order " + Iorder + "!");
			System.out
					.println("Need to pass predictNext a Double array of at least length "
							+ (coeffs.length + Iorder)
							+ " containing the "
							+ (coeffs.length + Iorder) + " previous values!");
			return -1D;
		}

		Double[][] series = new Double[Iorder + 1][prevValues.length];

		for (int i = 0; i < prevValues.length; i++)
			series[0][i] = prevValues[i];

		// Do differencing for the "I" portion of ARIMA
		for (int i = 0; i < Iorder; i++) {
			series[i + 1] = Arrays.copyOf(series[i], series[i].length);
			difference(series[i + 1]);

			// Differencing means that a few points off the beginning of the
			// series must be removed
			// (Technically, Iorder # of points must be removed)
			Double[] newSeries = new Double[series[i + 1].length - 1];
			for (int j = 1; j < series[i + 1].length; j++)
				newSeries[j - 1] = series[i + 1][j];

			series[i + 1] = newSeries;

		}

		Double result = 0D;

		for (int i = 0; i < coeffs.length; i++) {
			result -= coeffs[i]
					* series[series.length - 1][series[series.length - 1].length
							- i - 1];

		}

		Double value = result;

		for (int i = Iorder - 1; i >= 0; i--)
			value += series[i][series[i].length - 1];

		return value;
	}

	public static void outputEquationAR(Double[] coeff) {
		String eq = "Value_t = ";
		for (int i = 0; i < coeff.length; i++) {
			if (i != 0)
				eq += " + ";
			eq += "( " + (-1 * coeff[i]) + " * Value_{t - " + (i + 1) + "} )";
		}

		System.out.println(eq);
	}

	public static void outputEquationARI(Double[] coeff) {
		String eq = "Value_t Dif = ";
		for (int i = 0; i < coeff.length; i++) {
			if (i != 0)
				eq += " + ";
			eq += "( " + (-1 * coeff[i]) + " * Value_{t - " + (i + 1)
					+ "} Dif )";
		}

		System.out.println(eq);
	}

}
