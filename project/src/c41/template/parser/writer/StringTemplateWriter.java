package c41.template.parser.writer;

public class StringTemplateWriter implements ITemplateWriter{

	private final StringBuilder sb = new StringBuilder();
	
	@Override
	public void write(String string) {
		sb.append(string);
	}

	@Override
	public String toString() {
		return sb.toString();
	}

	@Override
	public void close() {
	}
	
}
