package c41.template.internal.engine;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;

import c41.template.internal.fragment.IFragment;
import c41.template.internal.fragment.ParameterFragment;
import c41.template.internal.fragment.TextFragment;
import c41.template.parser.IResolve;
import c41.template.parser.ITemplate;

public class Template implements ITemplate{

	private final ArrayList<IFragment> fragments = new ArrayList<>();
	
	public void addTextFragment(String string) {
		this.fragments.add(new TextFragment(string));
	}

	public void addParameter(char mark, String name) {
		this.fragments.add(new ParameterFragment(mark, name));
	}
	
	@Override
	public void renderTo(IResolve resolve, Writer writer) throws IOException {
		for (IFragment fragment : fragments) {
			fragment.renderTo(resolve, writer);
		}
	}

}
