package com.zensar.automation.executors;

import org.testng.TestNG;

public class OBATRegressionRunner {

	public static void main(String[] args) {

		TestNG testng = new TestNG();
		 testng.setTestClasses(new Class [] {OBATRegressionSuiteGTP.class});
		 testng.run();
	}

}
