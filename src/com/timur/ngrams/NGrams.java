package com.timur.ngrams;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class is used to implement N-Grams for auto-completion.
 * 
 * @author Timur
 *
 */
public class NGrams {

	private static HashMap<String, HashMap<String, Double>> ngHash;
	private static LinkedList<String> searchTerms;

	public NGrams(String path, int n) {
		ngHash = new HashMap<String, HashMap<String, Double>>();
		searchTerms = new LinkedList<String>();
		extractTextFile(new File(path));
		storeSearchTerms(n);
		calculateScores();
	}

	/**
	 * After getting all the required information in our {@link ngHash}, this
	 * method takes that acquired data and performs the logic behind N-Grams.
	 */
	private void calculateScores() {
		for (String start : ngHash.keySet()) {
			if (ngHash.get(start).size() <= 1)
				continue;
			else
				for (String s : ngHash.get(start).keySet()) {
					Double size = (double) ngHash.get(start).size();
					ngHash.get(start).put(s, 1D / size);
				}
		}
	}

	/**
	 * Goes through every search term and finds n-tuples which are added to the
	 * hashtable, {@link ngHash}.
	 * 
	 * @param n
	 *            The variable of n-tuples. Only works with n = 3 for the scope
	 *            of this Assignment.
	 */
	private void storeSearchTerms(int n) {
		for (String s : searchTerms) {
			List<String> split = Arrays.asList(s.split(" "));
			HashMap<String, Double> inner = new HashMap<String, Double>();
			for (int i = 0; i < split.size() - 2; i++) {
				String key = split.get(i + 2);
				inner.put(key, 1D);
				String masterKey = split.get(i) + " " + split.get(i + 1);
				ngHash.put(masterKey, inner);
			}
		}
	}

	/**
	 * Adds "<S>" to the front and "</S>" to the end of every search term.
	 * 
	 * @param terms
	 *            The terms which were extracted from the text file in
	 *            {@link extractTextFile}.
	 */
	private void addTags(LinkedList<String> terms) {
		for (String s : terms)
			searchTerms.add("<S> " + s + " </S>");
	}

	/**
	 * Extracts a text file containing AOL search data and stores it in a
	 * LinkedList containing all search terms, these are then used in the
	 * {@link addTags} method.}
	 * 
	 * @param f
	 *            The text file being extracted.
	 */
	private void extractTextFile(File f) {
		try {
			Scanner scan = new Scanner(f);
			LinkedList<String> terms = new LinkedList<String>();
			while (scan.hasNext()) {
				String line = scan.nextLine();
				Pattern pattern = Pattern.compile("\\d{6}\\s[\\w ]+");
				Matcher matcher = pattern.matcher(line);
				while (matcher.find()) {
					String match = matcher.group();
					String term = match.substring(7).trim();
					if (term.isEmpty())
						continue;
					terms.add(term);
				}
			}
			addTags(terms);
			scan.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public HashMap<String, HashMap<String, Double>> getNGHash(){
		return ngHash;
	}
}
