package com.timur.ngrams;

import java.util.HashMap;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		String filePath = "./res/user-ct-test-collection-07.txt";
		NGrams autoComplete = new NGrams(filePath, 3);
		HashMap<String, HashMap<String, Double>> test = autoComplete.getNGHash();
		@SuppressWarnings("resource")
		Scanner scan = new Scanner(System.in);
		while(true){
			System.out.print("Please enter a search term: ");
			String input = scan.nextLine();
			if(test.containsKey(input)){
				System.out.println("Possibile next words: ");
				for(String s : test.get(input).keySet()){
					System.out.println("\t" + s);
				}
			}else{
				System.out.println("No auto-complete found for your search.");
			}
		}
	}
}
