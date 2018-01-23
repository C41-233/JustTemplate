package c41.template.internal.resolve;

import java.util.Iterator;

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
