package c41.template.internal.resolve;

import java.util.Iterator;

interface IObject {

	public boolean toBoolean();
	
	@Override
	public String toString();
	
	public Iterator<Object> toIterator();
	
	public IObject getKey(String name);
}
