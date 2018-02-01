package c41.template.parser;

import c41.template.resolver.reflect.IResolver;

public interface ITemplate {

	public String render(IResolver resolver);
	
}
