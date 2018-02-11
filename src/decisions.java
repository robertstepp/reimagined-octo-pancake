// Java Program by Ross Hemphill & Robert Stepp 02/11/2018
package src;

import java.time.LocalDate;

public class decisions {
	LocalDate[] daCh;
	String[] bpCh;
	String tyCh;

	decisions(LocalDate[] daCh, String[] bpCh, String tyCh) {
		this.daCh = daCh;
		this.bpCh = bpCh;
		this.tyCh = tyCh;
	}

	/**
	 * @return the daCh
	 */
	public LocalDate[] getDaCh() {
		return daCh;
	}

	/**
	 * @return the bpCh
	 */
	public String[] getBpCh() {
		return bpCh;
	}

	/**
	 * @return the tyCh
	 */
	public String getTyCh() {
		return tyCh;
	}

	/**
	 * @param tyCh
	 *            the tyCh to set
	 */
	public void setTyCh(String tyCh) {
		this.tyCh = tyCh;
	}

	/**
	 * @param bpCh
	 *            the bpCh to set
	 */
	public void setBpCh(String[] bpCh) {
		this.bpCh = bpCh;
	}

	/**
	 * @param daCh
	 *            the daCh to set
	 */
	public void setDaCh(LocalDate[] daCh) {
		this.daCh = daCh;
	}
}
