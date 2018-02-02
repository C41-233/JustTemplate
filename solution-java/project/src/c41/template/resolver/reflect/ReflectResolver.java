package c41.template.resolver.reflect;

import java.util.Iterator;
import java.util.Map;
import java.util.Stack;

import c41.template.TemplateException;
import c41.template.internal.util.Buffer;
import c41.template.internal.util.ErrorString;
import c41.template.internal.util.InputReader;

public class ReflectResolver implements IResolver{

	private final Stack<IContext> contexts = new Stack<>();
	
	public ReflectResolver(Object object) {
		IContext context = new RootObjectContext(object);
		contexts.push(context);
	}

	public ReflectResolver(Map<String, ?> parameters) {
		IContext context = new RootMapContext(parameters);
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
	public Iterator<?> onVisitLoop(String name, int line, int column) {
		IObject object = getParameterObject(contexts.peek(), name, line, column);
		return object.asIterator();
	}

	@Override
	public void enterContext(Object current) {
		IContext context = new OverrideObjectContext(current, contexts.peek());
		contexts.push(context);
	}

	@Override
	public void enterContext(String name, Object value) {
		IContext context = new OverrideParameterObject(name, value, contexts.peek());
		contexts.push(context);
	}

	@Override
	public void leaveContext() {
		contexts.pop();
	}

	private static IObject getParameterObject(IContext context, String name, int line, int column) {
		Buffer buffer = new Buffer();
		InputReader reader = new InputReader(name);
		
		IObject root = null;
		
		ReflectResolverState state = ReflectResolverState.Start;
		Read:while(true) {
			int ch = reader.read();
			
			switch (state) {
			
			case Start:{
				if(ch == '.') {
					root = context.getContextObject();
					state = ReflectResolverState.StartReadPart;
				}
				else if(ch == '['){
					root = new ContextObject(context);
					state = ReflectResolverState.ReadFullPart;
				}
				else if(ch == InputReader.EOF) {
					throw new TemplateException(ErrorString.unexpectedEndOfParameter(line, column));
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
						throw new TemplateException();
					}
					String part = buffer.take();
					root = root.getKey(part, line, column + reader.getColumn() - part.length());
					reader.pushBack();
					state = ReflectResolverState.EndPart;
				}
				else if(ch == '[') {
					if(buffer.length() == 0) {
						throw new TemplateException();
					}
					String part = buffer.take();
					root = root.getKey(part, line, column + reader.getColumn() - part.length() - 1);
					reader.pushBack();
					state = ReflectResolverState.EndPart;
				}
				else if(ch == InputReader.EOF) {
					if(buffer.length() == 0) {
						throw new TemplateException();
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
						throw new TemplateException();
					}
					String part = buffer.take();
					root = root.getKey(part, line, column + reader.getColumn() - part.length() - 1);
					state = ReflectResolverState.EndPart;
				}
				else if(ch == InputReader.EOF) {
					throw new TemplateException(ErrorString.unexpectedEndOfParameter(line, column));
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
					throw new TemplateException("unexpected character %c", ch);
				}
				break;
			}
			
			default:
				throw new TemplateException();
			}
		}
		
		if(root == null) {
			throw new TemplateException();
		}
		
		return root;
	}

}

class ContextObject implements IObject{

	private final IContext context;
	
	public ContextObject(IContext context) {
		this.context = context;
	}
	
	@Override
	public String asString() {
		throw new TemplateException();
	}

	@Override
	public IObject getKey(String name, int line, int column) {
		return this.context.getParameter(name, line, column);
	}

	@Override
	public boolean asBoolean() {
		throw new TemplateException();
	}

	@Override
	public Iterator<Object> asIterator() {
		throw new TemplateException();
	}
	
}
