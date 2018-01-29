import c41.template.JustTemplate;

public class Test {

	public static void main(String[] args) {
		JustTemplate.render("123#{if .}true#{endif}456", true);
	}
	

}
