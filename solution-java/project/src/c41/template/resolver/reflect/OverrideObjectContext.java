package c41.template.resolver.reflect;

import c41.template.TemplateException;

class OverrideObjectContext implements IContext {

	private final IObject current;
	private final IContext parent;
	
	public OverrideObjectContext(Object object, IContext parent) {
		this.current = ObjectCreator.create(object);
		this.parent = parent;
	}
	
	@Override
	public IObject getParameter(String name, int line, int column) {
		try{
			return current.getKey(name, line, column);
		}
		catch (TemplateException e) {
			return parent.getParameter(name, line, column);
		}
	}

	@Override
	public IObject getContextObject() {
		return current;
	}

}
