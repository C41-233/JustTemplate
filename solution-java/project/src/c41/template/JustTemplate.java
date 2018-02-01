package c41.template;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import c41.template.parser.ITemplate;
import c41.template.parser.TemplateParser;
import c41.template.resolver.reflect.IResolver;
import c41.template.resolver.reflect.ReflectResolver;

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
	
	public static String render(String input, Object argument) {
		TemplateParser parser = new TemplateParser();
		ITemplate tp = parser.parse(input);
		
		IResolver resolver = new ReflectResolver(argument);
		return tp.render(resolver);
	}

}
