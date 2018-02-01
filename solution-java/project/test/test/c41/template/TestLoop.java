package test.c41.template;

import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.Test;

import c41.template.JustTemplate;

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

	@Test
	public void test2() {
		assertEquals("12345", JustTemplate.render("#{for value in .}${value}#{endfor}", "12345"));
	}

	@Test
	public void test3() {
		assertEquals("0112233445", JustTemplate.render("#{for value in . by k}${k}${value}#{endfor}", "12345"));
	}

}
