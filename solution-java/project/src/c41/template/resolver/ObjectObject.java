package c41.template.resolver;

import java.lang.reflect.Field;

class ObjectObject implements IObject{

	private final Object object;
	
	public ObjectObject(Object object) {
		this.object = object;
	}
	
	@Override
	public IObject getKey(String name, int line, int column) {
		try {
			Field field = object.getClass().getDeclaredField(name);
			field.setAccessible(true);
			return ObjectCreator.create(field.get(object));
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			throw new ResolveException(e);
		}
	}

	@Override
	public String asString() {
		return object.toString();
	}

	@Override
	public boolean asBoolean() {
		return true;
	}
	
}