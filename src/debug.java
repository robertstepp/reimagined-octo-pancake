package src;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * This class holds methods used in debugging Robert Stepp and Ross Hemphill's
 * CS142 class project (primarily by printing to console to check stored data)
 * 
 * @author Ross
 *
 */
public class debug {
	/**
	 * this printDates will print an ArrayList of LocalDate's with requested
	 * format, one per line
	 * 
	 * @param mydates
	 *            ArrayList of LocalDates to print
	 * @param format
	 *            Formatting String for LocalDate/DateTimeFormatter
	 */
	public static void printDates(ArrayList<LocalDate> mydates, String format) {

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);

		for (LocalDate date : mydates) {
			System.out.println(date.format(formatter));
		}
	}

	/**
	 * this printDates will print a single LocalDate with required formatting
	 * and newline
	 * 
	 * @param mydate
	 *            LocalDate to print
	 * @param format
	 *            Format String for DateTimeFormatter
	 */
	public static void printDates(LocalDate mydate, String format) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
		System.out.println(mydate.format(formatter));
	}

	/**
	 * this PrintArray is for an ArrayList of Strings, to be printed with a
	 * delimiter after each String and a terminating newline
	 * 
	 * @param al
	 *            ArrayList of Strings
	 * @param delim
	 *            Delimiter between Strings
	 */
	public static void printArray(ArrayList<String> al, String delim) {
		for (String s : al)
			System.out.print(s + delim);
		System.out.println();
	}

	/**
	 * this printArray is for an array of Strings, printed with delimiters and
	 * terminating newline
	 * 
	 * @param sa
	 *            Array of Strings
	 * @param delim
	 *            Delimiter after Springs
	 */
	public static void printArray(String[] sa, String delim) {
		for (String s : sa)
			System.out.print(s + delim);
		System.out.println();
	}

	/**
	 * The boolean is just to differentiate from printArray for
	 * ArrayList-String's (vs. ArrayList-String[]'s)
	 * 
	 * @param outtie
	 *            Our ArrayList of String Arrays (sic)
	 * @param delim
	 *            Delimiter between items printed
	 * @param special
	 *            Boolean value doesn't matter
	 */
	public static void printArray(ArrayList<String[]> outtie, String delim, boolean special) {
		// TODO: Either add ranges as a param, or remove "j" conditionals when
		// done testing
		int j = 0;
		for (String[] innie : outtie) {
			if ((j < 3) || (j > outtie.size() - 4))
				printArray(innie, " \t ");
			++j;
		}
	}

}
