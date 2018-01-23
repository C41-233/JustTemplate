package c41.template.internal.fragment;

import java.io.IOException;
import java.io.Writer;

import c41.template.parser.IResolve;

public class ParameterFragment implements IFragment{

	private final char mark;
	private final String name;
	
	public ParameterFragment(char mark, String name) {
		this.mark = mark;
		this.name = name;
	}
	
	@Override
	public void renderTo(IResolve resolve, Writer writer) throws IOException {
		String output = resolve.onVisitParameter(mark, name);
		writer.write(output);
	}

}
