package c41.template.resolver.reflect;

import java.util.Iterator;

import c41.template.TemplateException;
import c41.template.internal.util.ErrorString;

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
		throw new TemplateException(ErrorString.readBadPropertyOfType(name, value.getClass(), line, column));
	}

	@Override
	public boolean asBoolean() {
		return value;
	}

	@Override
	public Iterator<Object> asIterator() {
		// TODO Auto-generated method stub
		return null;
	}
	
}