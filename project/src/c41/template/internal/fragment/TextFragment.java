package c41.template.internal.fragment;

import java.io.IOException;
import java.io.Writer;

import c41.template.parser.IResolve;

public class TextFragment implements IFragment{

	private final String text;
	
	public TextFragment(String text) {
		this.text = text;
	}

	@Override
	public void renderTo(IResolve resolve, Writer writer) throws IOException {
		writer.write(text);
	}
	
}
