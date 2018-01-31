package c41.template.resolver;

import c41.template.internal.util.ErrorString;

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