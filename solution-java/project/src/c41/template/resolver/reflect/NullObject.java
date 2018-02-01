package c41.template.resolver.reflect;

import java.util.Iterator;

import c41.template.internal.util.ErrorString;
import c41.template.resolver.ResolveException;

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

	@Override
	public Iterator<Object> asIterator() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}