// Java Program by Ross Hemphill & Robert Stepp 11 Feb 2018
package src;

import java.util.ArrayList;

public class selecARec {
	ArrayList<String[]> beatRecs = new ArrayList<String[]>();
	ArrayList<String[]> precRecs = new ArrayList<String[]>();

	selecARec(ArrayList<String[]> beatRecs, ArrayList<String[]> precRecs) {
		this.beatRecs = beatRecs;
		this.precRecs = precRecs;
	}

	/**
	 * @return the beatRecs
	 */
	public ArrayList<String[]> getBeatRecs() {
		return beatRecs;
	}

	/**
	 * @return the precRecs
	 */
	public ArrayList<String[]> getPrecRecs() {
		return precRecs;
	}

	/**
	 * @param beatRecs
	 *            the beatRecs to set
	 */
	public void setBeatRecs(ArrayList<String[]> beatRecs) {
		this.beatRecs = beatRecs;
	}

	/**
	 * @param precRecs
	 *            the precRecs to set
	 */
	public void setPrecRecs(ArrayList<String[]> precRecs) {
		this.precRecs = precRecs;
	}
}
