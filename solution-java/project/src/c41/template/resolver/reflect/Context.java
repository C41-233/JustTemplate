package c41.template.resolver.reflect;

import c41.template.resolver.ResolveException;

class Context{
	
	public final IObject current;
	private final Context parent;

	private final String name;
	private final IObject value;
	
	public Context(IObject object) {
		this.parent = null;
		this.current = object;
		this.name = null;
		this.value = null;
	}
	
	public Context(IObject object, Context parent) {
		this.parent = parent;
		this.current = object;
		this.name = null;
		this.value = null;
	}

	public Context(String name, IObject value, Context parent) {
		this.parent = parent;
		this.current = null;
		this.name = name;
		this.value = value;
	}

	public IObject getParameter(String name, int line, int column) {
		if(name.equals(this.name)) {
			return value;
		}
		
		if(current == null) {
			return parent.getParameter(name, line, column);
		}
		
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