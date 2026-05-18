//package com.orengrhrm.tests;
//
//import com.orangehrm.base.BaseTest;
//import com.orangehrm.keywords.KeywordEngine;
//import com.orangehrm.pages.DashboardPage;import org.testng.annotations.Test;
//
//import com.orangehrm.pages.LoginPage;
//import com.orangehrm.pages.PIMPage;
//import com.orangehrm.utils.ConfigReader;
//import com.orangehrm.utils.ExcelReader;
//import org.testng.Assert;
//import org.testng.annotations.DataProvider;
//
//
///**
// * HYBRID FRAMEWORK demo.
// *
// *   POM             -> page classes encapsulate locators + actions
// *   DATA-DRIVEN     -> @DataProvider feeds parameters
// *   KEYWORD-DRIVEN  -> Excel-defined keyword flow runs alongside
// *
// * Combine the strengths: business-readable keyword flows for repetitive
// * actions, POM for complex assertions, data-driven for coverage.
// */
//public class HybridTest extends BaseTest {
//
//    @DataProvider(name = "employees")
//    public Object[][] employees() {
//        return ExcelReader.getSheetData(
//                "src/test/resources/testdata/LoginData.xlsx", "Employees");
//    }
//
//    @Test(dataProvider = "employees",
//            description = "POM + Data-Driven combined hybrid scenario")
//    public void testEmployeeSearchHybrid(String empId, String expected) {
//        // POM phase
//        DashboardPage dash = new LoginPage(driver)
//                .loginAs(ConfigReader.get("username"), ConfigReader.get("password"));
//        PIMPage pim = dash.navigateToPIM();
//
//        boolean found = pim.findEmployeeAcrossAllPages(empId);
//        Assert.assertEquals(String.valueOf(found), expected,
//                "Mismatch for empId: " + empId);
//    }
//
//    @Test(description = "Hybrid: keyword-driven login + POM-driven assertions")
//    public void testKeywordLoginThenPomAssertion() {
//        KeywordEngine engine = new KeywordEngine(driver);
//        engine.execute("type",  "name=username", ConfigReader.get("username"));
//        engine.execute("type",  "name=password", ConfigReader.get("password"));
//        engine.execute("click", "css=button[type='submit']", "");
//
//        // Hand off to POM for richer post-conditions
//        DashboardPage dash = new DashboardPage(driver);
//        boolean isLoaded = dash.isPageLoaded();
//
//       // Assert.assertTrue(dash.isPageLoaded(), "Dashboard not visible");
//
//       // DashboardPage dash = new DashboardPage(driver);
//        //boolean isLoaded = dash.isPageLoaded();
//
//        try {
//            // 1. Assert first. If this fails, it jumps straight to the catch block.
//            Assert.assertTrue(isLoaded, "Dashboard not visible");
//
//            // 2. If it didn't fail, log the pass status.
//            test.pass("Dashboard loaded successfully.");
//
//        } catch (AssertionError e) {
//            // 3. Log the failure and the exact error message to Extent Reports
//            test.fail("Assertion Failed: " + e.getMessage());
//
//            // 4. CRITICAL: Re-throw the error so TestNG knows the test actually failed!
//            throw e;
//        }
//    }
//}
package com.orangerhrm.tests;

import com.orangehrm.base.BaseTest;
import com.orangehrm.keywords.KeywordEngine;
import com.orangehrm.pages.DashboardPage;
import com.orangehrm.pages.LoginPage;
import com.orangehrm.pages.PIMPage;
import com.orangehrm.utils.ConfigReader;
import com.orangehrm.utils.ExcelReader;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static com.orangehrm.base.BasePage.extent;
import static com.orangehrm.base.BasePage.test;

/**
 * HYBRID FRAMEWORK demo.
 *
 *   POM             -> page classes encapsulate locators + actions
 *   DATA-DRIVEN     -> @DataProvider feeds parameters
 *   KEYWORD-DRIVEN  -> Excel-defined keyword flow runs alongside
 */
public class HybridTest extends BaseTest {

    @DataProvider(name = "employees")
    public Object[][] employees() {
        return ExcelReader.getSheetData(
                "src/test/resources/testdata/LoginData.xlsx", "Employees");
    }

    @Test(dataProvider = "employees", description = "POM + Data-Driven combined hybrid scenario")
    public void testEmployeeSearchHybrid(String empId, String expected) {
        // Create an entry in Extent Report for this data iteration
        test = extent.createTest("testEmployeeSearchHybrid - EmpID: " + empId);

        try {
            DashboardPage dash = new LoginPage(driver)
                    .loginAs(ConfigReader.get("username"), ConfigReader.get("password"));
            PIMPage pim = dash.navigateToPIM();

            boolean found = pim.findEmployeeAcrossAllPages(empId);

            Assert.assertEquals(String.valueOf(found), expected, "Mismatch for empId: " + empId);
            test.pass("Successfully verified employee search status for ID: " + empId);

        } catch (AssertionError e) {
            test.fail("Assertion failed for EmpID " + empId + ": " + e.getMessage());
            throw e;
        } catch (Exception e) {
            test.fail("Test broke due to an unexpected exception: " + e.getMessage());
            throw e;
        }
    }

    @Test(description = "Hybrid: keyword-driven login + POM-driven assertions")
    public void testKeywordLoginThenPomAssertion() {
        // 1. Initialize Extent Test Node
        test = extent.createTest("testKeywordLoginThenPomAssertion", "Testing hybrid keyword login");

        // 2. Execute Keyword Driven Actions
        KeywordEngine engine = new KeywordEngine(driver);
        test.info("Executing keyword-driven login steps...");
        engine.execute("type",  "name=username", ConfigReader.get("username"));
        engine.execute("type",  "name=password", ConfigReader.get("password"));
        engine.execute("click", "css=button[type='submit']", "");

        // 3. Hand off to Page Object Model (POM) for assertions
        DashboardPage dash = new DashboardPage(driver);
        boolean isLoaded = dash.isPageLoaded();

        // 4. Robust Try-Catch Reporting Flow
        try {
            Assert.assertTrue(isLoaded, "Dashboard page failed to load.");
            test.pass("Dashboard loaded successfully."); // Logs to HTML report if step above passes

        } catch (AssertionError e) {
            test.fail("Assertion Failed: " + e.getMessage()); // Logs failure to HTML report
            throw e; // Forces TestNG to mark this test as "FAILED"
        }
    }
}
