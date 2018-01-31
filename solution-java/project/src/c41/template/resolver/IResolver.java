package c41.template.resolver;

public interface IResolver {

	public String onVisitParameter(char mark, String name, int line, int column);
	
	public boolean onVisitCondition(String name, int line, int column);
	
}
