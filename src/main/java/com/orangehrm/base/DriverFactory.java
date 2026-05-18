package com.orangehrm.base;

import com.orangehrm.exceptions.FrameworkException;
import com.orangehrm.utils.ConfigReader;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.edge.EdgeDriver;

import java.time.Duration;

/**
 * Thread-safe driver factory.
 *
 * MULTI-THREADING concepts demonstrated:
 *  - ThreadLocal<WebDriver>  -> each thread gets its own driver instance
 *  - synchronized methods    -> critical sections guarded
 *  - Static variables        -> shared across the JVM
 *
 * This enables parallel test execution (TestNG parallel="methods")
 * without browser sessions stepping on each other.
 */
public final class DriverFactory {

    // Each thread gets its own WebDriver -> the core of parallel-safe Selenium
    private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();

    // Private constructor - utility class, no instances allowed
    private DriverFactory() {
        throw new UnsupportedOperationException("Utility class - cannot instantiate");
    }

    /**
     * 'synchronized' ensures only one thread initialises a browser at a time.
     * Without it, parallel WebDriverManager calls can race on driver downloads.
     */
    public static synchronized WebDriver initDriver(String browser) {
        WebDriver driver;
        switch (browser.toLowerCase()) {
            case "chrome" -> {
                WebDriverManager.chromedriver().setup();
                ChromeOptions co = new ChromeOptions();
                if (ConfigReader.getBoolean("headless")) co.addArguments("--headless=new");
                co.addArguments("--remote-allow-origins=*", "--disable-notifications");
                driver = new ChromeDriver(co);
            }
            case "firefox" -> {
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions fo = new FirefoxOptions();
                if (ConfigReader.getBoolean("headless")) fo.addArguments("--headless=new");
                driver = new FirefoxDriver(fo);
            }
            case "edge" -> {
                WebDriverManager.edgedriver().setup();
                EdgeOptions eo = new EdgeOptions();
                if(ConfigReader.getBoolean("headless")) eo.addArguments("--headless=new");
                driver = new EdgeDriver(eo);
            }
            default -> throw new FrameworkException("Unsupported browser: " + browser);
        }

        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(
                Duration.ofSeconds(ConfigReader.getInt("implicit.wait")));

        DRIVER.set(driver);
        return driver;
    }

    public static WebDriver getDriver() {
        WebDriver d = DRIVER.get();
        if (d == null) {
            throw new FrameworkException(
                    "Driver not initialised for thread: " + Thread.currentThread().getName());
        }
        return d;
    }

    public static synchronized void quitDriver() {
        WebDriver d = DRIVER.get();
        if (d != null) {
            d.quit();
            DRIVER.remove();   // critical -> prevents memory leaks across thread reuse
        }
    }
}
