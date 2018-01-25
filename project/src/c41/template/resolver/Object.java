package c41.template.resolver;

import java.lang.reflect.Field;
import java.util.Iterator;

interface IObject {

	public boolean toBoolean();
	
	@Override
	public String toString();
	
	public Iterator<Object> toIterator();
	
	public IObject getKey(String name);
}


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

class NumberObject implements IObject{

	private Number value;
	
	public NumberObject(Number value) {
		this.value = value;
	}
	
	@Override
	public boolean toBoolean() {
		return value.intValue() != 0;
	}

	@Override
	public Iterator<Object> toIterator() {
		throw new ResolveException();
	}

	@Override
	public String toString() {
		return value.toString();
	}

	@Override
	public IObject getKey(String name) {
		throw new ResolveException();
	}
	
}
