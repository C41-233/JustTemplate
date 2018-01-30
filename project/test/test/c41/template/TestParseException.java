package test.c41.template;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

import c41.template.JustTemplate;
import c41.template.internal.util.ErrorString;
import c41.template.resolver.ResolveException;

public class TestParseException {

	@Test
	public void test1() {
		try {
			JustTemplate.render("#{ip}#{endif}", false);
		}
		catch (ResolveException e) {
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
		catch (ResolveException e) {
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
		catch (ResolveException e) {
			assertEquals(ErrorString.emptyLogicWord(1, 2), e.getMessage());
			return;
		}
		fail();
	}

	@Test
	public void test4() {
		try {
			JustTemplate.render("#{else}", false);
		}
		catch (ResolveException e) {
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
		catch (ResolveException e) {
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
		catch (ResolveException e) {
			assertEquals(ErrorString.unmatchedElseIf(1, 3), e.getMessage());
			return;
		}
		fail();
	}

}
