package src;

import java.awt.Image;
//import java.awt.List;
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

	/**
	 * structFromStream takes filepath, delimiter, date format, and column
	 * keywords of interest. In return, it provides an ArrayList whose
	 * first-dimension elements are: 0) the indices of columns of interest 1)
	 * all relevant values which occur in those columns, and 2) the line-by-line
	 * records themselves
	 * 
	 * @param file
	 *            String path to the file to parse
	 * @param del
	 *            String delimiter which separates fields
	 * @param daFo
	 *            String date format which LocalDate parses with
	 * @param cols
	 *            String[] containing textual column definitions (from the
	 *            header)
	 * @return An ArrayList of Objects, each of which are super-fun themselves
	 * @throws IOException
	 */
	public static magicTuple structFromStream(String file, String del, String daFo, String[] cols) throws IOException {
		// The columnar positions of the fields we're recording possible values
		// for
		// Index [0=beat, 1=sector, 2=precinct, 3=date]
		// TODO: This is hardcoded and would probably be more appropriately
		// represented by a linked list or something
		int[] whichCol = { -1, -1, -1, -1 };
		// Assumption: first column containing any occurrence of the keyword is
		// the one we want
		for (int q = 0; q < cols.length; ++q) {
			String col = cols[q].toLowerCase();
			if ((whichCol[0] == -1) && (col.contains("beat")))
				whichCol[0] = q;
			else if ((whichCol[1] == -1) && (col.contains("sector")))
				whichCol[1] = q;
			else if ((whichCol[2] == -1) && (col.contains("precinct")))
				whichCol[2] = q;
			else if ((whichCol[3] == -1) && (col.contains("date")))
				whichCol[3] = q;
		}

		// For unique or extreme values for the fields we'll present for user
		// selection
		ArrayList<String> beats = new ArrayList<String>();
		ArrayList<String> sectors = new ArrayList<String>();
		ArrayList<String> precincts = new ArrayList<String>();
		ArrayList<LocalDate> dates = new ArrayList<LocalDate>();
		// https://docs.oracle.com/javase/tutorial/datetime/iso/format.html
		// Index 0 is earliest date
		dates.add(LocalDate.now());
		// Index 1 is latest date
		dates.add(LocalDate.of(1900, 1, 1));
		// currDate is date of current record
		LocalDate currDate = LocalDate.of(1901, 1, 1);

		// Our precious records (etc)! (read every line after the header
		// until null)
		// (I imagine there's a better (more dynamic) way to do this, just don't
		// have the time to teach myself/research it at the moment)
		ArrayList<String[]> theRecords = new ArrayList<String[]>();
		theRecords.ensureCapacity(numberOfRows(file));
		BufferedReader br = new BufferedReader(new FileReader(file));
		// Ditch first line (presumed header)
		br.readLine();
		// TODO: should this be a do-while loop? Not very important
		String curLine = br.readLine();
		String[] splitLine;
		while (curLine != null) {
			splitLine = listFromString(curLine, del);
			// Discard record if different number of columns than header
			if (splitLine.length == cols.length) {
				theRecords.add(splitLine);
				// Add each unique occurrence of beats, sectors, and precincts
				// to our lists of possibilities
				if (!(beats.contains(splitLine[whichCol[0]])))
					beats.add(splitLine[whichCol[0]]);
				if (!(sectors.contains(splitLine[whichCol[1]])))
					sectors.add(splitLine[whichCol[1]]);
				if (!(precincts.contains(splitLine[whichCol[2]])))
					precincts.add(splitLine[whichCol[2]]);
				// Turn our date string into a LocalDate
				currDate = LocalDate.parse(splitLine[whichCol[3]], DateTimeFormatter.ofPattern(daFo));
				// If the current record's date is earlier and/or later than our
				// thus-far-seen extremes, it becomes the new extreme(s)
				if (currDate.isBefore(dates.get(0)))
					dates.set(0, currDate);
				if (currDate.isAfter(dates.get(1)))
					dates.set(1, currDate);
			}
			curLine = br.readLine();
		}
		br.close();

		// Make our lists of textual possibilities "naturally ordered"
		// (alphabetical) (We're preferring the order from the file for
		// precincts (better reflects cardinal biases, orientation of map image,
		// and reading direction))
		Collections.sort(beats);
		Collections.sort(sectors);
		ArrayList<ArrayList<String>> textVals = new ArrayList<ArrayList<String>>();
		textVals.add(new ArrayList<String>(beats));
		textVals.add(new ArrayList<String>(sectors));
		textVals.add(new ArrayList<String>(precincts));

		magicTuple allTheThings = new magicTuple(whichCol, textVals, dates, theRecords);
		return allTheThings;
	}

	public static void getChoices(String dateForm, ArrayList<LocalDate> dateLimits, String imgLoc,
			ArrayList<String> beatOpts, ArrayList<String> precOpts) {
		getDateRange(dateForm, dateLimits);
		displayMap(imgLoc);
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
	public static void getDateRange(String dateForm, ArrayList<LocalDate> dateLims) {
		LocalDate beginDate = LocalDate.now(), endDate = LocalDate.of(1900, 1, 1);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateForm);
		boolean validRange = false;
		while (!validRange) {
			JPanel dPanel = new JPanel();
			dPanel.add(new JLabel("Beginning date:"));
			// TODO: Move information from titlebar to inside dialog?
			// Each at 12 wide just barely fits informative titlebar on MY
			// computer...
			JTextField beginChoice = new JTextField(12);
			dPanel.add(beginChoice);
			dPanel.add(new JLabel("Ending date:"));
			JTextField endChoice = new JTextField(12);
			dPanel.add(endChoice);

			JOptionPane.showConfirmDialog(null, dPanel,
					"Please enter date range of interest: (Strictly formatted and from "
							+ dateLims.get(0).format(formatter) + " to " + dateLims.get(1).format(formatter) + ")",
					JOptionPane.DEFAULT_OPTION);

			// Next two conditionals ensure that neither entry is blank and each
			// contains "/". This is hardcoded and it would be better to handle
			// the exception.
			if (!((beginChoice.getText().equals("")) || (endChoice.getText().equals("")))) {
				if ((beginChoice.getText().contains("/")) && (beginChoice.getText().contains("/"))) {
					beginDate = LocalDate.parse(beginChoice.getText(), formatter);
					endDate = LocalDate.parse(endChoice.getText(), formatter);
					if (!((beginDate.isBefore(dateLims.get(0)) || (endDate.isAfter(dateLims.get(1))))
							|| (beginDate.isAfter(endDate)))) {
						validRange = true;
						// printDates(beginDate);
						// printDates(endDate);
					}
				}
			}
		}
	}

	/**
	 * Displays the Beat Map resized down.
	 **/
	public static void displayMap(String mapLoc) {
		JFrame frame = new JFrame();
		ImageIcon icon = new ImageIcon(
				new ImageIcon(mapLoc).getImage().getScaledInstance(-1, 1000, Image.SCALE_SMOOTH));
		JLabel label = new JLabel(icon);
		frame.add(label);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}

