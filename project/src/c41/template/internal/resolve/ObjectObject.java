package c41.template.internal.resolve;

import java.lang.reflect.Field;
import java.util.Iterator;

class ObjectObject implements IObject{

	private final Object object;
	
	public ObjectObject(Object object) {
		this.object = object;
	}
	
	@Override
	public boolean toBoolean() {
		return true;
	}

	@Override
	public Iterator<Object> toIterator() {
		throw new ResolveException();
	}

	@Override
	public IObject getKey(String name) {
		try {
			Field field = object.getClass().getField(name);
			return ObjectCreator.create(field.get(object));
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			throw new ResolveException(e);
		}
	}

	@Override
	public String toString() {
		return object.toString();
	}
	
}
