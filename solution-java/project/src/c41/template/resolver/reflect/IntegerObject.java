package c41.template.resolver.reflect;

import java.util.Iterator;

import c41.template.TemplateException;
import c41.template.internal.util.ErrorString;

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
		throw new TemplateException(ErrorString.readBadPropertyOfType(name, value.getClass(), line, column));
	}

	@Override
	public boolean asBoolean() {
		return value != 0;
	}

	@Override
	public Iterator<Object> asIterator() {
		// TODO Auto-generated method stub
		return null;
	}
	
}