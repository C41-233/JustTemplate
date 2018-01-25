package c41.template.parser;

import java.util.ArrayList;

import c41.template.parser.writer.ITemplateWriter;
import c41.template.resolver.IResolver;

class Template implements ITemplate{

	private final ArrayList<IFragment> fragments = new ArrayList<>();
	
	public void addTextFragment(String string) {
		this.fragments.add(new TextFragment(string));
	}

	public void addParameter(char mark, String name) {
		this.fragments.add(new ParameterFragment(mark, name));
	}
	
	public void end() {
		fragments.trimToSize();
	}
	
	@Override
	public void renderTo(IResolver resolve, ITemplateWriter writer){
		for (IFragment fragment : fragments) {
			fragment.renderTo(resolve, writer);
		}
	}

}
