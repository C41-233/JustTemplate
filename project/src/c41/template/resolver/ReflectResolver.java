package c41.template.resolver;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;

import c41.template.internal.util.Buffer;
import c41.template.internal.util.InputReader;

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
	
	private static IObject getParameterObject(Context context, String name, int line, int column) {
		Buffer buffer = new Buffer();
		InputReader reader = new InputReader(name);
		
		IObject root = null;
		
		ObjectState state = ObjectState.Start;
		Read:
		while(true) {
			int ch = reader.read();
			
			if(ch != InputReader.EOF) {
				
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
						if(root == null) {
							root = context.getParameter(part);
						}
						else {
							root = root.getKey(part, line, column + reader.getColumn() - part.length());
						}
						reader.pushBack();
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
							root = root.getKey(part, line, column + reader.getColumn() - part.length());
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
			else {
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
						root = root.getKey(part, line, column + reader.getColumn() - part.length());
					}
					break Read;
				}
				case StartReadPart:{
					break Read;
				}
				case EndPart:{
					break Read;
				}
				default:
					throw new ResolveException("unexpected eof");
				}
			}
			
		}
		
		if(root == null) {
			throw new ResolveException();
		}
		return root;
	}

}
