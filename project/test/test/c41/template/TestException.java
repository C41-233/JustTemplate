package test.c41.template;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

import c41.template.JustTemplate;
import c41.template.resolver.ResolveException;

public class TestException {

	@Test
	public void test() {
		try {
			JustTemplate.render("#{ip}#{endif}", false);
		}
		catch (ResolveException e) {
			assertEquals("line 1 column 3", e.getMessage());
			return;
		}
		fail();
	}

}
