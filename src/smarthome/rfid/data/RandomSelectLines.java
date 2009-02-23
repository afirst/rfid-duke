package smarthome.rfid.data;
import java.util.*;
import java.io.*;

public class RandomSelectLines {
	/**This is the program I used to generate control.txt (about 70% of 
	 * database.txt and test.txt (30%), note that
	 * you need to delete those two files if you want a new ones. 
	 */
	public void main(int many_times) {
		File file = new File("data_files/database.txt");
		Scanner s = null;
		PrintStream control = null;
		PrintStream test = null;
		for (int i=1; i<many_times+1; i++) {
			try {
				s = new Scanner(file);
				boolean create_file = (new File("data_files\\test" + i + "\\")).mkdir();
			    control = new PrintStream(new FileOutputStream ("data_files\\test" + i + "\\control.txt"));
			    test = new PrintStream(new FileOutputStream("data_files\\test" + i + "\\test.txt"));
			} catch(FileNotFoundException e) {
				System.out.println("File not found!");
				System.exit(0);
			}
			while (s.hasNextLine()) {
				String input = s.nextLine();
				Random r = new Random();
				Double x = r.nextDouble();
				if (x > .70) {
					test.println (input);
				} else {
					control.println(input);
				}
			}
		}
	}
	
	public static void main(String arg[]) {
		RandomSelectLines r = new RandomSelectLines();
		r.main(10);
	}
}
