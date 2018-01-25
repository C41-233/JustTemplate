package c41.template;

import java.io.File;
import java.util.Map;

import c41.template.parser.ITemplate;
import c41.template.parser.TemplateParser;
import c41.template.parser.reader.FileTemplateReader;
import c41.template.parser.reader.ITemplateReader;
import c41.template.parser.reader.StringTemplateReader;
import c41.template.parser.writer.StringTemplateWriter;
import c41.template.resolver.IResolver;
import c41.template.resolver.ReflectResolver;

public final class JustTemplate {

	private JustTemplate() {}
	
	public static String render(String template, Object argument) {
		try(
			StringTemplateReader reader = new StringTemplateReader(template);
		){
			return render(reader, argument);
		}
	}

	public static String render(String template, Map<String, ?> arguments) {
		try(
			StringTemplateReader reader = new StringTemplateReader(template);
		){
			return render(reader, arguments);
		}
	}

	public static String render(File template, Object argument) {
		try(
			FileTemplateReader reader = new FileTemplateReader(template);
		){
			return render(reader, argument);
		}
	}
	
	public static String render(File template, Map<String, ?> arguments) {
		try(
			FileTemplateReader reader = new FileTemplateReader(template);
		){
			return render(reader, arguments);
		}
	}

	public static String render(ITemplateReader reader, Object argument) {
		try(
			StringTemplateWriter writer = new StringTemplateWriter()
		){
			TemplateParser parser = new TemplateParser();
			ITemplate tp = parser.parse(reader);
			
			IResolver resolver = new ReflectResolver(argument);
			tp.renderTo(resolver, writer);
			return writer.toString();
		}
	}

	public static String render(ITemplateReader reader, Map<String, ?> arguments) {
		try(
			StringTemplateWriter writer = new StringTemplateWriter()
		){
			TemplateParser parser = new TemplateParser();
			ITemplate tp = parser.parse(reader);
			
			IResolver resolver = new ReflectResolver(arguments);
			tp.renderTo(resolver, writer);
			return writer.toString();
		}
	}
	
}
