package c41.template.resolver;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;

import c41.template.internal.util.Buffer;

public class ReflectResolver implements IResolver{

	private final ArrayList<Context> contexts = new ArrayList<>();
	
	public ReflectResolver(Object root) {
		try {
			Context context = new Context(ObjectCreator.create(root));
			if(root != null) {
				for (Field field : root.getClass().getFields()) {
					context.addParameter(field.getName(), ObjectCreator.create(field.get(root)));
				}
			}
			push(context);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			throw new ResolveException(e);
		}
	}

	public ReflectResolver(Map<String, ?> arguments) {
		Context context = new Context(ObjectCreator.create(arguments));
		for (Entry<String, ?> entry: arguments.entrySet()) {
			context.addParameter(entry.getKey(), ObjectCreator.create(entry.getValue()));
		}
		push(context);
	}
	
	@Override
	public String onVisitParameter(char mark, String name) {
		IObject object = getParameterObject(current(), name);
		return object.asString();
	}

	@Override
	public boolean OnVisitCondition(String name) {
		IObject object = getParameterObject(current(), name);
		return object.asBoolean();
	}
	
	private static enum ObjectState{
		Start,
		StartReadPart,
		ReadPart,
		ReadFullPart,
		EndPart,
	}

	private Context current() {
		return contexts.get(contexts.size() - 1);
	}
	
	private void push(Context context) {
		contexts.add(context);
	}
	
	private static IObject getParameterObject(Context context, String name) {
		Buffer buffer = new Buffer();
		
		IObject root = null;
		
		ObjectState state = ObjectState.Start;
		for(int i=0; i<name.length(); i++) {
			char ch = name.charAt(i);
			switch (state) {
			
			case Start:{
				if(ch == '.') {
					root = context.current;
					state = ObjectState.StartReadPart;
				}
				else if(ch == '['){
					state = ObjectState.ReadFullPart;
				}
				else {
					buffer.append(ch);
					state = ObjectState.ReadPart;
				}
				break;
			}
			
			case StartReadPart:{
				if(ch == '[') {
					state = ObjectState.ReadFullPart;
				}
				else {
					i--;
					state = ObjectState.ReadPart;
				}
				break;
			}
			
			case ReadPart:{
				if(ch == '.') {
					if(buffer.length() == 0) {
						throw new ResolveException();
					}
					String part = buffer.take();
					if(root == null) {
						root = context.getParameter(part);
					}
					else {
						root = root.getKey(part);
					}
					i--;
					state = ObjectState.EndPart;
				}
				else {
					buffer.append(ch);
				}
				break;
			}
			
			case ReadFullPart:{
				if(ch == ']') {
					if(buffer.length() == 0) {
						throw new ResolveException();
					}
					String part = buffer.take();
					if(root == null) {
						root = context.getParameter(part);
					}
					else {
						root = root.getKey(part);
					}
					state = ObjectState.EndPart;
				}
				else {
					buffer.append(ch);
				}
				break;
			}
			
			case EndPart:{
				if(ch == '.') {
					state = ObjectState.ReadPart;
				}
				else if(ch == '['){
					state = ObjectState.ReadFullPart;
				}
				else {
					throw new ResolveException("unexpected character %c", ch);
				}
				break;
			}
			
			default:
				break;
			}
		}
		
		//EOF
		switch (state) {
		case ReadPart:{
			if(buffer.length() == 0) {
				throw new ResolveException();
			}
			String part = buffer.take();
			if(root == null) {
				root = context.getParameter(part);
			}
			else {
				root = root.getKey(part);
			}
			break;
		}
		case StartReadPart:{
			break;
		}
		case EndPart:{
			break;
		}
		default:
			throw new ResolveException("unexpected eof");
		}
		
		if(root == null) {
			throw new ResolveException();
		}
		return root;
	}

}
