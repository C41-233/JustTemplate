package test.c41.template;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

import c41.template.JustTemplate;
import c41.template.resolver.ResolveException;

public class TestException {

	@Test
	public void test1() {
		try {
			JustTemplate.render("#{ip}#{endif}", false);
		}
		catch (ResolveException e) {
			assertEquals("unrecognized logic word 'ip' in line 1 column 3", e.getMessage());
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
			assertEquals("unexpected character '}' after 'if' in line 1 column 5", e.getMessage());
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
			assertEquals("empty logic block in line 1 column 2", e.getMessage());
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
			assertEquals("else without match if or elseif in line 1 column 3", e.getMessage());
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
			assertEquals("endif without match if in line 1 column 3", e.getMessage());
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
			assertEquals("elseif without match if in line 1 column 3", e.getMessage());
			return;
		}
		fail();
	}

}
