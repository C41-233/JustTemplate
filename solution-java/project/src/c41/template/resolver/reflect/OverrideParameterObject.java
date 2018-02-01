package c41.template.resolver.reflect;

import java.util.Objects;

class OverrideParameterObject implements IContext {

	private final IContext parent;
	private final String name;
	private final IObject object;
	
	public OverrideParameterObject(String name, Object object, IContext parent) {
		this.name = name;
		this.object = ObjectCreator.create(object);
		this.parent = parent;
	}
	
	@Override
	public IObject getParameter(String name, int line, int column) {
		if(Objects.equals(name, this.name)) {
			return object;
		}
		return parent.getParameter(name, line, column);
	}

	@Override
	public IObject getContextObject() {
		return parent.getContextObject();
	}

}
