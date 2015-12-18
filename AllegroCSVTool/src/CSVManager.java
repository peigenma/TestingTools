import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class CSVManager {
	private String[][] table;
	private String filename;

	public CSVManager(String filename, String separator) throws IOException, IndexOutOfBoundsException {
		if (!filename.endsWith(".csv")) {
			throw new IllegalArgumentException(
					"the given input is not a *.csv file");
		}

		table = loadCSV(filename, separator);
		this.filename = filename;
	}

	public void fillColumn(int column, String pattern, boolean unique) {
		if (column > table[0].length - 1 || column < 0) {
			throw new IllegalArgumentException("column index \"" + (column + 1)
					+ "\"(" + ExcelUtility.numberToExcelColumn(column + 1)
					+ ") is not a valid column");
		}

		StringGenerator generator = new StringGenerator(pattern, unique);
		String text;

		for (int i = 1; i < table.length; i++) {
			text = generator.generateString();

			table[i][column] = text;
		}
	}

	public String[][] loadCSV(String filename, String separator)
			throws IOException, IndexOutOfBoundsException {
		ArrayList<String> lines = new ArrayList<>();

		BufferedReader reader = new BufferedReader(new FileReader(new File(
				filename)));
		String line;

		while ((line = reader.readLine()) != null) {
			lines.add(line);
		}

		reader.close();

		String[] firstLine = lines.get(0).split(separator);
		String[] fields;

		String[][] table = new String[lines.size()][firstLine.length];
		table[0] = firstLine;

		for (int i = 1; i < lines.size(); i++) {
			fields = lines.get(i).split(separator);

			for (int j = 0; j < table[0].length; j++) {
				if (j < fields.length) {
					table[i][j] = fields[j];
				} else {
					table[i][j] = "";
				}
			}
		}

		return table;
	}

	public void saveCSV() throws IOException {
		StringBuilder buffer = new StringBuilder();
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(
				filename)));

		for (int i = 0; i < table.length; i++) {
			for (int j = 0; j < table[0].length - 1; j++) {
				buffer.append(table[i][j]);
				buffer.append(";");
			}

			buffer.append(table[i][table[0].length - 1]);
			buffer.append("\n");

			writer.append(buffer.toString());
			buffer.setLength(0);
		}

		writer.close();
	}

	public void cutCSV(int lines) {
		if (lines < 0) {
			throw new IllegalArgumentException("a negative number is not valid");
		}
		table = Arrays.copyOf(table, lines + 1);
	}

	public void enlargeCSV(int lines) {
		if (table.length < lines) {
			String[] line = new String[table[0].length];
			Arrays.fill(line, "");

			String[][] biggerTable = new String[lines][table[0].length];

			for (int i = 0; i < lines; i++) {
				if (i < table.length) {
					biggerTable[i] = table[i];
				} else {
					biggerTable[i] = Arrays.copyOf(line, line.length);
				}
			}

			table = biggerTable;
		}
	}

	public void copyLine(int line, int from, int to) {
		if (table.length - 1 < line) {
			throw new IllegalArgumentException("the chosen line index \""
					+ line + "\" in copyLine is bigger than the file size");
		} else if (line < 1 || from < 1 || to < 1 || from > to) {
			throw new IllegalArgumentException(
					"there is an error in the values of line, from or to");
		} else if (table.length - 1 < to) {
			enlargeCSV(to + 1);
		}

		for (int i = from; i < to + 1; i++) {
			table[i] = Arrays.copyOf(table[line], table[0].length);
		}
	}

	public int countLines() {
		return table.length;
	}

	public String fillCell(int line, int column, String pattern, boolean unique) {
		if (line > table.length - 1 || column > table[0].length - 1 || line < 0
				|| column < 0) {
			throw new IllegalArgumentException(
					"line and/or column has an invalid value");
		}

		StringGenerator generator = new StringGenerator(pattern, unique);
		String text = generator.generateString();

		if (unique) {
			while (columnContainsValue(column, text)) {
				text = generator.generateString();
			}
		}

		table[line][column] = text;
		return text;
	}

	public boolean columnContainsValue(int column, String value) {
		for (int j = 0; j < table.length; j++) {
			if (table[j][column].equals(value)) {
				return true;
			}
		}

		return false;
	}
}
