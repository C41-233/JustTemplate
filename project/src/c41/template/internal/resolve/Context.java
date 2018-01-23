package c41.template.internal.resolve;

import java.util.HashMap;

class Context{
	
	public final IObject current;
	
	private HashMap<String, IObject> parameters = new HashMap<>();
	
	public Context(IObject current) {
		this.current = current;
	}
	
	public IObject getParameter(String name) {
		IObject obj = parameters.get(name);
		if(obj == null) {
			throw new ResolveException();
		}
		return obj;
	}
	
	public void addParameter(String name, IObject object) {
		parameters.put(name, object);
	}
}