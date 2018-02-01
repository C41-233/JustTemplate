package test.c41.template;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.Test;

import com.sun.xml.internal.bind.v2.model.core.Adapter;

import c41.template.JustTemplate;

@SuppressWarnings("unused")
public class TestLoop {

	@Test
	public void test1() {
		assertEquals("12345", JustTemplate.render("#{for .}${.}#{endfor}", "12345"));
	}

	@Test
	public void test4() {
		HashMap<String, Object> map = new HashMap<>();
		map.put("value", "12345");
		map.put("key", 1);
		assertEquals("1112131415", JustTemplate.render("#{for value}${key}${.}#{endfor}", map));
	}
	
	private static class Test1{
		int val;
		
		public Test1(int val) {
			this.val = val;
		}
	}
	
	@Test
	public void test5() {
		List<Test1> list = new ArrayList<>();
		list.add(new Test1(1));
		list.add(new Test1(2));
		list.add(new Test1(3));
		assertEquals("123", JustTemplate.render("#{for .}${.val}#{endfor}", list));
	}
	
/*
	@Test
	public void test2() {
		assertEquals("12345", JustTemplate.render("#{for value in .}${value}#{endfor}", "12345"));
	}

	@Test
	public void test3() {
		assertEquals("0112233445", JustTemplate.render("#{for value in . by k}${k}${value}#{endfor}", "12345"));
	}
*/
}
