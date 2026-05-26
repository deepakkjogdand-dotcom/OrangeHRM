

package com.orangehrm.base;

import com.orangehrm.utils.ConfigReader;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.*;

public class BaseTest {

   protected WebDriver driver;

  @Parameters({"browser"})
    @BeforeMethod(alwaysRun = true)
    public void setUp(@Optional("") String browserParam) {

        String browser = browserParam.isBlank()
                ? ConfigReader.get("browser")
                : browserParam;

        driver = DriverFactory.initDriver(browser);
        driver.get(ConfigReader.get("url"));
    }

   @AfterMethod(alwaysRun = true)
    public void tearDown() {
        DriverFactory.quitDriver();
    }
}