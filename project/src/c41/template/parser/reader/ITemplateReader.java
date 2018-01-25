package c41.template.parser.reader;

import java.io.Closeable;

public interface ITemplateReader extends Closeable{

	public int read();
	
	public void push(int ch);
	
	@Override
	public void close();
	
}
