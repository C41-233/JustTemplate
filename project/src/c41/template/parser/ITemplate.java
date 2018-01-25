package c41.template.parser;

import c41.template.parser.writer.ITemplateWriter;
import c41.template.resolver.IResolver;

public interface ITemplate {

	public void renderTo(IResolver resolver, ITemplateWriter writer);
	
}
