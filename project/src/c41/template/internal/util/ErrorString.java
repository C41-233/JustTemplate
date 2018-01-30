package c41.template.internal.util;

public final class ErrorString {

	private ErrorString() {}
	
	public static String unexpectedEndOfParameter(int line, int column) {
		return String.format("unexpected end of parameter in line %d column %d", line, column);
	}
	
	public static String unrecognizedLogicWord(String word, int line, int column) {
		return String.format("unrecognized logic word '%s' in line %d column %d", word, line, column);
	}
	
	public static String emptyLogicWord(int line, int column) {
		return String.format("empty logic block in line %d column %d", line, column);
	}
	
	public static String unexpectedCharacterAfter(int ch, String target, int line, int column) {
		return String.format("unexpected character '%c' after '%s' in line %d column %d", ch, target, line, column);
	}
	
	public static String unmatchedElse(int line, int column) {
		return String.format("else without match if or elseif in line %d column %d", line, column);
	}
	
	public static String unmatchedEndIf(int line, int column) {
		return String.format("endif without match if in line %d column %d", line, column);
	}
	
	public static String unmatchedElseIf(int line, int column) {
		return String.format("elseif without match if in line %d column %d", line, column);
	}

	public static String readBadPropertyOfType(String name, Class<?> cl, int line, int column) {
		if(cl != null) {
			return String.format("read property '%s' of class %s in line %d column %d", name, cl.getName(), line, column);
		}
		else {
			return String.format("read property '%s' of null in line %d column %d", name, line, column);
		}
	}
	
}
