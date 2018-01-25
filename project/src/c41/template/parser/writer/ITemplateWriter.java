package c41.template.parser.writer;

import java.io.Closeable;

public interface ITemplateWriter extends Closeable{

	public void write(String string);
	
	@Override
	public void close();
	
}
