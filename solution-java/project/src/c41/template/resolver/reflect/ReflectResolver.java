package c41.template.resolver.reflect;

import java.util.Iterator;
import java.util.Stack;

import c41.template.internal.util.Buffer;
import c41.template.internal.util.ErrorString;
import c41.template.internal.util.InputReader;
import c41.template.resolver.ResolveException;

public class ReflectResolver implements IResolver{

	private final Stack<Context> contexts = new Stack<>();
	
	public ReflectResolver(Object root) {
		Context context = new Context(ObjectCreator.create(root));
		contexts.push(context);
	}

	@Override
	public String onVisitParameter(char mark, String name, int line, int column) {
		IObject object = getParameterObject(contexts.peek(), name, line, column);
		return object.asString();
	}

	@Override
	public boolean onVisitCondition(String name, int line, int column) {
		IObject object = getParameterObject(contexts.peek(), name, line, column);
		return object.asBoolean();
	}

	@Override
	public Iterator<Object> onVisitLoop(String name, int line, int column) {
		IObject object = getParameterObject(contexts.peek(), name, line, column);
		return object.asIterator();
	}

	@Override
	public void createContext(Object current) {
		Context context = new Context(ObjectCreator.create(current), contexts.peek());
		contexts.push(context);
	}

	@Override
	public void createContext(String name, Object current) {
		Context context = new Context(name, ObjectCreator.create(current), contexts.peek());
		contexts.push(context);
	}

	@Override
	public void releaseContext() {
		contexts.pop();
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
			return this.context.getParameter(name, line, column);
		}

		@Override
		public boolean asBoolean() {
			throw new ResolveException();
		}

		@Override
		public Iterator<Object> asIterator() {
			throw new ResolveException();
		}
		
	}
	
	private static IObject getParameterObject(Context context, String name, int line, int column) {
		Buffer buffer = new Buffer();
		InputReader reader = new InputReader(name);
		
		IObject root = null;
		
		ReflectResolverState state = ReflectResolverState.Start;
		Read:while(true) {
			int ch = reader.read();
			
			switch (state) {
			
			case Start:{
				if(ch == '.') {
					root = context.current;
					state = ReflectResolverState.StartReadPart;
				}
				else if(ch == '['){
					root = new ContextObject(context);
					state = ReflectResolverState.ReadFullPart;
				}
				else if(ch == InputReader.EOF) {
					throw new ResolveException(ErrorString.unexpectedEndOfParameter(line, column));
				}
				else {
					root = new ContextObject(context);
					buffer.append(ch);
					state = ReflectResolverState.ReadPart;
				}
				break;
			}
			
			case StartReadPart:{
				if(ch == '[') {
					state = ReflectResolverState.ReadFullPart;
				}
				else if(ch == InputReader.EOF) {
					break Read;
				}
				else {
					reader.pushBack();
					state = ReflectResolverState.ReadPart;
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
					state = ReflectResolverState.EndPart;
				}
				else if(ch == '[') {
					if(buffer.length() == 0) {
						throw new ResolveException();
					}
					String part = buffer.take();
					root = root.getKey(part, line, column + reader.getColumn() - part.length() - 1);
					reader.pushBack();
					state = ReflectResolverState.EndPart;
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
					state = ReflectResolverState.EndPart;
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
					state = ReflectResolverState.ReadPart;
				}
				else if(ch == '['){
					state = ReflectResolverState.ReadFullPart;
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
