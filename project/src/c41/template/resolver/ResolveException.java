package c41.template.resolver;

public class ResolveException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ResolveException() {
		
	}
	
	public ResolveException(String msg) {
		super(msg);
	}

	public ResolveException(String format, Object...args) {
		super(String.format(format, args));
	}

	public ResolveException(Exception e) {
		super(e);
	}
	
}
