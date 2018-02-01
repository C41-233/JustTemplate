package c41.template;

public class TemplateException extends RuntimeException {

	public TemplateException() {
	}
	
	public TemplateException(String msg) {
		super(msg);
	}

	public TemplateException(String format, Object...args) {
		super(String.format(format, args));
	}

	public TemplateException(Exception e) {
		super(e);
	}
	
}
