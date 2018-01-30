package c41.template;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

import c41.template.parser.ITemplate;
import c41.template.parser.TemplateParser;
import c41.template.resolver.IResolver;
import c41.template.resolver.ReflectResolver;

public final class JustTemplate {

	private JustTemplate() {}
	
	public static String render(File template, Object argument) {
		try {
			byte[] bs = Files.readAllBytes(template.toPath());
			return render(new String(bs, "utf8"), argument);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static String render(File template, Map<String, ?> arguments) {
		try {
			byte[] bs = Files.readAllBytes(template.toPath());
			return render(new String(bs, "utf8"), arguments);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static String render(String input, Object argument) {
		TemplateParser parser = new TemplateParser();
		ITemplate tp = parser.parse(input);
		
		IResolver resolver = new ReflectResolver(argument);
		return tp.render(resolver);
	}

	public static String render(String input, Map<String, ?> arguments) {
		TemplateParser parser = new TemplateParser();
		ITemplate tp = parser.parse(input);
		
		IResolver resolver = new ReflectResolver(arguments);
		return tp.render(resolver);
	}
	
}
