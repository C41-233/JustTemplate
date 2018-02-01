package c41.template;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

import c41.template.parser.ITemplate;
import c41.template.parser.TemplateParser;
import c41.template.resolver.reflect.IResolver;
import c41.template.resolver.reflect.ReflectResolver;

public final class JustTemplate {

	private JustTemplate() {}
	
	public static String render(File template, Object object) {
		try {
			byte[] bs = Files.readAllBytes(template.toPath());
			return render(new String(bs, "utf8"), object);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static String render(File template, Map<String, ?> parameters) {
		try {
			byte[] bs = Files.readAllBytes(template.toPath());
			return render(new String(bs, "utf8"), parameters);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static String render(String input, Object object) {
		TemplateParser parser = new TemplateParser();
		ITemplate tp = parser.parse(input);
		
		IResolver resolver = new ReflectResolver(object);
		return tp.render(resolver);
	}

	public static String render(String input, Map<String, ?> parameters) {
		TemplateParser parser = new TemplateParser();
		ITemplate tp = parser.parse(input);
		
		IResolver resolver = new ReflectResolver(parameters);
		return tp.render(resolver);
	}

}
