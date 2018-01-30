package c41.template.resolver;

import java.lang.reflect.Field;

import c41.template.internal.util.ErrorString;

interface IObject {

	public String asString();
	
	public IObject getKey(String name, int line, int column);

	public boolean asBoolean();
}


class NullObject implements IObject{

	@Override
	public String asString() {
		return "";
	}

	@Override
	public IObject getKey(String name, int line, int column) {
		throw new ResolveException(ErrorString.readBadPropertyOfType(name, null, line, column));
	}

	@Override
	public boolean asBoolean() {
		return false;
	}
	
	
}

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

class StringObject implements IObject{

	private String value;
	
	public StringObject(String value) {
		this.value = value;
	}
	
	@Override
	public IObject getKey(String name, int line, int column) {
		throw new ResolveException(ErrorString.readBadPropertyOfType(name, value.getClass(), line, column));
	}

	@Override
	public String asString() {
		return value;
	}

	@Override
	public boolean asBoolean() {
		return !value.isEmpty();
	}
	
}

class IntegerObject implements IObject{

	private Integer value;
	
	public IntegerObject(Integer value) {
		this.value = value;
	}
	
	@Override
	public String asString() {
		return value.toString();
	}

	@Override
	public IObject getKey(String name, int line, int column) {
		throw new ResolveException(ErrorString.readBadPropertyOfType(name, value.getClass(), line, column));
	}

	@Override
	public boolean asBoolean() {
		return value != 0;
	}
	
}

class DoubleObject implements IObject{

	private Double value;
	
	public DoubleObject(Double value) {
		this.value = value;
	}
	
	@Override
	public String asString() {
		return value.toString();
	}

	@Override
	public IObject getKey(String name, int line, int column) {
		throw new ResolveException(ErrorString.readBadPropertyOfType(name, value.getClass(), line, column));
	}

	@Override
	public boolean asBoolean() {
		return value != 0;
	}
	
}

class BooleanObject implements IObject{

	private final Boolean value;
	
	public BooleanObject(Boolean value) {
		this.value = value;
	}
	
	@Override
	public String asString() {
		return value.toString();
	}

	@Override
	public IObject getKey(String name, int line, int column) {
		throw new ResolveException(ErrorString.readBadPropertyOfType(name, value.getClass(), line, column));
	}

	@Override
	public boolean asBoolean() {
		return value;
	}
	
}
