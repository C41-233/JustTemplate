import c41.template.JustTemplate;

public class Test {

	private static class Test1{
		String value = "123";
	}
	
	public static void main(String[] args) {
		JustTemplate.render("${.value[value]}", "12345");
	}
	

}
