package test.c41.template;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import c41.template.JustTemplate;

public class TestLogic {

	@Test
	public void test1() {
		assertEquals("123true456", JustTemplate.render("123#{if .}true#{endif}456", true));
		assertEquals("123456", JustTemplate.render("123#{if .}true#{endif}456", false));
	}

}
