package c41.template.resolver.reflect;

interface IContext {

	public IObject getParameter(String name, int line, int column);
	public IObject getContextObject();
	
}
