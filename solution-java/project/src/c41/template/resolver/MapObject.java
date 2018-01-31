package c41.template.resolver;

import java.util.Map;

class MapObject implements IObject{

	private final Map<?, ?> value;
	
	public MapObject(Map<?, ?> map) {
		this.value = map;
	}
	
	@Override
	public String asString() {
		return value.toString();
	}

	@Override
	public IObject getKey(String name, int line, int column) {
		return ObjectCreator.create(value.get(name));
	}

	@Override
	public boolean asBoolean() {
		return value.size() > 0;
	}

}
