package c41.template.parser;

import java.util.ArrayList;

import c41.template.internal.util.FastStack;
import c41.template.parser.writer.ITemplateWriter;
import c41.template.resolver.IResolver;
import c41.template.resolver.ResolveException;

class Template implements ITemplate{

	private final ArrayList<IFragment> fragments = new ArrayList<>();
	private int ifElseStack = 0;
	
	public void addTextFragment(String string) {
		this.fragments.add(new TextFragment(string));
	}

	public void addParameter(char mark, String name) {
		this.fragments.add(new ParameterFragment(mark, name));
	}

	public void addIfFragment(String name) {
		this.fragments.add(new IfFragment(name));
		ifElseStack++;
	}

	public void endIfFragment() {
		if(ifElseStack == 0) {
			throw new ResolveException();
		}
		this.fragments.add(new EndIfFragment());
		ifElseStack--;
	}

	public void end() {
		fragments.trimToSize();
	}
	
	@Override
	public void renderTo(IResolver resolve, ITemplateWriter writer){
		FastStack conditionStack = new FastStack();
		for (IFragment f : fragments) {
			switch (f.getType()) {
			case Text:{
				if(conditionStack.size() > 0 && conditionStack.peek() == 0) {
					continue;
				}
				TextFragment fragment = (TextFragment)f;
				writer.write(fragment.text);
				break;
			}
			case Parameter:{
				if(conditionStack.size() > 0 && conditionStack.peek() == 0) {
					continue;
				}
				ParameterFragment fragment = (ParameterFragment)f;
				writer.write(resolve.onVisitParameter(fragment.mark, fragment.name));
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
	}

}
