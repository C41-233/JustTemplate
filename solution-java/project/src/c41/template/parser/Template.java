package c41.template.parser;

import java.util.ArrayList;

import c41.template.internal.util.ErrorString;
import c41.template.internal.util.FastStack;
import c41.template.resolver.IResolver;
import c41.template.resolver.ResolveException;

class Template implements ITemplate{

	private final ArrayList<IFragment> fragments = new ArrayList<>();
	
	private FastStack ifElseStack = new FastStack();
	private final int MatchIf = 0;
	private final int MatchElse = 1;
	
	void onText(String string) {
		this.fragments.add(new TextFragment(string));
	}

	void onParameter(char mark, String name, int line, int column) {
		this.fragments.add(new ParameterFragment(mark, name, line, column));
	}

	void onIf(String name, int lineStart, int columnStart, int lineParameter, int columnParameter) {
		this.fragments.add(new IfFragment(name, lineParameter, columnParameter));
		ifElseStack.push(MatchIf);
	}

	void onElse(int line, int column) {
		if(ifElseStack.size() == 0 || ifElseStack.peek() == MatchElse) {
			throw new ResolveException(ErrorString.unmatchedElse(line, column));
		}
		ifElseStack.pop();
		ifElseStack.push(MatchElse);
		this.fragments.add(new ElseFragment());
	}

	void onElseIf(String name, int lineStart, int columnStart, int lineParameter, int columnParameter) {
		if(ifElseStack.size() == 0 || ifElseStack.peek() == MatchElse) {
			throw new ResolveException(ErrorString.unmatchedElseIf(lineStart, columnStart));
		}
		this.fragments.add(new ElseFragment());
		this.fragments.add(new IfFragment(name, lineParameter, columnParameter));
	}
	
	void onEndif(int line, int column) {
		if(ifElseStack.size() == 0) {
			throw new ResolveException(ErrorString.unmatchedEndIf(line, column));
		}
		this.fragments.add(new EndIfFragment());
		ifElseStack.pop();
	}

	public void end() {
		fragments.trimToSize();
	}
	
	@Override
	public String render(IResolver resolve){
		final int Condition_False = 0;
		final int Condition_True = 1;
		final int Condition_Ignore = 2;
		
		StringBuilder sb = new StringBuilder();
		
		FastStack conditionStack = new FastStack();
		for (IFragment f : fragments) {
			switch (f.getType()) {
			case Text:{
				if(conditionStack.size() > 0 && conditionStack.peek() != Condition_True) {
					continue;
				}
				TextFragment fragment = (TextFragment)f;
				sb.append(fragment.text);
				break;
			}
			case Parameter:{
				if(conditionStack.size() > 0 && conditionStack.peek() != Condition_True) {
					continue;
				}
				ParameterFragment fragment = (ParameterFragment)f;
				sb.append(resolve.onVisitParameter(fragment.mark, fragment.name, fragment.line, fragment.column));
				break;
			}
			case If:{
				IfFragment fragment = (IfFragment) f;
				boolean condition = resolve.OnVisitCondition(fragment.name, fragment.line, fragment.column);
				if(conditionStack.size() > 0) {
					if(conditionStack.peek() == Condition_True) {
						conditionStack.push(condition ? Condition_True : Condition_False);
					}
					else {
						conditionStack.push(Condition_Ignore);
					}
				}
				else {
					conditionStack.push(condition ? Condition_True : Condition_False);
				}
				break;
			}
			case Else:{
				int val = conditionStack.pop();
				if(val == Condition_False) {
					conditionStack.push(Condition_True);
				}
				else{
					conditionStack.push(Condition_Ignore);
				}
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
