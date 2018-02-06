package src;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Robert {
	public static void main(String[] args) {
		//
		JTextField filename = new JTextField(10);
		JTextField precinct = new JTextField(10);
		JTextField date = new JTextField(10);
		JPanel inputPanel = new JPanel();
		inputPanel.add(new JLabel("Filename: (Case Sensitive)"));
		inputPanel.add(filename);
		inputPanel.add(Box.createHorizontalStrut(15)); // a spacer
		inputPanel.add(new JLabel("Precinct:"));
		inputPanel.add(precinct);
		inputPanel.add(Box.createHorizontalStrut(15));
		inputPanel.add(new JLabel("Dates requested (MM/DD/YYYY):"));
		inputPanel.add(date);
		int result = JOptionPane.showConfirmDialog(null, inputPanel,
				"Please Enter Filename, Precinct and Dates Requested:",
				JOptionPane.OK_CANCEL_OPTION);
		if (result == JOptionPane.OK_OPTION) {
			System.out.println("Filename value: " + filename.getText());
			System.out.println("Precinct value: " + precinct.getText());
			System.out.println("Date value: " + date.getText());
		}
	}

	// public static String findDates(String[][] array) {
	// String foundDates = "";
	// Calendar today = Calendar.getInstance();
	// DateFormat dateFormat = new SimpleDateFormat("mm-dd-yyyy");
	// String currentDate = dateFormat.format(today.getTime());
	// for (String[] row : array) {
	// if (row.toString().compareTo(currentDate) < 0)
	// ;
	// foundDates = row.toString();
	// }
	// return foundDates;
	// }
}
