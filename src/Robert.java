package src;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Robert {
	public static void main(String[] args) {
		// http://www.java2s.com/Tutorials/Java/Swing_How_to/JOptionPane/Create_Multiple_input_in_JOptionPane_showInputDialog.htm
		JTextField filename = new JTextField(10);
		JTextField precinct = new JTextField(10);
		JPanel inputPanel = new JPanel();
		inputPanel.add(new JLabel("Filename: (Case Sensitive)"));
		inputPanel.add(filename);
		inputPanel.add(Box.createHorizontalStrut(15)); // a spacer
		inputPanel.add(new JLabel("Precinct:"));
		inputPanel.add(precinct);
		int result = JOptionPane.showConfirmDialog(null, inputPanel,
				"Please Enter X and Y Values", JOptionPane.OK_CANCEL_OPTION);
		if (result == JOptionPane.OK_OPTION) {
			System.out.println("x value: " + filename.getText());
			System.out.println("y value: " + precinct.getText());
		}
	}
}
