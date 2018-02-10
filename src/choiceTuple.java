package src;

import java.time.LocalDate;

public class choiceTuple {
	String prCh;
	String beCh;
	LocalDate[] daCh;

	choiceTuple(String prCh, String beCh, LocalDate[] daCh) {
		this.prCh = prCh;
		this.beCh = beCh;
		this.daCh = daCh;
	}

	/**
	 * @return the prCh
	 */
	public String getPrCh() {
		return prCh;
	}

	/**
	 * @return the beCh
	 */
	public String getBeCh() {
		return beCh;
	}

	/**
	 * @return the daCh
	 */
	public LocalDate[] getDaCh() {
		return daCh;
	}

	/**
	 * @param prCh the prCh to set
	 */
	public void setPrCh(String prCh) {
		this.prCh = prCh;
	}

	/**
	 * @param beCh the beCh to set
	 */
	public void setBeCh(String beCh) {
		this.beCh = beCh;
	}

	/**
	 * @param daCh the daCh to set
	 */
	public void setDaCh(LocalDate[] daCh) {
		this.daCh = daCh;
	}
}
