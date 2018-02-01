package c41.template.resolver.reflect;

import java.util.Iterator;

import c41.template.internal.util.ErrorString;
import c41.template.resolver.ResolveException;

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

	@Override
	public Iterator<Object> asIterator() {
		return new Iterator<Object>() {

			private int index = 0;
			
			@Override
			public boolean hasNext() {
				return index < value.length();
			}

			@Override
			public Object next() {
				return value.charAt(index++);
			}
		};
	}
	
}