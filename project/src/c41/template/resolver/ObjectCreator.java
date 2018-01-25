package c41.template.resolver;

final class ObjectCreator {

	public static IObject create(Object object) {
		if(object instanceof Number) {
			return new NumberObject((Number) object);
		}
		return new ObjectObject(object);
	}
	
}
