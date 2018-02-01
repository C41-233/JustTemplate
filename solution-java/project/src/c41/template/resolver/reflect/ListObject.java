package c41.template.resolver.reflect;

import java.util.Iterator;
import java.util.List;

class ListObject implements IObject {

	private final List<?> list;
	
	public ListObject(List<?> list) {
		this.list = list;
	}
	
	@Override
	public String asString() {
		return list.toString();
	}

	@Override
	public IObject getKey(String name, int line, int column) {
		return null;
	}

	@Override
	public boolean asBoolean() {
		return list.size() > 0;
	}

	@Override
	public Iterator<?> asIterator() {
		return list.iterator();
	}

}
