package c41.template.resolver.reflect;

import java.util.Iterator;

public interface IResolver {

	public String onVisitParameter(char mark, String name, int line, int column);
	
	public boolean onVisitCondition(String name, int line, int column);

	public Iterator<?> onVisitLoop(String name, int line, int column);
	
	public void enterContext(Object current);
	
	public void enterContext(String name, Object current);
	
	public void leaveContext();
	
}
