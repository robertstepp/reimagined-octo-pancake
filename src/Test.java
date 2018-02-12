package src;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import javafx.util.Pair;

class Test {
	/* This method returns a Pair which has maximum score */
	public static Pair<String, Integer> getMaximum(
			ArrayList<Pair<String, Integer>> l) {
		// Assign minimum value initially
		int max = Integer.MIN_VALUE;

		// Pair to store the maximum marks of a student with its name
		Pair<String, Integer> ans = new Pair<String, Integer>("", 0);

		// Using for each loop to iterate array of Pair Objects
		for (Pair<String, Integer> temp : l) {
			// Get the score of Student
			int val = temp.getValue();

			// Check if it is greater than the previous maximum marks
			if (val > max) {
				max = val; // update maximum
				ans = temp; // update the Pair
			}
		}
		return ans;
	}

	public static void displayRecs(
			LinkedHashMap<String, Integer> listOfCrimeScores, String key) {

		System.out.printf("%s: %d instances\n", key,
				listOfCrimeScores.get(key));
	}

	// Driver method to test above method
	public static void main(String[] args) {
		int n = 5;// Number of Students
		LinkedHashMap<String, Integer> listOfCrimeScores = new LinkedHashMap<String, Integer>();
		listOfCrimeScores.put("Robbery", 10);
		listOfCrimeScores.put("Burglery", 20);
		displayRecs(listOfCrimeScores, "Robbery");
		// Create an Array List
		ArrayList<Pair<String, Integer>> l = new ArrayList<Pair<String, Integer>>();

		/*
		 * Create pair of name of student with their corresponding score and
		 * insert into the Arraylist
		 */
		l.add(new Pair<String, Integer>("Student A", 90));
		l.add(new Pair<String, Integer>("Student B", 54));
		l.add(new Pair<String, Integer>("Student C", 99));
		l.add(new Pair<String, Integer>("Student D", 88));
		l.add(new Pair<String, Integer>("Student E", 89));

		// get the Pair which has maximum value
		Pair<String, Integer> ans = getMaximum(l);

		System.out.println(ans.getKey() + " is top scorer " + "with score of "
				+ ans.getValue());
	}
}