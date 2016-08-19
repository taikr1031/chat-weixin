package com.zm.mondo;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
		UserInitDB.class,
		ChatInitDB.class,
		MessageInitDB.class
})

public class TestSuite {
}