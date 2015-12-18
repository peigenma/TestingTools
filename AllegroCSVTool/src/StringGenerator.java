import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class StringGenerator {
	private String pattern;
	private boolean unique;
	private ArrayList<String> generatedStrings;
	private ArrayList<Integer> indexes;

	public StringGenerator(String pattern, boolean unique) {
		this.pattern = pattern;
		this.unique = unique;

		if (unique) {
			generatedStrings = new ArrayList<String>();
		}

		indexes = new ArrayList<>();

		for (int i = 0; i < pattern.length(); i++) {
			i = pattern.indexOf("?", i);

			if (i == -1) {
				break;
			} else {
				indexes.add(i);
			}
		}

		if (unique && indexes.isEmpty()) {
			throw new IllegalArgumentException();
		}
	}

	public String generateString() {
		Random rand = new Random();
		char[] text = pattern.toCharArray();

		for (int i = 0; i < indexes.size(); i++) {
			text[indexes.get(i)] = (char) (rand.nextInt(10) + 48);
		}

		String generatedString = new String(text);

		if (unique && generatedStrings.contains(generatedString)) {
			if (getPossibleUniqueCount() > generatedStrings.size()) {
				generatedString = generateString();
			} else {
				throw new IllegalArgumentException(
						"There are too many lines or not enough question marks to generate unique values\n");
			}
		}
		
		if (generatedString.contains("today")) {
			DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
			Date date = new Date();
			generatedString = generatedString.replace("today",dateFormat.format(date));
		}

		return generatedString;
	}

	public long getPossibleUniqueCount() {
		return Math.round(Math.pow(10, indexes.size()));
	}
}
