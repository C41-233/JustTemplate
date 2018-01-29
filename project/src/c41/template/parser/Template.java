package c41.template.parser;

import java.util.ArrayList;

import c41.template.internal.util.FastStack;
import c41.template.resolver.IResolver;
import c41.template.resolver.ResolveException;

class Template implements ITemplate{

	private final ArrayList<IFragment> fragments = new ArrayList<>();
	private int ifElseStack = 0;
	
	public void onText(String string) {
		this.fragments.add(new TextFragment(string));
	}

	public void onParameter(char mark, String name) {
		this.fragments.add(new ParameterFragment(mark, name));
	}

	public void onIf(String name, int line, int column) {
		this.fragments.add(new IfFragment(name));
		ifElseStack++;
	}

	public void onElse(int line, int column) {
		if(ifElseStack == 0) {
			throw new ResolveException("else without match if or elseif in line %d column %d", line, column);
		}
	}

	public void onEndif(int line, int column) {
		if(ifElseStack == 0) {
			throw new ResolveException("endif without match if in line %d column %d", line, column);
		}
		this.fragments.add(new EndIfFragment());
		ifElseStack--;
	}

	public void OnElseIf(String take, int line, int column) {
		if(ifElseStack == 0) {
			throw new ResolveException("elseif without match if in line %d column %d", line, column);
		}
		
	}

	public void end() {
		fragments.trimToSize();
	}
	
	@Override
	public String render(IResolver resolve){
		StringBuilder sb = new StringBuilder();
		
		FastStack conditionStack = new FastStack();
		for (IFragment f : fragments) {
			switch (f.getType()) {
			case Text:{
				if(conditionStack.size() > 0 && conditionStack.peek() == 0) {
					continue;
				}
				TextFragment fragment = (TextFragment)f;
				sb.append(fragment.text);
				break;
			}
			case Parameter:{
				if(conditionStack.size() > 0 && conditionStack.peek() == 0) {
					continue;
				}
				ParameterFragment fragment = (ParameterFragment)f;
				sb.append(resolve.onVisitParameter(fragment.mark, fragment.name));
				break;
			}
			case If:{
				IfFragment fragment = (IfFragment) f;
				boolean condition = resolve.OnVisitCondition(fragment.name);
				conditionStack.push(condition ? 1 : 0);
				break;
			}
			case EndIf:{
				conditionStack.pop();
				break;
			}
			
			default:
				throw new ResolveException();
			}
			
		}
		return sb.toString();
	}

}
