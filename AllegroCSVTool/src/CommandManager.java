import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JTextArea;

public class CommandManager {
	
	public static boolean handleFileCommands(CSVManager csv, String scriptPath,
			JTextArea errorArea) {
		BufferedReader reader = null;

		try {
			reader = new BufferedReader(new FileReader(new File(scriptPath)));
		} catch (FileNotFoundException e) {
			errorArea.append("Script file not found\n");
			return false;
		}

		ArrayList<String> commands = new ArrayList<>();
		String line;

		try {
			while ((line = reader.readLine()) != null) {
				commands.add(line);
			}

			reader.close();
		} catch (IOException e) {
			errorArea.append("Problem occurred while reading script file\n");
			return false;
		}

		return handleCommands(csv,commands,errorArea);
	}
	
	public static boolean handleCommandText(CSVManager csv, String text, JTextArea errorArea) {
		ArrayList<String> commands = new ArrayList<>();
		commands.addAll(Arrays.asList(text.split("\n")));
		return handleCommands(csv,commands,errorArea);
	}
	
	public static boolean handleCommands(CSVManager csv, ArrayList<String> commands, JTextArea errorArea) {
		boolean success = true;
		for (int i = 0; i < commands.size(); i++) {
			try {
				executeCommand(csv, commands.get(i), errorArea);
			} catch (IllegalArgumentException e) {
				errorArea.append("Error in script command at line " + (i + 1)
						+ ": " + e.getMessage() + "\n");
				success = false;
			}
		}
		return success;
	}

	public static void executeCommand(CSVManager csv, String command,
			JTextArea errorArea) {
		String[] parts = command.split(" ");
		int length = parts.length;

		if (length == 0) {
			throw new IllegalArgumentException("no command found");
		}

		if (parts[0].equals("cut")) {
			if (length == 2) {
				int arg = Integer.parseInt(parts[1]) - 1;
				csv.cutCSV(arg);
			} else {
				throw new IllegalArgumentException("error in command: "
						+ command);
			}
		} else if (parts[0].equals("fillColumn")) {
			if (length > 2) {
				int column = ExcelUtility.interpretColumnString(parts[1]) - 1;
				boolean unique = false;

				if (length > 3 && parts[3].equals("unique")) {
					unique = true;
				}

				csv.fillColumn(column, parts[2], unique);
			} else {
				throw new IllegalArgumentException("error in command: "
						+ command);
			}
		} else if (parts[0].equals("copyLine")) {
			if (length > 2) {
				int sourceLine = Integer.parseInt(parts[1]) - 1;
				int from = Integer.parseInt(parts[2]) - 1;
				int to = csv.countLines() - 1;

				if (length > 3) {
					to = Integer.parseInt(parts[3]) - 1;
				}

				csv.copyLine(sourceLine, from, to);
			} else {
				throw new IllegalArgumentException("error in command: "
						+ command);
			}
		} else if (parts[0].equals("fillCell")) {
			if (length > 3) {
				int line = Integer.parseInt(parts[1]) - 1;
				int column = ExcelUtility.interpretColumnString(parts[2]) - 1;
				String pattern = parts[3];
				boolean unique = false;

				if (length > 4 && parts[4].equals("unique")) {
					unique = true;
				}

				String text = csv.fillCell(line, column, pattern, unique);
				errorArea.append("\"" + text + "\" has been set in line "
						+ line + " column " + (column+1) + " (" + ExcelUtility.numberToExcelColumn(column+1) + ")\n");
			} else {
				throw new IllegalArgumentException("error in command: "
						+ command);
			}
		} else if (parts[0].equals("")) {
			return;
		} else {
			throw new IllegalArgumentException("unknown command: " + command);
		}
	}
}
