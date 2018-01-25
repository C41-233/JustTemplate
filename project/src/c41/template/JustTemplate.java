package c41.template;

import java.util.Map;

import c41.template.parser.ITemplate;
import c41.template.parser.TemplateParser;
import c41.template.parser.reader.StringTemplateReader;
import c41.template.parser.writer.StringTemplateWriter;
import c41.template.resolver.IResolver;
import c41.template.resolver.ReflectResolver;

public final class JustTemplate {

	private JustTemplate() {}
	
	public static String render(String template, Object argument) {
		TemplateParser parser = new TemplateParser();
		StringTemplateReader reader = new StringTemplateReader(template);
		ITemplate tp = parser.parse(reader);
		
		IResolver resolver = new ReflectResolver(argument);
		StringTemplateWriter writer = new StringTemplateWriter();
		tp.renderTo(resolver, writer);
		return writer.toString();
	}

	public static String render(String template, Map<String, ?> arguments) {
		TemplateParser parser = new TemplateParser();
		StringTemplateReader reader = new StringTemplateReader(template);
		ITemplate tp = parser.parse(reader);
		
		IResolver resolver = new ReflectResolver(arguments);
		StringTemplateWriter writer = new StringTemplateWriter();
		tp.renderTo(resolver, writer);
		return writer.toString();
	}
	
}
