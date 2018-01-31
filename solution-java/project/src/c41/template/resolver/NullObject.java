package c41.template.resolver;

import c41.template.internal.util.ErrorString;

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