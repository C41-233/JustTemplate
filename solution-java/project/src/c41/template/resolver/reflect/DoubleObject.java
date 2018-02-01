package c41.template.resolver.reflect;

import java.util.Iterator;

import c41.template.internal.util.ErrorString;
import c41.template.resolver.ResolveException;

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

	@Override
	public Iterator<Object> asIterator() {
		// TODO Auto-generated method stub
		return null;
	}
	
}