package org.hackatron.ocrsudoku.tests;
import junit.framework.TestCase;

public class OCRTests extends TestCase {

	public OCRTests(String name) {
		super(name);
	}

	public void testSimpleTest() {
		int answer = 2;
		assertEquals((1 + 1), answer);
	}
}
