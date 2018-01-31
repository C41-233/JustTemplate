package c41.template.resolver;

interface IObject {

	public String asString();
	
	public IObject getKey(String name, int line, int column);

	public boolean asBoolean();
}
