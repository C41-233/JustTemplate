package c41.template.resolver;

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
		throw new ResolveException(ErrorString.readBadPropertyOfType(name, value.getClass(), line, column));
	}

	@Override
	public boolean asBoolean() {
		return value != 0;
	}
	
}