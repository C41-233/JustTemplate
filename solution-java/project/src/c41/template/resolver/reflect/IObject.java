package c41.template.resolver.reflect;

import java.util.Iterator;

interface IObject {

	public String asString();
	
	public IObject getKey(String name, int line, int column);

	public boolean asBoolean();

	public Iterator<?> asIterator();
}
