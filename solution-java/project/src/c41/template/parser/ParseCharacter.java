package c41.template.parser;

import java.util.HashSet;

import c41.template.TemplateException;

class ParseCharacter {
	
	private Character commentPrefix;
	private Character logicPrefix;
	private HashSet<Character> parameterPrefixs = new HashSet<>();
	
	private char openMatch = '{';
	private char closeMatch = '}';
	
	private final HashSet<Character> prefixs = new HashSet<>();
	
	public ParseCharacter() {
		prefixs.add(openMatch);
		prefixs.add(closeMatch);
	}
	
	public void setCommentPrefix(char value) {
		if(commentPrefix != null) {
			prefixs.remove(commentPrefix);
		}
		if(!prefixs.add(value)) {
			throw new TemplateException("conflict prefix %c", value);
		}
		commentPrefix = value;
	}
	
	public void setLogicPrefix(char value) {
		if(logicPrefix != null) {
			prefixs.remove(logicPrefix);
		}
		if(!prefixs.add(value)) {
			throw new TemplateException("conflict prefix %c", value);
		}
		logicPrefix = value;
	}
	
	public void addParameterPrefix(char value) {
		if(!parameterPrefixs.add(value)) {
			return;
		}
		if(!prefixs.add(value)) {
			throw new TemplateException("conflict prefix %c", value);
		}
	}
	
	public void setOpenMatch(char value) {
		prefixs.remove(openMatch);
		if(!prefixs.add(value)) {
			throw new TemplateException("conflict prefix %c", value);
		}
	}
	
	public void setCloseMatch(char value) {
		prefixs.remove(value);
		if(!prefixs.add(value)) {
			throw new TemplateException("conflict prefix %c", value);
		}
	}
	
	public boolean isPrefix(char ch) {
		return prefixs.contains(ch);
	}

	public boolean isPrefix(int ch) {
		return isPrefix((char)ch);
	}
	
	public boolean isOpenMatch(char ch) {
		return openMatch == ch;
	}

	public boolean isOpenMatch(int ch) {
		return isOpenMatch((char)ch);
	}
	
	public boolean isCloseMatch(char ch) {
		return closeMatch == ch;
	}

	public boolean isCloseMatch(int ch) {
		return isCloseMatch((char)ch);
	}
	
	public boolean isComment(char mark) {
		return commentPrefix!=null && commentPrefix.charValue() == mark;
	}

	public boolean isParameterPrefix(char mark) {
		return parameterPrefixs.contains(mark);
	}

	public boolean isLogicPrefix(char mark) {
		return logicPrefix!=null && logicPrefix.charValue() == mark;
	}
	
	public char getOpenMatch() {
		return openMatch;
	}
	
	public char getCloseMatch() {
		return closeMatch;
	}
	
}
