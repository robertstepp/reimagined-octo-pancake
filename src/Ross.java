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

//import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Ross {
	public static void printDates(ArrayList<LocalDate> mydates) {

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

		for (LocalDate date : mydates) {
			System.out.println(date.format(formatter));
		}
	}

	public static void printDates(LocalDate mydate) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
		System.out.println(mydate.format(formatter));
	}

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

	public static ArrayList<LocalDate> recordsFromText(String file, String del, String daFo, String[] cols)
			throws IOException {
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
		LocalDate startDate = LocalDate.now();
		LocalDate endDate = LocalDate.of(1900, 1, 1);
		LocalDate currDate = LocalDate.of(1901, 1, 1);

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
				currDate = LocalDate.parse(splitLine[dCol], DateTimeFormatter.ofPattern(daFo));
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

		// printDates(startDate);
		// printDates(endDate);

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

		// printDates(dateRange);
		return dateRange;
		// return theRecords;
	}

	public static void getChoices(String dateForm, ArrayList<LocalDate> dateLimits, String imgLoc, String[] beatOpts,
			String[] precOpts) {

		getDateRange(dateForm, dateLimits);

		// displayMap("src/beat-map-2.png");
		displayMap(imgLoc);

		String[] beats = { "q1", "r3" };
		String[] precincts = { "n", "s" };
		getBeatPrecinct(beatOpts, precOpts);

		String[] types = { "Personal", "Property" };
		getTypeOfCrime(types);

	}

	/**
	 * getDateRange uses a dialog box to obtain from the user the range of dates
	 * to consider. Informs of the range available to them and, incidentally, of
	 * the formatting required. (Strict matching of format, including
	 * superfluous zeroes.)
	 * 
	 * @param dateFormat
	 *            A String determining the date format printed and expected
	 * @param dateLims
	 *            ArrayList of LocalDate's. First element is earliest
	 *            permissible date, second latest
	 */
	public static void getDateRange(String dateFormat, ArrayList<LocalDate> dateLims) {
		LocalDate beginDate = LocalDate.now(), endDate = LocalDate.of(1900, 1, 1);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);
		boolean validRange = false;
		while (!validRange) {
			JPanel dPanel = new JPanel();
			JTextField beginChoice = new JTextField(10);
			dPanel.add(new JLabel("Beginning date:"));
			dPanel.add(beginChoice);
			JTextField endChoice = new JTextField(10);
			dPanel.add(new JLabel("Ending date:"));
			dPanel.add(endChoice);

			JOptionPane.showConfirmDialog(null,
					dPanel, "Indicate date range of interest: (Strictly formatted, from: "
							+ dateLims.get(0).format(formatter) + " to " + dateLims.get(1).format(formatter) + ")",
					JOptionPane.DEFAULT_OPTION);

			beginDate = LocalDate.parse(beginChoice.getText(), DateTimeFormatter.ofPattern(dateFormat));
			endDate = LocalDate.parse(endChoice.getText(), DateTimeFormatter.ofPattern(dateFormat));
			if (!((beginDate.isBefore(dateLims.get(0)) || (endDate.isAfter(dateLims.get(1))))
					|| (beginDate.isAfter(endDate)))) {
				validRange = true;
			} else {
				// System.out.println("You have failed!");
			}
		}
	}

	public static void getBeatPrecinct(String[] precincts, String[] beats) {
		JPanel inputArea = new JPanel();
		JComboBox<?> precinct = new JComboBox<Object>(precincts);
		JComboBox<?> beat = new JComboBox<Object>(beats);
		inputArea.add(new JLabel("Precinct:"));
		inputArea.add(precinct);
		inputArea.add(new JLabel("Beat:"));
		inputArea.add(beat);
		JOptionPane.showConfirmDialog(null, inputArea, "Enter requested area:", JOptionPane.OK_CANCEL_OPTION);
		// System.out.printf("Precinct: %s Beat: %s\n",
		// precincts[precinct.getSelectedIndex()],
		// beats[beat.getSelectedIndex()]);
		// System.out.println("Precinct value: " + precinct.getText());

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

	public static void getTypeOfCrime(String[] types) {
		JPanel inputType = new JPanel();
		JComboBox<?> type = new JComboBox<Object>(types);
		inputType.add(new JLabel("Type requested:"));
		inputType.add(type);
		JOptionPane.showConfirmDialog(null, inputType, "Enter requested type:", JOptionPane.OK_CANCEL_OPTION);
		// System.out.println(types[type.getSelectedIndex()]);

	}

	public static void input() {

		/*
		 * JPanel inputFilename = new JPanel(); JTextField filename = new
		 * JTextField(10); inputFilename.add(new
		 * JLabel("Filename: (Case Sensitive)")); inputFilename.add(filename);
		 * JOptionPane.showConfirmDialog(null, inputFilename,
		 * "Please Enter Filename:", JOptionPane.OK_CANCEL_OPTION);
		 */
		// System.out.println("Filename value: " + filename.getText());

	}

	public static void main(String[] args) throws IOException {
		// Following block are currently hardcoded
		final boolean debug = true;
		String filename = "datasets/original raw data-DON'T MODIFY.csv";
		String mapLoc = "src/beat-map-2.png";
		String delimiter = ",";
		String dateFormat = "MM/dd/yyyy";

		input();
		String[] columns = getColDefs(filename, delimiter);
		ArrayList<LocalDate> rows = new ArrayList<LocalDate>(recordsFromText(filename, delimiter, dateFormat, columns));

		String[] bo = { "a", "b" };
		String[] po = { "c", "d" };
		getChoices(dateFormat, rows, mapLoc, bo, po);

		if (debug) {
			// printArray(rows, " \t ", true);
			printDates(rows);
			System.out.println("---");
			printArray(columns, " \t ");
			System.out.println("---");
		}
	}
}
