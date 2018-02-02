package c41.template;

public class TemplatePosition {

	public final int line;
	public final int column;
	
	public TemplatePosition(int line, int column) {
		this.line = line;
		this.column = column;
	}
	
	public TemplatePosition offset(int line, int column) {
		if(line == 1) {
			return new TemplatePosition(this.line, this.column + column - 1);
		}
		else {
			return new TemplatePosition(this.line + line - 1, column);
		}
	}
	
}
