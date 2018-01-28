package c41.template.resolver;

public interface IResolver {

	public String onVisitParameter(char mark, String name);
	
	public boolean OnVisitCondition(String name);
	
}
