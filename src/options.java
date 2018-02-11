// Java Program by Ross Hemphill & Robert Stepp 11 Feb 2018
package src;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * I'm currently confusing myself with "How did I create this and then not use
 * it?!" TODO: Is this trash, self? Testing for use for the crime classes? I bet
 * I was feeling hopeful about removing hardcodes by have figured out named
 * sublists by LinkedHashMap's... or sumpin
 * 
 * @author Ross
 *
 */
public class options {
	LocalDate[] daRn;
	String[] beOp;
	String[] prOp;
	Map<String, String[]> bpAs = new LinkedHashMap<String, String[]>();
	String[] tyOp;

	options(LocalDate[] daRn, String[] beOp, String[] prOp,
			LinkedHashMap<String, String[]> bpAs, String[] tyOp) {
		this.daRn = daRn;
		this.beOp = beOp;
		this.prOp = prOp;
		this.bpAs = bpAs;
		this.tyOp = tyOp;
	}

	/**
	 * @return the daRn
	 */
	public LocalDate[] getDaRn() {
		return daRn;
	}

	/**
	 * @return the beOp
	 */
	public String[] getBeOp() {
		return beOp;
	}

	/**
	 * @return the prOp
	 */
	public String[] getPrOp() {
		return prOp;
	}

	/**
	 * @return the bpAs
	 */
	public Map<String, String[]> getBpAs() {
		return bpAs;
	}

	/**
	 * @return the tyOp
	 */
	public String[] getTyOp() {
		return tyOp;
	}

	/**
	 * @param daRn
	 *            the daRn to set
	 */
	public void setDaRn(LocalDate[] daRn) {
		this.daRn = daRn;
	}

	/**
	 * @param beOp
	 *            the beOp to set
	 */
	public void setBeOp(String[] beOp) {
		this.beOp = beOp;
	}

	/**
	 * @param prOp
	 *            the prOp to set
	 */
	public void setPrOp(String[] prOp) {
		this.prOp = prOp;
	}

	/**
	 * @param bpAs
	 *            the bpAs to set
	 */
	public void setBpAs(Map<String, String[]> bpAs) {
		this.bpAs = bpAs;
	}

	/**
	 * @param tyOp
	 *            the tyOp to set
	 */
	public void setTyOp(String[] tyOp) {
		this.tyOp = tyOp;
	}
}
