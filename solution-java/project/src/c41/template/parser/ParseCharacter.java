package c41.template.parser;

import java.util.HashSet;

import c41.template.TemplateException;

class ParseCharacter {
	
	private Integer commentPrefix;
	private Integer logicPrefix;
	private HashSet<Integer> parameterPrefixs = new HashSet<>();
	
	private int openMatch = '{';
	private int closeMatch = '}';
	
	private final HashSet<Integer> prefixs = new HashSet<>();
	
	public ParseCharacter() {
		prefixs.add(openMatch);
		prefixs.add(closeMatch);
	}
	
	public void setCommentPrefix(int value) {
		if(commentPrefix != null) {
			prefixs.remove(commentPrefix);
		}
		if(!prefixs.add(value)) {
			throw new TemplateException();
		}
		commentPrefix = value;
	}
	
	public void setLogicPrefix(int value) {
		if(logicPrefix != null) {
			prefixs.remove(logicPrefix);
		}
		if(!prefixs.add(value)) {
			throw new TemplateException();
		}
		logicPrefix = value;
	}
	
	public void addParameterPrefix(int value) {
		if(!parameterPrefixs.add(value)) {
			return;
		}
		if(!prefixs.add(value)) {
			throw new TemplateException();
		}
	}
	
	public void setOpenMatch(int value) {
		prefixs.remove(openMatch);
		if(!prefixs.add(value)) {
			throw new TemplateException();
		}
	}
	
	public void setCloseMatch(int value) {
		prefixs.remove(value);
		if(!prefixs.add(value)) {
			throw new TemplateException();
		}
	}
	
	public boolean isPrefix(int ch) {
		return prefixs.contains(ch);
	}

	public boolean isOpenMatch(int ch) {
		return openMatch == ch;
	}

	public boolean isCloseMatch(int ch) {
		return closeMatch == ch;
	}

	public boolean isComment(int mark) {
		return commentPrefix!=null && commentPrefix.intValue() == mark;
	}

	public boolean isParameterPrefix(int mark) {
		return parameterPrefixs.contains(mark);
	}

	public boolean isLogicPrefix(char mark) {
		return logicPrefix!=null && logicPrefix.intValue() == mark;
	}
	
	public int getOpenMatch() {
		return openMatch;
	}
	
	public int getCloseMatch() {
		return closeMatch;
	}
	
}
