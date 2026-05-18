package com.orangehrm.pages;

import com.orangehrm.base.BasePage;
import com.orangehrm.exceptions.FrameworkException;
import com.orangehrm.interfaces.IPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

public class AdminPage extends BasePage implements IPage {

    // ==================== LOCATORS ====================

    private final By userRoleDropdown =
            By.xpath("//label[text()='User Role']/../following-sibling::div//div[contains(@class,'oxd-select-text-input')]");

    private final By statusDropdown =
            By.xpath("//label[text()='Status']/../following-sibling::div//div[contains(@class,'oxd-select-text-input')]");

    private final By dropdownOptions =
            By.cssSelector(".oxd-select-dropdown .oxd-select-option");

    private final By searchBtn =
            By.xpath("//button[normalize-space()='Search']");

    private final By recordsFoundLabel =
            By.cssSelector(".orangehrm-horizontal-padding span");

    // ==================== CONSTRUCTOR ====================

    public AdminPage(WebDriver driver) {
        super(driver);
    }

    // ==================== CUSTOM DROPDOWN ====================

    public void selectFromCustomDropdown(By dropdown, String optionText) {

        click(dropdown);

        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(dropdownOptions));

        List<WebElement> options = getElements(dropdownOptions);

        for (WebElement option : options) {

            if (option.getText().trim().equalsIgnoreCase(optionText.trim())) {

                option.click();

                wait.until(ExpectedConditions
                        .textToBePresentInElementLocated(dropdown, optionText));

                return;
            }
        }

        throw new FrameworkException("Dropdown option not found: " + optionText);
    }

    // ==================== ACTIONS ====================

    public void searchUser(String role, String status) {

        selectFromCustomDropdown(userRoleDropdown, role);

        selectFromCustomDropdown(statusDropdown, status);

        click(searchBtn);
    }

    public String getRecordsFoundText() {
        return getText(recordsFoundLabel);
    }

    // ==================== OVERRIDES ====================

    @Override
    public boolean isPageLoaded() {
        return isDisplayed(searchBtn);
    }

    @Override
    public String getPageTitle() {
        return driver.getTitle();
    }

    @Override
    public String getPageUrl() {
        return driver.getCurrentUrl();
    }

    @Override
    public boolean isAt() {
        return isPageLoaded();
    }
}