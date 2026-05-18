package com.orangehrm.tests;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.orangehrm.base.DriverFactory;
import com.orangehrm.pages.ShadowDomPage;
import com.orangehrm.utils.ConfigReader;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.*;

/**
 * SHADOW DOM test.
 * Does not extend BaseTest because it points at a different URL than
 * OrangeHRM. Reuses DriverFactory + ShadowDomPage from the framework.
 */
public class ShadowDOMTest {
    ExtentReports extent;
    ExtentTest test;

    private WebDriver driver;
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

    @BeforeMethod
    public void setUp() {
        driver = DriverFactory.initDriver(ConfigReader.get("browser"));
        // selenium.dev uses shadow DOM in its components
        driver.get("https://www.selenium.dev/selenium/web/shadow_dom/");
    }

    @Test(description = "Pierce a shadow root via the BasePage helper")
    public void testReadShadowText() {
        test = extent.createTest("Shadow DOM Test");
        try {
            ShadowDomPage page = new ShadowDomPage(driver);
            // The selenium test fixture exposes a custom-checkbox-element
            // with an internal #content paragraph.
            WebElement target = page.findInShadowManual(
                    "#shadow_host",
                    "#shadow_content");
            Assert.assertTrue(target.isDisplayed(),
                    "Shadow DOM element should be visible");
            test.pass("Shadow DOM element found");
        }
        catch(Exception e)
        {
           test.fail(e);
           Assert.fail(e.getMessage());
        }
    }
    @AfterMethod
    public void tearDown() { DriverFactory.quitDriver(); }

    @AfterTest
    public void endReport() {
        extent.flush();
    }
}

