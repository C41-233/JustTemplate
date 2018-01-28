package c41.template.parser;

enum FragmentType{
	Text,
	Parameter,
	If,
	EndIf,
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
	
	public ParameterFragment(char mark, String name) {
		this.mark = mark;
		this.name = name;
	}

	@Override
	public FragmentType getType() {
		return FragmentType.Parameter;
	}
	
}

class IfFragment implements IFragment{

	public final String name;
	
	public IfFragment(String name) {
		this.name = name;
	}

	@Override
	public FragmentType getType() {
		return FragmentType.If;
	}
	
}

class EndIfFragment implements IFragment{

	@Override
	public FragmentType getType() {
		return FragmentType.EndIf;
	}
	
}