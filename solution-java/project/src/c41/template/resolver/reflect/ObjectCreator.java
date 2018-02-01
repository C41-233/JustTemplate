package c41.template.resolver.reflect;

import java.util.List;
import java.util.Map;

final class ObjectCreator {

	public static IObject create(Object object) {
		if(object == null) {
			return new NullObject();
		}
		if(object instanceof Boolean) {
			return new BooleanObject((Boolean) object);
		}
		if(object instanceof Integer) {
			return new IntegerObject((Integer) object);
		}
		if(object instanceof Double) {
			return new DoubleObject((Double) object);
		}
		if(object instanceof String) {
			return new StringObject((String) object);
		}
		if(object instanceof Map) {
			return new MapObject((Map<?, ?>) object);
		}
		if(object instanceof List) {
			return new ListObject((List<?>) object);
		}
		return new ObjectObject(object);
	}
	
}
