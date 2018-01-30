package test.c41.template;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;

import org.junit.Test;

import c41.template.JustTemplate;

public class TestLogic {

	@Test
	public void test1() {
		assertEquals("123true456", JustTemplate.render("123#{if .}true#{endif}456", true));
		assertEquals("123456", JustTemplate.render("123#{if .}true#{endif}456", false));
		assertEquals("123false456", JustTemplate.render("123#{if .}true#{else}false#{endif}456", false));
	}

	@Test
	public void test2() {
		HashMap<String, Boolean> map = new HashMap<>();
		map.put("condition1", false);
		map.put("condition2", true);
		map.put("condition3", true);
		
		assertEquals("condition2\n", JustTemplate.render("#{if condition1}condition1\n#{elseif condition2}condition2\n#{elseif condition3}condition3\n#{else}condition4\n#{endif}", map));
	}

}
