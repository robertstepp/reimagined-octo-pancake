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
import java.util.LinkedHashMap;
import java.util.Map;

//import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Ross {
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
		// ArrayList<LocalDate> dates = new ArrayList<LocalDate>();
		LocalDate[] daDates = new LocalDate[2];

		// https://docs.oracle.com/javase/tutorial/datetime/iso/format.html
		// Index 0 "is" (will be) earliest date found
		daDates[0] = LocalDate.now();
		// Index 1 is latest date found
		daDates[1] = LocalDate.of(1900, 1, 1);
		// currDate is date of the current record
		LocalDate currDate = LocalDate.of(1901, 1, 1);

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
				if (currDate.isBefore(daDates[0]))
					daDates[0] = currDate;
				if (currDate.isAfter(daDates[1]))
					daDates[1] = currDate;
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

		String[] daBeats = beats.toArray(new String[beats.size()]);
		String[] daSectors = sectors.toArray(new String[sectors.size()]);
		String[] daPrecincts = precincts.toArray(new String[precincts.size()]);

		ArrayList<String[]> textVals = new ArrayList<String[]>();
		textVals.add(daBeats);
		textVals.add(daSectors);
		textVals.add(daPrecincts);

		magicTuple allTheThings = new magicTuple(whichCol, textVals, daDates, theRecords);
		return allTheThings;
	}

	/**
	 * getChoices (via submethods) obtains from the user their choices after
	 * relevant options have been obtained from the data
	 * 
	 * @param dateForm
	 *            The formatting String for parsing and display of dates
	 * @param dateLimits
	 *            An LocalDate ArrayList with (0) the earliest permissible date
	 *            and (1) the latest permissible date
	 * @param imgLoc
	 *            Path to the image (map) to be displayed
	 * @param beatOpts
	 *            A String ArrayList of beat options
	 * @param precOpts
	 *            String ArrayList of precinct options
	 */
	public static decisions getChoices(String dateForm, LocalDate[] dateLimits, String imgLoc,
			LinkedHashMap<String,String[]> bpAssoc, String[] beatOpts, String[] precOpts, String[] typeOpts) {

		// Get user's chosen min/max dates
		LocalDate[] daChoice = getDateRange(dateForm, dateLimits);
		// Show user the beat(/sector)/precinct map to aid their areal choosings
		displayMap(imgLoc);
		// Get user's chosen beat and precinct
		String[] bpChoice = getBeatPrecinct(bpAssoc, beatOpts, precOpts);
		// Get user's crime type of focus
		String tyChoice = getTypeOfCrime(typeOpts);

		// Return the decisions/choices of all our submethods
		decisions choices = new decisions(daChoice, bpChoice, tyChoice);
		return choices;
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
	public static LocalDate[] getDateRange(String dateForm, LocalDate[] dateLims) {
		LocalDate[] datesChosen = new LocalDate[2];
		datesChosen[0] = LocalDate.now();
		datesChosen[1] = LocalDate.of(1900, 1, 1);

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateForm);
		boolean validRange = false;

		while (!validRange) {
			JPanel dPanel = new JPanel();
			dPanel.add(new JLabel("Beginning date:"));
			JTextField beginChoice = new JTextField(12);
			dPanel.add(beginChoice);

			dPanel.add(new JLabel("Ending date:"));
			JTextField endChoice = new JTextField(12);
			dPanel.add(endChoice);

			// TODO: Move information from titlebar to inside dialog?
			// Each JTextField at 12 wide just barely fits informative titlebar
			// on MY
			// computer...
			JOptionPane.showConfirmDialog(null,
					dPanel, "Please enter date range of interest: (Strictly formatted and from "
							+ dateLims[0].format(formatter) + " to " + dateLims[1].format(formatter) + ")",
					JOptionPane.DEFAULT_OPTION);

			// Two nested conditions ensure that neither entry is blank and each
			// contains "/". This is hardcoded and it would be better to handle
			// the exception. (B/C infinite other unparseable input possible)
			if (!((beginChoice.getText().equals("")) || (endChoice.getText().equals("")))) {
				if ((beginChoice.getText().contains("/")) && (beginChoice.getText().contains("/"))) {
					datesChosen[0] = LocalDate.parse(beginChoice.getText(), formatter);
					datesChosen[1] = LocalDate.parse(endChoice.getText(), formatter);
					if (!((datesChosen[0].isBefore(dateLims[0]) || (datesChosen[1].isAfter(dateLims[1])))
							|| (datesChosen[0].isAfter(datesChosen[1]))))
						validRange = true;
				}
			} else {
				// If no entry, assume whole range is desired
				datesChosen[0] = dateLims[0];
				datesChosen[1] = dateLims[1];
				validRange = true;
			}
		}
		return datesChosen;
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

	/**
	 * getBeatPrecinct obtains from the user their beat and precinct preferences
	 * via a dialog with dropdowns. It will only accept a beat which is within
	 * the precinct chosen.
	 * 
	 * @param bpAssoc
	 * 
	 * @param theBeats
	 *            A String[] of all the beat possibilities
	 * @param thePrecincts
	 *            A String[] of all the precinct possibilities
	 * @return
	 */
	public static String[] getBeatPrecinct(LinkedHashMap<String, String[]> bpAssoc, String[] theBeats, String[] thePrecincts) {
		JPanel bpPanel = new JPanel();

		JComboBox<?> prec = new JComboBox<Object>(thePrecincts);
		bpPanel.add(new JLabel("Precinct:"));
		bpPanel.add(prec);
		String precChoice;

		JComboBox<?> be = new JComboBox<Object>(theBeats);
		bpPanel.add(new JLabel("Beat:"));
		bpPanel.add(be);
		String beatChoice;

		JOptionPane.showConfirmDialog(null, bpPanel, "Please choose a precinct and a beat within it:",
				JOptionPane.DEFAULT_OPTION);

		String[] bpChoice = { "prec", "be" };
		return bpChoice;
	}

	/**
	 * Ask the user the type of crime they would like to focus on
	 * 
	 * @param types
	 *            Array of Strings containing the general types of crimes
	 *            available
	 */
	public static String getTypeOfCrime(String[] types) {
		JPanel inputType = new JPanel();
		JComboBox<?> type = new JComboBox<Object>(types);
		inputType.add(new JLabel("Type requested:"));
		inputType.add(type);
		JOptionPane.showConfirmDialog(null, inputType, "Enter requested type:", JOptionPane.OK_CANCEL_OPTION);
		// Will aim to make all debug output run from main(), that way there's a
		// builtin check to ensure actual data needing to be returned/passed is
		// System.out.println(types[type.getSelectedIndex()]);
		String typeChoice = "ichose";
		return typeChoice;
	}

	public static String input() {
		String fileName = "";
		JTextField preFilename = new JTextField(10);
		JPanel inputFilename = new JPanel();
		inputFilename.add(new JLabel("Filename: (Case Sensitive)"));
		inputFilename.add(preFilename);
		JOptionPane.showConfirmDialog(null, inputFilename, "Please Enter Filename:", JOptionPane.OK_CANCEL_OPTION);
		fileName = preFilename.getText();
		return fileName;
	}

	public static void main(String[] args) throws IOException {
		// Defaults/constants/hardcodes
		String filename = "datasets/original raw data-DON'T MODIFY.csv";
		String mapLoc = "src/beat-map-2.png";
		String delimiter = ",";
		String dateFormat = "MM/dd/yyyy";
		String[] crimeClasses = {"Personal","Property"};
		final boolean DEBUG = true;

		// Obtain from user which file has the records, which will provide our
		// data and the parameters of our options
		String tempFilename = "";
		tempFilename = input();
		if (tempFilename.length() > 0)
			filename = tempFilename;

		// Obtain from the file our categories
		String[] colDefs = getColDefs(filename, delimiter);
		// Now that we know our header, we can parse our file into structured
		// data
		magicTuple allDat = structFromStream(filename, delimiter, dateFormat, colDefs);
		// With our structured data, we can ask the user their choice among the possibilities
	
		LinkedHashMap<String, String[]> pb = new LinkedHashMap<String, String[]>();
		String[] nw = { "n1", "n2", "n3" };
		String[] sw = { "s1", "s2", "s3", "q5", "q7", "q9" };
		String[] yom = { "b", "a", "d", " ", "a", "s", "s" };
		String[] aa = { "berf" };
		pb.put("yom", yom);
		pb.put("aa", aa);
		pb.put("nw", nw);
		pb.put("sw", sw);

		
		decisions thCh = getChoices(dateFormat, allDat.dateVals, mapLoc,
				pb, allDat.getTextVals().get(0), allDat.getTextVals().get(1), crimeClasses);

		if (DEBUG) {
			src.debug.printFilename(filename);
			src.debug.printDates(allDat.getDateVals(), dateFormat);
			src.debug.printArray(allDat.getTextVals().get(0), ",");
			src.debug.printArray(allDat.getTextVals().get(1), ",");
			src.debug.printArray(allDat.getTextVals().get(2), ",");
			System.out.println();
			src.debug.printArray(colDefs, " \t ");
			System.out.println("=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
			src.debug.printArray(allDat.getTheRecs(), " \t ", true);
		}
	}
}
