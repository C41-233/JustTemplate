package c41.template.resolver;

import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;

import c41.template.internal.util.Buffer;

public class ReflectResolver implements IResolver{


	private final ArrayList<Context> contexts = new ArrayList<>();
	
	public ReflectResolver(Object root) {
		Context context = new Context(ObjectCreator.create(root));
		push(context);
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
		IObject object = getLeafObject(current(), name);
		return object.toString();
	}

	private static enum ObjectState{
		Start,
		StartReadPart,
		ReadPart,
		ReadFullPart,
	}
	
	private static IObject getLeafObject(Context context, String name) {
		Buffer buffer = new Buffer();
		
		IObject root = null;
		
		ObjectState state = ObjectState.Start;
		for(int i=0; i<name.length(); i++) {
			char ch = name.charAt(i);
			switch (state) {
			//first head
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
				if(ch == '.' || ch == '[' || ch == ']') {
					throw new ResolveException();
				}
				else {
					buffer.append(ch);
					state = ObjectState.ReadPart;
				}
				break;
			}
			
			case ReadPart:{
				if(ch == '.') {
					if(buffer.length() == 0) {
						throw new ResolveException();
					}
					String part = buffer.toString();
					buffer.clear();
					if(root == null) {
						root = context.getParameter(part);
					}
					else {
						root = root.getKey(part);
					}
				}
				else {
					buffer.append(ch);
				}
				break;
			}
			
			default:
				break;
			}
		}
		
		//EOF
		switch (state) {
		case ReadPart:
			if(buffer.length() == 0) {
				throw new ResolveException();
			}
			String part = buffer.toString();
			buffer.clear();
			if(root == null) {
				root = context.getParameter(part);
			}
			else {
				root = root.getKey(part);
			}
			break;
		default:
			break;
		}
		
		return root;
	}
	
	private Context current() {
		return contexts.get(contexts.size() - 1);
	}
	
	private void push(Context context) {
		contexts.add(context);
	}
	
}
