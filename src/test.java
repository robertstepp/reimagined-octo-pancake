
//Java Program by Robert Stepp 01/08/2018
import javax.swing.JOptionPane;

public class test {

	public static void main(String[] args) {
		String selection = JOptionPane
				.showInputDialog("Input a value for selection");

		switch (selection) {
		case "A":
			System.out.println("You selected A.");
			break;
		case "B":
			System.out.println("You selected B.");
			break;
		case "C":
			System.out.println("You selected C.");
			break;
		case "D":
			System.out.println("You selected D.");
			break;
		default:
			System.out.println("Not good with letters, eh?");
		}
	}

}