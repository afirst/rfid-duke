import java.util.*;
import java.io.*;

public class RandomSelectLines {
	/**This is the program I used to generate control.txt (about 70% of 
	 * database.txt and test.txt (30%), note that
	 * you need to delete those two files if you want a new ones. 
	 */
	public static void main(String arg[]) {
		File file = new File("database.txt");
		Scanner s = null;
		PrintStream control = null;
		PrintStream test = null;
		try {
			s = new Scanner(file);
		    control = new PrintStream(new FileOutputStream ("control.txt"));
		    test = new PrintStream(new FileOutputStream("test.txt"));
		} catch(FileNotFoundException e) {
			System.out.println("File not found!");
			System.exit(0);
		}
		while (s.hasNextLine()) {
			String input = s.nextLine();
			Random r = new Random();
			Double x = r.nextDouble();
			if (x > .70) {
				control.println (input);
			} else {
				test.println(input);
			}
		}
			
	}
}
