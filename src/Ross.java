package src;

import java.io.BufferedReader;
//import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;

public class Ross {
	public static void printArray(ArrayList<String> al, String delim) {
		for (String s : al)
			System.out.print(s + delim);
		System.out.println();
	}

	public static void printArray(String[] sa, String delim) {
		for (String s : sa)
			System.out.print(s + delim);
		System.out.println();
	}

	/**
	 * The boolean is just to differentiate from printArray for
	 * ArrayList<String>'s (vs. ArrayList<String[]>'s) (Turns out greater
	 * than/less than symbols don't show in javadoc/Eclipse hoverover...)
	 * 
	 * @param outtie
	 * @param delim
	 * @param special
	 *            Boolean value doesn't matter
	 */
	public static void printArray(ArrayList<String[]> outtie, String delim, boolean special) {
		// TODO: Either add ranges as a param, or remove "j" conditionals when
		// done testing
		int j = 0;
		for (String[] innie : outtie) {
			if ((j < 10) || (j > outtie.size() - 10))
				printArray(innie, " \t ");
			++j;
		}
	}

	/**
	 * getColDefs finds column definitions from a text file's header
	 * (Assumption: single line header.)
	 * 
	 * @param file
	 *            A String telling us the file's location
	 * @param delim
	 *            The splitting delimiter as a String
	 * @return A String ArrayList containing the column headings
	 * @throws IOException
	 */
	public static String[] getColDefs(String file, String delim) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(file));
		// ArrayList<String> cols = new
		// ArrayList<>(Arrays.asList(br.readLine().split(delim)));
		String[] columns = listFromString(br.readLine(), delim);
		br.close();
		return columns;
	}

	/**
	 * numberOfRows determines number of rows in a text file (Assumptions:
	 * single line header, empty lines are valid (eg as from final newline).)
	 * 
	 * @param file
	 *            A String of the file's location/name
	 * @return An Int holding the number of lines
	 * @throws IOException
	 */
	public static int numberOfRows(String file) throws IOException {
		LineNumberReader lineReader = new LineNumberReader(new FileReader(file));
		lineReader.skip(Long.MAX_VALUE);
		int numRows = lineReader.getLineNumber();
		lineReader.close();
		return numRows;
	}

	/**
	 * listFromString takes a String and returns a list, split by delimiter and
	 * escaped by double quotes. Also strips double quotes. (Assumption: double
	 * quote is the escape character and the delimiter's a different character.
	 * Untested: del being more than one character.)
	 * 
	 * @param line
	 *            The String to be listified
	 * @param del
	 *            The (non-double-quote) delimiter we're splitting on
	 * @return The split String as a list
	 */
	public static String[] listFromString(String line, String del) {
		// https://stackoverflow.com/questions/15738918/splitting-a-csv-file-with-quotes-as-text-delimiter-using-string-split
		// Split on del except within double quotes
		String[] list = line.split(del + "(?=([^\"]|\"[^\"]*\")*$)");
		for (int q = 0; q < list.length; ++q)
			// Replace double quotes with nothing
			list[q] = list[q].replace("\"", "");
		return list;
	}

	public static ArrayList<String[]> recordsFromText(String file, String del, int cols) throws IOException {
		ArrayList<String[]> theRecords = new ArrayList<String[]>();
		theRecords.ensureCapacity(numberOfRows(file));
		BufferedReader br = new BufferedReader(new FileReader(file));
		// Ditch first line (presumed header)
		br.readLine();
		// TODO: make a different kinda loop? (avoid repeating curLine
		// assignment)
		String curLine = br.readLine();
		String[] splitLine;
		while (curLine != null) {
			splitLine = listFromString(curLine, del);
			if (splitLine.length == cols)
				// System.out.print("~");
				theRecords.add(splitLine);
			curLine = br.readLine();
		}
		br.close();
		return theRecords;
	}

	public static void main(String[] args) throws IOException {
		final boolean debug = true;
		String filename = "datasets/original raw data-DON'T MODIFY.csv";
		String delimiter = ",";

		String[] columns = getColDefs(filename, delimiter);
		ArrayList<String[]> rows = new ArrayList<String[]>(recordsFromText(filename, delimiter,columns.length));

		if (debug) {
			printArray(rows, " \t ", true);
			System.out.println("---");
			printArray(columns, " \t ");
			System.out.println("---");
		}
	}
}
