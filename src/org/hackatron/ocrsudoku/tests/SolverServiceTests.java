package org.hackatron.ocrsudoku.tests;

import org.hackatron.ocrsudoku.SolverService;

import android.content.Intent;
import android.test.ServiceTestCase;
import android.test.suitebuilder.annotation.MediumTest;

public class SolverServiceTests extends ServiceTestCase<SolverService> {

	private Intent _startIntent;

	public SolverServiceTests() {
		super(SolverService.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		// In setUp, you can create any shared test data, or set up mock
		// components to inject into your Service. But do not call
		// startService() until the actual test methods.
		_startIntent = new Intent(Intent.ACTION_MAIN);
	}

	/**
	 * The name 'test preconditions' is a convention to signal that if this test
	 * doesn't pass, the test case was not set up properly and it might explain
	 * any and all failures in other tests. This is not guaranteed to run before
	 * other tests, as junit uses reflection to find the tests.
	 */
	@MediumTest
	public void testPreconditions() {
		startService(_startIntent);

		assertNotNull(getService());
	}
}
