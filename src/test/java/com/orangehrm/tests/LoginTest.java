/*package com.orengrhrm.tests;

import com.orangehrm.base.BaseTest;
import com.orangehrm.pages.DashboardPage;
import com.orangehrm.pages.LoginPage;
import com.orangehrm.utils.ConfigReader;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class LoginTest extends BaseTest {

    @Test(description = "Valid login leads to dashboard")
    public void testValidLogin() {
        LoginPage login = new LoginPage(driver);
        Assert.assertTrue(login.isPageLoaded(), "Login page did not load");

        DashboardPage dash = login.loginAs(
                ConfigReader.get("username"),
                ConfigReader.get("password"));

        Assert.assertTrue(dash.isAt(), "Dashboard URL not reached");
        Assert.assertEquals(dash.getHeaderText(), "Dashboard");
    }

    @DataProvider(name = "invalidCreds")
    public Object[][] badCreds() {
        return new Object[][]{
                {"WrongUser", "admin123", "Invalid credentials"},
                {"Admin", "wrongPass", "Invalid credentials"},
                {"", "", "Required"}
        };
    }

    @Test(dataProvider = "invalidCreds", description = "DATA-DRIVEN negative login")
    public void testInvalidLogin(String user, String pass, String expectedMsg) {
        LoginPage login = new LoginPage(driver);
        login.loginAs(user, pass);
        // For empty fields, error wording differs. Just verify we did not pass.
        Assert.assertTrue(login.isPageLoaded(), "Login should NOT have succeeded");
    }
}*/


package com.orangehrm.tests;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import com.orangehrm.base.BaseTest;
import com.orangehrm.pages.DashboardPage;
import com.orangehrm.pages.LoginPage;
import com.orangehrm.utils.ConfigReader;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.*;

public class LoginTest extends BaseTest {

    ExtentReports extent;
    ExtentTest test;

    @BeforeTest
    public void setupReport() {

        ExtentSparkReporter spark =
                new ExtentSparkReporter(
                        "test-output/LoginTestReport.html");

        spark.config().setReportName(
                "Login Test Report");

        spark.config().setDocumentTitle(
                "OrangeHRM Login Execution");

        extent = new ExtentReports();

        extent.attachReporter(spark);
    }

    @Test
    public void testValidLogin() {

        test = extent.createTest("Valid Login Test");

        LoginPage login = new LoginPage(driver);

        Assert.assertTrue(login.isPageLoaded());

        DashboardPage dash = login.loginAs(
                ConfigReader.get("username"),
                ConfigReader.get("password"));

        Assert.assertTrue(dash.isAt());

        test.pass("Login successful");
    }
    @DataProvider(name = "invalidCreds")
    public Object[][] badCreds() {

        return new Object[][]{
                {"WrongUser", "admin123", "Invalid credentials"},
                {"Admin", "wrongPass", "Invalid credentials"},
                {"Admin", "admin123", "Required"}
        };
    }

    @Test(
            dataProvider = "invalidCreds",
            description = "DATA-DRIVEN negative login"
    )
    public void testInvalidLogin(
            String user,
            String pass,
            String expectedMsg) {

        test = extent.createTest(
                "Invalid Login Test - " + user);

        LoginPage login = new LoginPage(driver);

        test.info("Trying invalid login");

        login.loginAs(user, pass);

        Assert.assertTrue(
                login.isPageLoaded(),
                "Login should NOT have succeeded");

        test.pass("Invalid login verified");
    }

    @AfterMethod
    public void tearDown(ITestResult result)
    {

        if (result.getStatus()
                == ITestResult.FAILURE) {

            String base64Screenshot =
                    captureScreenshot();

            test.fail(result.getThrowable());

            try {

                test.addScreenCaptureFromBase64String(
                        base64Screenshot,
                        "Failed Test Screenshot");

            } catch (Exception e) {

                e.printStackTrace();
            }
        }

        else if (result.getStatus()
                == ITestResult.SUCCESS) {

            test.pass("Test Passed");
        }

        else {

            test.skip("Test Skipped");
        }

        extent.flush();
    }

    public String captureScreenshot() {

        TakesScreenshot ts =
                (TakesScreenshot) driver;

        return ts.getScreenshotAs(
                OutputType.BASE64);
    }
}