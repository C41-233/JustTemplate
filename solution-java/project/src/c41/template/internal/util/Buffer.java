package c41.template.internal.util;

public final class Buffer {

	private final StringBuilder sb = new StringBuilder();
	
	public void append(int ch) {
		sb.append((char)ch);
	}
	
	public int length() {
		return sb.length();
	}
	
	public String take() {
		String string = sb.toString();
		sb.delete(0, sb.length());
		return string;
	}
	
	@Override
	public String toString() {
		return sb.toString();
	}
	
}
