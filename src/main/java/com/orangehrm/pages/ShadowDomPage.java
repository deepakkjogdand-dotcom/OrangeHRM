package com.orangehrm.pages;

import com.orangehrm.base.BasePage;
import com.orangehrm.exceptions.FrameworkException;
import com.orangehrm.interfaces.IPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import org.openqa.selenium.SearchContext;
/**
 * SHADOW DOM demonstration.
 *
 * OrangeHRM does not natively use shadow DOM, so for hands-on practice point
 * the test at one of these public pages:
 *   - https://www.selenium.dev (footer uses shadow components)
 *   - chrome://settings (multi-level shadow root - browser-internal)
 *   - https://books-pwakit.appspot.com/ (deep shadow trees)
 *
 * Selenium 4 introduces WebElement.getShadowRoot() returning a SearchContext.
 * The findInShadow(...) helper in BasePage walks N levels of shadow roots.
 */
public class ShadowDomPage extends BasePage implements IPage {

    public ShadowDomPage(WebDriver driver) { super(driver); }

    /**
     * Example: pierce one shadow root to reach an inner element.
     *   <my-host>          #shadow-root  <button id="primary">Click</button>  </my-host>
     */
    public void clickShadowButton(String hostCss, String innerCss) {
        WebElement button = findInShadow(hostCss, innerCss);
        button.click();
    }

    /** Example: multi-level shadow root piercing. */
    public String getDeepShadowText(String... selectorsTopDown) {
        return findInShadow(selectorsTopDown).getText();
    }

    /**
     * Manual variant - useful when you want to learn the underlying API.
     * Same outcome as findInShadow() but written explicitly.
     */
    public WebElement findInShadowManual(String hostCss, String innerCss) {
        WebElement host = driver.findElement(By.cssSelector(hostCss));
        return host.getShadowRoot().findElement(By.cssSelector(innerCss));
    }

    @Override public boolean isPageLoaded() { return driver.getTitle() != null; }
    @Override public String getPageTitle()  { return driver.getTitle(); }
    @Override public String getPageUrl()    { return driver.getCurrentUrl(); }
    @Override public boolean isAt()         { return true; }

    /**
     * Loads config.properties once (eagerly) and exposes typed getters.
     * Demonstrates: static init block, exception handling, encapsulation.
     */
    static final class ConfigReader {

        private static final Properties PROPS = new Properties();

        static {
            try (FileInputStream fis = new FileInputStream("src/test/resources/config.properties")) {
                PROPS.load(fis);
            } catch (IOException e) {
                throw new FrameworkException("Failed to load config.properties", e);
            }
        }

       // private ConfigReader() {}

        public static String get(String key) {
            String value = PROPS.getProperty(key);
            if (value == null) throw new FrameworkException("Missing key in config: " + key);
            return value;
        }

        public static int getInt(String key) {
            try {
                return Integer.parseInt(get(key));
            } catch (NumberFormatException e) {
                throw new FrameworkException("Property " + key + " is not an int", e);
            }
        }

        public static boolean getBoolean(String key)
        {
            return Boolean.parseBoolean(get(key));
        }
    }
}

