package c41.template.parser;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

import c41.template.internal.resolve.ReflectResolve;

public interface ITemplate {

	public void renderTo(IResolve resolve, Writer writer) throws IOException;
	
	public default String render(Map<String, ?> args) {
		if(args == null) {
			return render((Object)null);
		}
		try {
			StringWriter writer = new StringWriter();
			ReflectResolve resolve = new ReflectResolve(args);
			renderTo(resolve, writer);
			return writer.toString();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public default String render(Object args) {
		try {
			StringWriter writer = new StringWriter();
			ReflectResolve resolve = new ReflectResolve(args);
			renderTo(resolve, writer);
			return writer.toString();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
}
