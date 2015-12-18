
public class ExcelUtility {

	public static int excelColumnToNumber(String excelColumn) {
		char[] letters = excelColumn.toCharArray();
		int column = 0;
		for (int i=0; i<letters.length; i++) {
			if (Character.isAlphabetic(letters[i])) {
				column += (Character.getNumericValue(letters[i])-9)*Math.pow(26,letters.length-i-1);
			} else {
				throw new IllegalArgumentException("Error in column index");
			}
		}
		return column;
	}
	
	public static String numberToExcelColumn(int column) {
		if(column < 1) {
			throw new IllegalArgumentException("Column must be bigger than 0");
		}
		
		int degree = 1;
		for (int i = 1;; i++) {
			if((column-1)/Math.pow(26,i) < 1 ) {
				degree = i;
				break;
			}
		}
		
		char[] letters = new char[degree];
		for(int i=0; i<letters.length; i++) {
			letters[i] = Character.toUpperCase(Character.forDigit(column/(int)Math.pow(26,letters.length-i-1)+9,36));
			column %= Math.pow(26,letters.length-i-1);
		}
		
		return new String(letters);
	}

	public static int interpretColumnString(String columnString) {
		int column;
		try {
			column = Integer.parseInt(columnString);
		} catch (NumberFormatException e) {
			column = excelColumnToNumber(columnString);
		}
		return column;
	}
	
}
