package c41.template.parser;

import c41.template.parser.writer.ITemplateWriter;
import c41.template.resolver.IResolver;

interface IFragment {

	public void renderTo(IResolver resolve, ITemplateWriter writer);
	
}

class TextFragment implements IFragment{

	private final String text;
	
	public TextFragment(String text) {
		this.text = text;
	}

	@Override
	public void renderTo(IResolver resolve, ITemplateWriter writer){
		writer.write(text);
	}
	
}

class ParameterFragment implements IFragment{

	private final char mark;
	private final String name;
	
	public ParameterFragment(char mark, String name) {
		this.mark = mark;
		this.name = name;
	}
	
	@Override
	public void renderTo(IResolver resolve, ITemplateWriter writer) {
		String output = resolve.onVisitParameter(mark, name);
		writer.write(output);
	}

}
