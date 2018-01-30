package c41.template.resolver;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;

import c41.template.internal.util.Buffer;
import c41.template.internal.util.InputReader;
import c41.template.internal.util.ErrorString;

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
	public String onVisitParameter(char mark, String name, int line, int column) {
		IObject object = getParameterObject(current(), name, line, column);
		return object.asString();
	}

	@Override
	public boolean OnVisitCondition(String name, int line, int column) {
		IObject object = getParameterObject(current(), name, line, column);
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
	
	private static class ContextObject implements IObject{

		private final Context context;
		
		public ContextObject(Context context) {
			this.context = context;
		}
		
		@Override
		public String asString() {
			throw new ResolveException();
		}

		@Override
		public IObject getKey(String name, int line, int column) {
			return context.getParameter(name);
		}

		@Override
		public boolean asBoolean() {
			throw new ResolveException();
		}
		
	}
	
	private static IObject getParameterObject(Context context, String name, int line, int column) {
		Buffer buffer = new Buffer();
		InputReader reader = new InputReader(name);
		
		IObject root = null;
		
		ObjectState state = ObjectState.Start;
		Read:while(true) {
			int ch = reader.read();
			
			switch (state) {
			
			case Start:{
				if(ch == '.') {
					root = context.current;
					state = ObjectState.StartReadPart;
				}
				else if(ch == '['){
					root = new ContextObject(context);
					state = ObjectState.ReadFullPart;
				}
				else if(ch == InputReader.EOF) {
					throw new ResolveException(ErrorString.unexpectedEndOfParameter(line, column));
				}
				else {
					root = new ContextObject(context);
					buffer.append(ch);
					state = ObjectState.ReadPart;
				}
				break;
			}
			
			case StartReadPart:{
				if(ch == '[') {
					state = ObjectState.ReadFullPart;
				}
				else if(ch == InputReader.EOF) {
					break Read;
				}
				else {
					reader.pushBack();
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
					root = root.getKey(part, line, column + reader.getColumn() - part.length());
					reader.pushBack();
					state = ObjectState.EndPart;
				}
				else if(ch == '[') {
					if(buffer.length() == 0) {
						throw new ResolveException();
					}
					String part = buffer.take();
					root = root.getKey(part, line, column + reader.getColumn() - part.length() - 1);
					reader.pushBack();
					state = ObjectState.EndPart;
				}
				else if(ch == InputReader.EOF) {
					if(buffer.length() == 0) {
						throw new ResolveException();
					}
					String part = buffer.take();
					root = root.getKey(part, line, column + reader.getColumn() - part.length());
					break Read;
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
					root = root.getKey(part, line, column + reader.getColumn() - part.length() - 1);
					state = ObjectState.EndPart;
				}
				else if(ch == InputReader.EOF) {
					throw new ResolveException(ErrorString.unexpectedEndOfParameter(line, column));
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
				else if(ch == InputReader.EOF) {
					break Read;
				}
				else {
					throw new ResolveException("unexpected character %c", ch);
				}
				break;
			}
			
			default:
				throw new ResolveException();
			}
		}
		
		if(root == null) {
			throw new ResolveException();
		}
		
		return root;
	}

}
