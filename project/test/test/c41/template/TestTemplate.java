package test.c41.template;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import c41.template.parser.TemplateParser;

public class TestTemplate {

	private TemplateParser parser;
	
	@Before
	public void init() {
		parser = new TemplateParser();
	}
	
	@Test
	public void test1() {
		assertEquals("text", parser.parse("text").render(null));
		assertEquals("text! {comment}", parser.parse("text! {comment}").render(null));
		assertEquals("text$ {.}", parser.parse("text$ {.}").render(null));
		assertEquals("text after", parser.parse("text!{comment} after").render(null));
		assertEquals("value=100", parser.parse("value=${.}").render(100));
	}

	public static class TestObject1{
		public int value = 100;
	}

	public static class TestObject2{
		public TestObject1 obj = new TestObject1();
	}
	
	@Test
	public void test2() {
		assertEquals("value=100", parser.parse("value=${.value}").render(new TestObject1()));
		assertEquals("value=100", parser.parse("value=${.obj.value}").render(new TestObject2()));
	}

	@Test
	public void test3() {
		HashMap<String, Object> map = new HashMap<>();
		map.put("value", 100);
		assertEquals("value=100", parser.parse("value=${value}").render(map));
	}

}
