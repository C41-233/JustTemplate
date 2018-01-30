package test.c41.template;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

import c41.template.JustTemplate;
import c41.template.internal.util.ErrorString;
import c41.template.resolver.ResolveException;

@SuppressWarnings("unused")
public class TestResolveException {

	private static class Test1{
		String value = "123";
	}
	
	@Test
	public void test7() {
		try {
			JustTemplate.render("${.value}", false);
		}
		catch (ResolveException e) {
			assertEquals("read property 'value' in class java.lang.Boolean in line 1 column 4", e.getMessage());
			return;
		}
		fail();
	}

	@Test
	public void test8() {
		try {
			JustTemplate.render("${.value.value}", new Test1());
		}
		catch (ResolveException e) {
			assertEquals(ErrorString.readBadPropertyOfType("value", String.class, 1, 10), e.getMessage());
			return;
		}
		fail();
	}

	@Test
	public void test9() {
		try {
			JustTemplate.render("${.value[value]}", new Test1());
		}
		catch (ResolveException e) {
			assertEquals("read property 'value' in class java.lang.String in line 1 column 10", e.getMessage());
			return;
		}
		fail();
	}

	@Test
	public void test10() {
		try {
			JustTemplate.render("${.value[value]}", "12345");
		}
		catch (ResolveException e) {
			assertEquals("read property 'value' in class java.lang.String in line 1 column 4", e.getMessage());
			return;
		}
		fail();
	}

}
