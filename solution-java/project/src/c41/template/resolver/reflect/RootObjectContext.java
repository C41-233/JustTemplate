package c41.template.resolver.reflect;

class RootObjectContext implements IContext{

	private final IObject root;
	
	public RootObjectContext(Object root) {
		this.root = ObjectCreator.create(root);
	}
	
	@Override
	public IObject getParameter(String name, int line, int column) {
		return root.getKey(name, line, column);
	}

	@Override
	public IObject getContextObject() {
		return root;
	}

}
