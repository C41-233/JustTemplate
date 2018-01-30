package c41.template.internal.util;

public class InputReader {

	public static final int EOF = -1;
	
	private final String input;
	private final FastStack linePos = new FastStack();
	
	private int i = -1;
	
	private int line = 1;
	private int col = 0;
	
	public InputReader(String input) {
		this.input = input;
	}
	
	public int getLine() {
		return line;
	}
	
	public int getColumn() {
		return col;
	}
	
	public int read() {
		if(i+1 == input.length()) {
			i++;
			return -1;
		}
		int val = input.charAt(i+1);
		i++;
		if(val == '\n') {
			line++;
			linePos.push(col);
			col = 0;
		}
		else {
			col++;
		}
		return val;
	}
	
	public void pushBack() {
		if(i < input.length() && input.charAt(i) == '\n') {
			line--;
			col = linePos.pop();
		}
		else {
			col--;
		}
		i--;
	}
	
}
