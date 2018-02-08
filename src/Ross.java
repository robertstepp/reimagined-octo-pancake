package src;

import java.awt.Image;
import java.io.BufferedReader;
//import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
//import java.util.Arrays;
import java.util.Collections;
//import java.util.Date;
//import java.util.List;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

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

	public static ArrayList<String[]> recordsFromText(String file, String del, String[] cols) throws IOException {
		// The columnar positions of the fields we're recording unique or
		// max/min values for
		int bCol = -1, sCol = -1, pCol = -1, dCol = -1;
		// Assumption: first column which contains keyword is the one we want
		for (int c = 0; c < cols.length; ++c) {
			String col = cols[c].toLowerCase();
			if ((bCol == -1) && (col.contains("beat")))
				bCol = c;
			else if ((sCol == -1) && (col.contains("sector")))
				sCol = c;
			else if ((pCol == -1) && (col.contains("precinct")))
				pCol = c;
			else if ((dCol == -1) && (col.contains("date")))
				dCol = c;
		}

		// For holding each unique value possible for the fields we'll present
		// for user selection
		ArrayList<String> beats = new ArrayList<String>(), sectors = new ArrayList<String>(),
				precincts = new ArrayList<String>();
		// https://docs.oracle.com/javase/tutorial/datetime/iso/format.html
		// First actual date encountered will become the initial startDate and
		// endDate
		LocalDate startDate = LocalDate.MIN;
		LocalDate endDate = LocalDate.MAX;
		LocalDate currDate = LocalDate.MAX;

		// Our precious records! (from every line after the header until null)
		ArrayList<String[]> theRecords = new ArrayList<String[]>();
		theRecords.ensureCapacity(numberOfRows(file));
		BufferedReader br = new BufferedReader(new FileReader(file));
		// Ditch first line (presumed header)
		br.readLine();
		// TODO: should this be a do-while loop? Not particularly important
		String curLine = br.readLine();
		String[] splitLine;
		while (curLine != null) {
			splitLine = listFromString(curLine, del);
			// Discard record if different number of columns than header
			if (splitLine.length == cols.length) {
				theRecords.add(splitLine);
				// Add each unique occurrence of beats, sectors, and precincts
				// to our lists of possibilities
				if (!(beats.contains(splitLine[bCol])))
					beats.add(splitLine[bCol]);
				if (!(sectors.contains(splitLine[sCol])))
					sectors.add(splitLine[sCol]);
				if (!(precincts.contains(splitLine[pCol])))
					precincts.add(splitLine[pCol]);
				// Turn our date string into a LocalDate
				// If the current record's date is earlier and/or later than our
				// thus-far-seen extremes, it's the new extreme(s)
				currDate = LocalDate.parse(splitLine[dCol], DateTimeFormatter.ofPattern("MM/dd/yyyy"));
				if (currDate.isBefore(startDate))
					startDate = currDate;
				if (currDate.isAfter(endDate))
					endDate = currDate;
			}
			curLine = br.readLine();
		}
		br.close();

		// Make our lists of possibilities "naturally ordered" (alphabetical)
		Collections.sort(beats);
		Collections.sort(sectors);
		// We're preferring the order from the file
		// Collections.sort(precincts);

		// To group our date extremes
		ArrayList<LocalDate> dateRange = new ArrayList<LocalDate>();
		dateRange.add(startDate);
		dateRange.add(endDate);

		printArray(beats, ", ");
		System.out.println(beats.size());
		printArray(sectors, ", ");
		System.out.println(sectors.size());
		printArray(precincts, ", ");
		System.out.println(precincts.size());
		return theRecords;
	}

	public static void getChoices(ArrayList<LocalDate> limits, String imgLoc, ArrayList<String> beatOpts,
			ArrayList<String> precOpts) {
		getDateRange(limits);
		displayMap(imgLoc);
		getBeatPrecinct(beatOpts, precOpts);
	}

	public static void getDateRange(ArrayList<LocalDate> extremes) {

	}

	public static void displayMap(String mapLoc) {
		/*
		 * Displays the Beat Map resized down.
		 **/
		JFrame frame = new JFrame();
		ImageIcon icon = new ImageIcon(
				new ImageIcon(mapLoc).getImage().getScaledInstance(-1, 1000, Image.SCALE_SMOOTH));
		JLabel label = new JLabel(icon);
		frame.add(label);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}

	public static void getBeatPrecinct(ArrayList<String> theBeats, ArrayList<String> thePrecincts) {

	}

	public static String[] input() {
		String[] types = { "Personal", "Property" };
		// displayMap("src/beat-map-2.png");
		// return types;
		String[] precincts = { "n", "s" };
		String[] beats = { "q1", "r3" };

		String[] input = new String[5]; // Filename, Precinct, Beat, Date Range,
										// Type (Personal/Property)

		JPanel inputFilename = new JPanel();
		JTextField filename = new JTextField(10);
		inputFilename.add(new JLabel("Filename: (Case Sensitive)"));
		inputFilename.add(filename);
		JOptionPane.showConfirmDialog(null, inputFilename, "Please Enter Filename:", JOptionPane.OK_CANCEL_OPTION);
		System.out.println(filename.getText());

		// Text boxes etc
		JPanel inputDate = new JPanel();
		JTextField startDate = new JTextField(10);
		JTextField endDate = new JTextField(10);
		inputDate.add(startDate);
		inputDate.add(Box.createHorizontalStrut(15));
		inputDate.add(new JLabel("End Date:"));
		inputDate.add(endDate);
		
		JPanel inputArea = new JPanel();
		JComboBox<?> precinct = new JComboBox<Object>(precincts);
		JComboBox<?> beat = new JComboBox<Object>(beats);

		JPanel inputType = new JPanel();
		JComboBox<?> type = new JComboBox<Object>(types);

		// Applying names etc inputDate.add(new JLabel("Start Date:"));
		inputArea.add(new JLabel("Precinct:"));
		inputArea.add(precinct);

		inputDate.add(Box.createHorizontalStrut(15));

		inputArea.add(new JLabel("Beat:"));
		inputArea.add(beat);

		inputType.add(new JLabel("Type requested:"));
		inputType.add(type);

		JOptionPane.showConfirmDialog(null, inputDate, "Enter requested dates:", JOptionPane.OK_CANCEL_OPTION);
		JOptionPane.showConfirmDialog(null, inputArea, "Enter requested area:", JOptionPane.OK_CANCEL_OPTION);
		JOptionPane.showConfirmDialog(null, inputType, "Enter requested type:",
				JOptionPane.OK_CANCEL_OPTION); /*
												 * The following is how to
												 * return the data the user
												 * inputs I was testing using an
												 * output to see the returned
												 * data.
												 */

		// System.out.println(types[type.getSelectedIndex()]);
		// System.out.println("Filename value: " + filename.getText());
		// System.out.printf("Start date: %s End date: %s\n",
		// startDate.getText(),
		// endDate.getText());
		// System.out.printf("Precinct: %s Beat: %s\n",
		// precincts[precinct.getSelectedIndex()],
		// beats[beat.getSelectedIndex()]);
		// System.out.println("Precinct value: " + precinct.getText());
		// System.out.println("Date value: " + date.getText());

		return input;
	}

	public static void main(String[] args) throws IOException {
		final boolean debug = true;
		String filename = "datasets/original raw data-DON'T MODIFY.csv";
		String mapLoc = "src/beat-map-2.png";
		String delimiter = ",";
		input();
		ArrayList<String> a = new ArrayList<String>();
		// getChoiceFromOpts(a, a, a, mapLoc);

		String[] columns = getColDefs(filename, delimiter);
		ArrayList<String[]> rows = new ArrayList<String[]>(recordsFromText(filename, delimiter, columns));

		if (debug) {
			printArray(rows, " \t ", true);
			System.out.println("---");
			printArray(columns, " \t ");
			System.out.println("---");
		}
	}
}
