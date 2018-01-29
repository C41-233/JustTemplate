package c41.template.internal.util;

public class InputReader {

	public static final int EOF = -1;
	
	private final String input;
	
	private int i = -1;
	
	public InputReader(String input) {
		this.input = input;
	}
	
	public int read() {
		if(i+1 == input.length()) {
			i++;
			return -1;
		}
		int val = input.charAt(i+1);
		i++;
		return val;
	}
	
	public void pushBack() {
		i--;
	}
	
}
