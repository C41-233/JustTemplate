package c41.template.parser.reader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import c41.template.internal.util.FastStack;

public class FileTemplateReader implements ITemplateReader {

	private final InputStreamReader reader;
	private final FastStack stack = new FastStack();
	
	public FileTemplateReader(File file) {
		this(file, "utf8");
	}

	public FileTemplateReader(File file, String charset) {
		try {
			reader = new InputStreamReader(new FileInputStream(file), charset);
		} catch (UnsupportedEncodingException | FileNotFoundException e) {
			throw new ReadException(e);
		}
	}
	
	@Override
	public int read() {
		if(stack.size() > 0) {
			return stack.pop();
		}
		try {
			return reader.read();
		} catch (IOException e) {
			throw new ReadException(e);
		}
	}

	@Override
	public void push(int ch) {
		stack.push(ch);
	}

	@Override
	public void close() {
		try {
			reader.close();
		} catch (IOException e) {
		}
	}

}
