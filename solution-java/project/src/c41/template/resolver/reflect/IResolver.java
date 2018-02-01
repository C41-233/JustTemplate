package c41.template.resolver.reflect;

import java.util.Iterator;

public interface IResolver {

	public String onVisitParameter(char mark, String name, int line, int column);
	
	public boolean onVisitCondition(String name, int line, int column);

	public Iterator<Object> onVisitLoop(String name, int line, int column);
	
	public void createContext(Object current);
	
	public void createContext(String name, Object current);
	
	public void releaseContext();
	
}
