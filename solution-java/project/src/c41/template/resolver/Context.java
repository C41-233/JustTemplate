package c41.template.resolver;

class Context{
	
	public final IObject current;
	private final Context parent;
	
	public Context(IObject object) {
		this.parent = null;
		this.current = object;
	}
	
	public IObject getParameter(String name, int line, int column) {
		try {
			return current.getKey(name, line, column);
		} catch (ResolveException e) {
			if(parent != null) {
				return parent.getParameter(name, line, column);
			}
			throw e;
		}
	}
	
}