/** transitioning to other version, below
 * 
 * @param precincts
 * @param beats
 */
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

	/** this version is a placeholder which takes the updated param datatypes
	 * 
	 * @param theBeats
	 * @param thePrecincts
	 */
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

		/************************************/
		input();
		
		String[] colDefs = getColDefs(filename,delimiter);
		// Here's the "magic" sauce, it's our custom "tuple" class, which consists at the
		// first layer of four different named ArrayList/arrays. (textVals and theRecs have
		// another layer of their own.) See magicTuple.java for the definition. Most changes
		// for storing the data in here are in structFromStream() (renamed from recordsFromText()).
		// The best way to get a sense of it is perhaps starting at the code below.
		magicTuple allDat = structFromStream(filename,
				delimiter,
				dateFormat,
				colDefs);
		
		// PS I know not all the dialogs are popping up, but that's next on my agenda and
		// should be easy. The date structuring/returning thing was the big time hog. (I was
		// surprised to learn recently that it wasn't until the recent version of Apple's Swift
		// that, IIRC, tuples became returnable with builtins. Seems odd to me that a language
		// would allow methods/functions to take a pile of parameters but only allow returning
		// a single variable.)
		getChoices(dateFormat,
				allDat.getDateVals(),
				mapLoc,
				allDat.getTextVals().get(0),
				allDat.getTextVals().get(2));
		/************************************/
		// PPS If you leave the applet in the background (IE with the map display), Eclipse might view it as still running
		// (The image display is non-blocking.)
		
		if (debug) {
			printDates(allDat.getDateVals());
			printArray(allDat.getTextVals().get(0), ",");
			printArray(allDat.getTextVals().get(1), ",");
			printArray(allDat.getTextVals().get(2), ",");
			System.out.println();
			printArray(colDefs, " \t ");
			System.out.println("=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
			printArray(allDat.getTheRecs(), " \t ", true);
		}
	}
}
