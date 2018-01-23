package c41.template.internal.util;

public final class Buffer {

	private final StringBuilder sb = new StringBuilder();
	
	public void append(int ch) {
		sb.append((char)ch);
	}
	
	public void clear() {
		sb.delete(0, sb.length());
	}
	
	public int length() {
		return sb.length();
	}
	
	@Override
	public String toString() {
		return sb.toString();
	}
}
