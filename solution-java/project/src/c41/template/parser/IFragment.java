package c41.template.parser;

import c41.template.TemplatePosition;

enum FragmentType{
	Text,
	Parameter,
	If,
	Else,
	ElseIf,
	EndIf,
	For,
	EndFor
}

interface IFragment {
	
	public FragmentType getType();
	
}

class TextFragment implements IFragment{

	public final String text;
	
	public TextFragment(String text) {
		this.text = text;
	}

	@Override
	public FragmentType getType() {
		return FragmentType.Text;
	}

}

class ParameterFragment implements IFragment{

	public final char mark;
	public final String name;
	
	public TemplatePosition position;
	
	public ParameterFragment(char mark, String name, TemplatePosition position) {
		this.mark = mark;
		this.name = name;
		this.position = position;
	}

	@Override
	public FragmentType getType() {
		return FragmentType.Parameter;
	}
	
}

class IfFragment implements IFragment{

	public final String name;
	public final int line;
	public final int column;
	
	public IfFragment(String name, int line, int column) {
		this.name = name;
		this.line = line;
		this.column = column;
	}

	@Override
	public FragmentType getType() {
		return FragmentType.If;
	}
	
}

class ElseIfFragment implements IFragment{

	public final String name;
	public final int line;
	public final int column;
	
	public ElseIfFragment(String name, int line, int column) {
		this.name = name;
		this.line = line;
		this.column = column;
	}

	@Override
	public FragmentType getType() {
		return FragmentType.ElseIf;
	}
	
}

class ElseFragment implements IFragment{

	@Override
	public FragmentType getType() {
		return FragmentType.Else;
	}
	
}

class EndIfFragment implements IFragment{

	@Override
	public FragmentType getType() {
		return FragmentType.EndIf;
	}
	
}

class ForFragment implements IFragment{
	
	public final String name;
	public final String key;
	public final String value;
	
	public int line;
	public int column;
	
	public ForFragment(String name, String key, String value) {
		this.name = name;
		this.key = key;
		this.value = value;
	}

	@Override
	public FragmentType getType() {
		return FragmentType.For;
	}
	
}

class EndForFragment implements IFragment{

	@Override
	public FragmentType getType() {
		return FragmentType.EndFor;
	}
	
}