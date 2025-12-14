package com.example.companiezbor;

import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

@Suite
@SuiteDisplayName("All Spring Boot Tests")
@SelectPackages("com.example.companiezbor")
class CompanieZborApplicationTests {
    // This test suite will run all tests in the com.example.companiezbor package and subpackages
}
