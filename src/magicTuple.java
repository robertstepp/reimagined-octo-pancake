package src;

import java.time.LocalDate;
import java.util.ArrayList;

// https://stackoverflow.com/questions/19602601/create-an-arraylist-with-multiple-object-types
// I'd like to take this opportunity to thank Eclipse's "generate getters and setters!"  :]
class magicTuple {
	int[] colPos;
	ArrayList<ArrayList<String>> textVals = new ArrayList<ArrayList<String>>();
	ArrayList<LocalDate> dateVals = new ArrayList<LocalDate>();
	ArrayList<String[]> theRecs = new ArrayList<String[]>();

	magicTuple(int[] colPos, ArrayList<ArrayList<String>> textVals, ArrayList<LocalDate> dateVals,
			ArrayList<String[]> theRecs) {
		this.colPos = colPos;
		this.textVals = textVals;
		this.dateVals = dateVals;
		this.theRecs = theRecs;
	}

	/**
	 * @return the colPos
	 */
	public int[] getColPos() {
		return colPos;
	}

	/**
	 * @return the textVals
	 */
	public ArrayList<ArrayList<String>> getTextVals() {
		return textVals;
	}

	/**
	 * @return the dateVals
	 */
	public ArrayList<LocalDate> getDateVals() {
		return dateVals;
	}

	/**
	 * @return the theRecs
	 */
	public ArrayList<String[]> getTheRecs() {
		return theRecs;
	}

	/**
	 * @param colPos
	 *            the colPos to set
	 */
	public void setColPos(int[] colPos) {
		this.colPos = colPos;
	}

	/**
	 * @param textVals
	 *            the textVals to set
	 */
	public void setTextVals(ArrayList<ArrayList<String>> textVals) {
		this.textVals = textVals;
	}

	/**
	 * @param dateVals
	 *            the dateVals to set
	 */
	public void setDateVals(ArrayList<LocalDate> dateVals) {
		this.dateVals = dateVals;
	}

	/**
	 * @param theRecs
	 *            the theRecs to set
	 */
	public void setTheRecs(ArrayList<String[]> theRecs) {
		this.theRecs = theRecs;
	}
}
