package c41.template.internal.fragment;

import java.io.IOException;
import java.io.Writer;

import c41.template.parser.IResolve;

public interface IFragment {

	public void renderTo(IResolve resolve, Writer writer) throws IOException;
	
}
