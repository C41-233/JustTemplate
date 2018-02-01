package c41.template.resolver.reflect;

import java.util.Map;

class RootMapContext implements IContext {

	private final IObject current;
	private final Map<String, ?> map;
	
	public RootMapContext(Map<String, ?> map) {
		this.current = ObjectCreator.create(map);
		this.map = map;
	}
	
	@Override
	public IObject getParameter(String name, int line, int column) {
		if(map.containsKey(name)) {
			return ObjectCreator.create(map.get(name));
		}
		return current.getKey(name, line, column);
	}

	@Override
	public IObject getContextObject() {
		return current;
	}

}
