package test.c41.template;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;

import org.junit.Test;

import c41.template.JustTemplate;

public class TestTemplate {
	
	@Test
	public void test1() {
		assertEquals("text", JustTemplate.render("text", (Object)null));
		assertEquals("text! {comment}", JustTemplate.render("text! {comment}", (Object)null));
		assertEquals("text$ {.}", JustTemplate.render("text$ {.}", (Object)null));
		assertEquals("text after", JustTemplate.render("text!{comment} after", (Object)null));
		assertEquals("value=100", JustTemplate.render("value=${.}", 100));
	}

	public static class TestObject1{
		public int value = 100;
	}

	public static class TestObject2{
		public TestObject1 obj = new TestObject1();
	}
	
	public static class TestObject3{
		public TestObject2 obj = new TestObject2();
	}
	
	@Test
	public void test2() {
		assertEquals("value=100", JustTemplate.render("value=${.value}", new TestObject1()));
		assertEquals("value=100", JustTemplate.render("value=${.[value]}", new TestObject1()));
		assertEquals("value=100", JustTemplate.render("value=${.obj.value}", new TestObject2()));
		assertEquals("value=100", JustTemplate.render("value=${.obj[value]}", new TestObject2()));
		assertEquals("value=100", JustTemplate.render("value=${.[obj].value}", new TestObject2()));
		assertEquals("value=100", JustTemplate.render("value=${.[obj][value]}", new TestObject2()));
		
		assertEquals("value=100", JustTemplate.render("value=${value}", new TestObject1()));
		assertEquals("value=100", JustTemplate.render("value=${obj.value}", new TestObject2()));
		assertEquals("value=100", JustTemplate.render("value=${obj.obj.value}", new TestObject3()));
	}

	@Test
	public void test3() {
		HashMap<String, Object> map = new HashMap<>();
		map.put("value", 100);
		map.put("value-link", 3.14);
		map.put("value.link", "12345");
		
		assertEquals("value=100", JustTemplate.render("value=${value}", map));
		assertEquals("value-link=3.14", JustTemplate.render("value-link=${value-link}", map));
		assertEquals("value.link=12345", JustTemplate.render("value.link=${[value.link]}", map));
	}

}
