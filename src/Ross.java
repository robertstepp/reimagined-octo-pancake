package src;

import java.awt.Image;
import java.io.BufferedReader;
//import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

//import java.util.Arrays;
//import java.util.List;

public class Ross {
	public static String[] input() {
		String[] types = { "Personal", "Property" };
		String[] beats = { "B1", "B2", "B3", "C1", "C2", "C3", "D1", "D2", "D3",
				"E1", "E2", "E3", "F1", "F2", "F3", "G1", "G2", "G3", "J1",
				"J2", "J3", "K1", "K2", "K3", "L1", "L2", "L3", "M1", "M2",
				"M3", "N1", "N2", "N3", "O1", "O2", "O3", "Q1", "Q2", "Q3",
				"R1", "R2", "R3", "S1", "S2", "S3", "U1", "U2", "U3", "W1",
				"W2", "W3" };
		String[] precincts = { "N", "W", "E", "SE", "SW" };
		String[] input = new String[5]; // Filename, Precinct, Beat, Date Range,
										// Type (Personal/Property)
		/*
		 * Displays the Beat Map resized down.
		 **/
		JFrame frame = new JFrame();
		ImageIcon icon = new ImageIcon(new ImageIcon("src/beat-map-2.png")
				.getImage().getScaledInstance(647, 1000, Image.SCALE_SMOOTH));
		JLabel label = new JLabel(icon);
		frame.add(label);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
		JTextField filename = new JTextField(10);
		JComboBox<?> precinct = new JComboBox<Object>(precincts);
		JTextField startDate = new JTextField(10);
		JTextField endDate = new JTextField(10);
		JComboBox<?> beat = new JComboBox<Object>(beats);
		JComboBox<?> type = new JComboBox<Object>(types);
		JPanel inputFilename = new JPanel();
		JPanel inputDate = new JPanel();
		JPanel inputArea = new JPanel();
		JPanel inputType = new JPanel();
		inputFilename.add(new JLabel("Filename: (Case Sensitive)"));
		inputFilename.add(filename);
		inputDate.add(new JLabel("Start Date:"));
		inputDate.add(startDate);
		inputDate.add(Box.createHorizontalStrut(15));
		inputDate.add(new JLabel("End Date:"));
		inputDate.add(endDate);
		inputArea.add(new JLabel("Precinct:"));
		inputArea.add(precinct);
		inputDate.add(Box.createHorizontalStrut(15));
		inputArea.add(new JLabel("Beat:"));
		inputArea.add(beat);
		inputType.add(new JLabel("Type requested:"));
		inputType.add(type);
		JOptionPane.showConfirmDialog(null, inputFilename,
				"Please Enter Filename:",
				JOptionPane.OK_CANCEL_OPTION);
		JOptionPane.showConfirmDialog(null, inputDate, "Enter requested dates:",
				JOptionPane.OK_CANCEL_OPTION);
		JOptionPane.showConfirmDialog(null, inputArea, "Enter requested area:",
				JOptionPane.OK_CANCEL_OPTION);
		JOptionPane.showConfirmDialog(null, inputType, "Enter requested type:",
				JOptionPane.OK_CANCEL_OPTION);
		/**
		 * The following is how to return the data the user inputs I was testing
		 * using an output to see the returned data.
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
	public static void printArray(ArrayList<String[]> outtie, String delim,
			boolean special) {
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
	public static String[] getColDefs(String file, String delim)
			throws IOException {
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
		LineNumberReader lineReader = new LineNumberReader(
				new FileReader(file));
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

	public static ArrayList<String[]> recordsFromText(String file, String del,
			int cols) throws IOException {
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
		input();

		String[] columns = getColDefs(filename, delimiter);
		ArrayList<String[]> rows = new ArrayList<String[]>(
				recordsFromText(filename, delimiter, columns.length));

		if (debug) {
			printArray(rows, " \t ", true);
			System.out.println("---");
			printArray(columns, " \t ");
			System.out.println("---");
		}
	}
}
