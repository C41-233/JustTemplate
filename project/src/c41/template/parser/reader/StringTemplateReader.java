package c41.template.parser.reader;

import java.io.EOFException;

import c41.template.internal.util.FastStack;

public class StringTemplateReader implements ITemplateReader {

	private final String string;
	private final int length;
	private int i = 0;
	private final FastStack cache = new FastStack();
	
	public StringTemplateReader(String string) {
		this.string = string;
		this.length = string.length();
	}
	
	@Override
	public int read() {
		if(cache.size() > 0) {
			return cache.pop();
		}
		if(i == length) {
			i++;
			return -1;
		}
		if(i > length) {
			throw new ReadException(new EOFException());
		}
		return string.charAt(i++);
	}

	@Override
	public void push(int ch) {
		if(i > 0 && length > 0 && i - 1 < length && string.charAt(i-1) == ch) {
			i--;
			return;
		}
		cache.push(ch);
	}

}
