package c41.template.internal.resolve;

public class ResolveException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ResolveException() {
		
	}
	
	public ResolveException(String msg) {
		super(msg);
	}

	public ResolveException(Exception e) {
		super(e);
	}
	
}
