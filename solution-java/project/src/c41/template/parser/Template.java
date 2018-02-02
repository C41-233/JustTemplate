package c41.template.parser;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;

import c41.template.TemplateException;
import c41.template.internal.util.ErrorString;
import c41.template.internal.util.FastStack;
import c41.template.resolver.reflect.IResolver;

class Template implements ITemplate{

	private final ArrayList<IFragment> fragments = new ArrayList<>();
	
	private FastStack logicStack = new FastStack();
	
	public Template() {
		logicStack.push(LogicMatch.MatchAll);
	}
	
	void onText(String string, int line, int column) {
		this.fragments.add(new TextFragment(string));
	}

	void onParameter(char mark, String name, int line, int column) {
		this.fragments.add(new ParameterFragment(mark, name, line, column));
	}

	void onIf(String name, int lineStart, int columnStart, int lineParameter, int columnParameter) {
		this.fragments.add(new IfFragment(name, lineParameter, columnParameter));
		logicStack.push(LogicMatch.MatchIf);
	}

	void onElse(int line, int column) {
		if(logicStack.peek() != LogicMatch.MatchIf) {
			throw new TemplateException(ErrorString.unmatchedElse(line, column));
		}
		logicStack.pop();
		logicStack.push(LogicMatch.MatchElse);
		this.fragments.add(new ElseFragment());
	}

	void onElseIf(String name, int lineStart, int columnStart, int lineParameter, int columnParameter) {
		if(logicStack.peek() != LogicMatch.MatchIf) {
			throw new TemplateException(ErrorString.unmatchedElseIf(lineStart, columnStart));
		}
		this.fragments.add(new ElseIfFragment(name, lineParameter, columnParameter));
	}
	
	void onEndIf(int line, int column) {
		if(logicStack.peek() != LogicMatch.MatchIf && logicStack.peek() != LogicMatch.MatchElse) {
			throw new TemplateException(ErrorString.unmatchedEndIf(line, column));
		}
		this.fragments.add(new EndIfFragment());
		logicStack.pop();
	}

	public void onFor(String name, int lineStart, int columnStart, int lineParameter, int columnParameter) {
		this.fragments.add(new ForFragment(name, null, null));
		logicStack.push(LogicMatch.MatchFor);
	}

	public void onEndFor(int line, int column) {
		if(logicStack.peek() != LogicMatch.MatchFor) {
			throw new TemplateException(ErrorString.unmatchedEndFor(line, column));
		}
		this.fragments.add(new EndForFragment());
		logicStack.pop();
	}
	
	public void end(int line) {
		if(logicStack.size() != 1) {
			throw new TemplateException(ErrorString.unexpectedEOF(line));
		}
		fragments.trimToSize();
	}

	@Override
	public String render(IResolver resolve){
		final int Condition_False = 0;
		final int Condition_True = 1;
		final int Condition_Ignore = 2;
		
		StringBuilder sb = new StringBuilder();
		
		FastStack conditionStack = new FastStack();
		conditionStack.push(Condition_True);
		
		Stack<LoopContext> loopStack = new Stack<>();
		
		for (int i=0; i<fragments.size(); i++) {
			IFragment f = fragments.get(i);
			switch (f.getType()) {
			case Text:{
				if(conditionStack.peek() != Condition_True) {
					continue;
				}
				TextFragment fragment = (TextFragment)f;
				sb.append(fragment.text);
				break;
			}
			case Parameter:{
				if(conditionStack.peek() != Condition_True) {
					continue;
				}
				ParameterFragment fragment = (ParameterFragment)f;
				sb.append(resolve.onVisitParameter(fragment.mark, fragment.name, fragment.line, fragment.column));
				break;
			}
			case If:{
				IfFragment fragment = (IfFragment) f;
				boolean condition = resolve.onVisitCondition(fragment.name, fragment.line, fragment.column);
				if(conditionStack.peek() == Condition_True) {
					conditionStack.push(condition ? Condition_True : Condition_False);
				}
				else {
					conditionStack.push(Condition_Ignore);
				}
				break;
			}
			case ElseIf:{
				ElseIfFragment fragment = (ElseIfFragment) f;
				boolean condition = resolve.onVisitCondition(fragment.name, fragment.line, fragment.column);
				int val = conditionStack.pop();
				if(val == Condition_False) {
					conditionStack.push(condition ? Condition_True : Condition_False);
				}
				else {
					conditionStack.push(Condition_Ignore);
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
			
			case For:{
				if(conditionStack.peek() != Condition_True) {
					continue;
				}
				ForFragment fragment = (ForFragment) f;
				if(fragment.key == null) {
					Iterator<?> iterator = resolve.onVisitLoop(fragment.name, fragment.line, fragment.column);
					if(iterator.hasNext()) {
						Object context = iterator.next();
						if(fragment.value == null) {
							resolve.enterContext(context);
						}
						else {
							resolve.enterContext(fragment.value, context);
						}
					}
					loopStack.push(new LoopContext(i, iterator, fragment.key, fragment.value));
				}
				break;
			}
			
			case EndFor:{
				if(conditionStack.peek() != Condition_True) {
					continue;
				}
				LoopContext loop = loopStack.peek();
				if(loop.iterator.hasNext()) {
					resolve.leaveContext();
					if(loop.key != null) {
						resolve.leaveContext();
					}
					
					i = loop.start;
					Object context = loop.iterator.next();
					if(loop.key == null) {
						if(loop.value == null) {
							resolve.enterContext(context);
						}
						else {
							resolve.enterContext(loop.value, context);
						}
					}
				}
				else {
					loopStack.pop();
				}
				
				break;
			}
			
			default:
				throw new TemplateException("state %s", f.getType());
			}
			
		}
		
		if(conditionStack.size() != 1) {
			throw new TemplateException();
		}
		return sb.toString();
	}

	private static class LoopContext{
		
		public final int start;
		public final Iterator<?> iterator;
		public final String key;
		public final String value;
		
		public LoopContext(int start, Iterator<?> iterator, String key, String value) {
			this.start = start;
			this.iterator = iterator;
			this.key = key;
			this.value = value;
		}
	}

	private static interface LogicMatch{

		public static final int MatchAll = 0;
		public static final int MatchIf = 1;
		public static final int MatchElse = 2;
		public static final int MatchFor = 3;
		
	}
	
}
