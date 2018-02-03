package test.c41.template;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import c41.template.JustTemplate;
import c41.template.TemplateException;
import c41.template.internal.util.ErrorString;

@SuppressWarnings("unused")
public class TestParseException {

	@Test
	public void test1() {
		try {
			JustTemplate.render("#{ip}#{endif}", false);
		}
		catch (TemplateException e) {
			assertEquals(ErrorString.unrecognizedLogicWord("ip", 1, 3), e.getMessage());
			return;
		}
		fail();
	}

	@Test
	public void test2() {
		try {
			JustTemplate.render("#{if}#{endif}", false);
		}
		catch (TemplateException e) {
			assertEquals(ErrorString.unexpectedCharacterAfter('}', "if", 1, 5), e.getMessage());
			return;
		}
		fail();
	}

	@Test
	public void test3() {
		try {
			JustTemplate.render("#{}#{endif}", false);
		}
		catch (TemplateException e) {
			assertEquals(ErrorString.unexpectedCharacterAfter('}', "{", 1, 3), e.getMessage());
			return;
		}
		fail();
	}

	@Test
	public void test9() {
		try {
			JustTemplate.render("${}", false);
		}
		catch (TemplateException e) {
			assertEquals(ErrorString.unexpectedCharacterAfter('}', "{", 1, 3), e.getMessage());
			return;
		}
		fail();
	}

	@Test
	public void test4() {
		try {
			JustTemplate.render("#{else}", false);
		}
		catch (TemplateException e) {
			assertEquals(ErrorString.unmatchedElse(1, 3), e.getMessage());
			return;
		}
		fail();
	}

	@Test
	public void test5() {
		try {
			JustTemplate.render("#{endif}", false);
		}
		catch (TemplateException e) {
			assertEquals(ErrorString.unmatchedEndIf(1, 3), e.getMessage());
			return;
		}
		fail();
	}

	@Test
	public void test6() {
		try {
			JustTemplate.render("#{elseif .}#{endif}", false);
		}
		catch (TemplateException e) {
			assertEquals(ErrorString.unmatchedElseIf(1, 3), e.getMessage());
			return;
		}
		fail();
	}

	@Test
	public void test7() {
		try {
			JustTemplate.render("#{elseif ", false);
		}
		catch (TemplateException e) {
			assertEquals(ErrorString.unexpectedEOF(1), e.getMessage());
			return;
		}
		fail();
	}

	@Test
	public void test8() {
		try {
			JustTemplate.render("#{if .}#{else}#{else}#{endif}", false);
		}
		catch (TemplateException e) {
			assertEquals(ErrorString.unmatchedElse(1, 17), e.getMessage());
			return;
		}
		fail();
	}

	private static class TestClass1{
		String val;
		public TestClass1(int val) {
			this.val = ""+val+val+val;
		}
	}
	
}
