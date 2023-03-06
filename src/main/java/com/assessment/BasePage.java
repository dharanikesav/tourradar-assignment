package com.assessment;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.time.Duration;
import java.util.List;


public class BasePage extends BaseTest {
    private static final WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    protected static final String loadingCircle = "//div[@class='js-serp-tour-list list pending']";


    protected By getByXpath(String path) {
        return By.xpath(path);
    }

    protected void clickElement(String xPath, String elementName) {
        try {
            waitUntilVisible(xPath);
            try {
                waitUntilClickable(xPath).click();
            } catch(StaleElementReferenceException stale) {
                waitUntilClickable(xPath).click();
            }

            ExtentReport.node.info("Successfully clicked element : " + elementName);

        } catch (Exception e) {
            e.printStackTrace();
            ExtentReport.node.fail("Unable to click element with xpath : " + xPath);
            throw e;
        }

    }

    protected void setValue(String xPath, String value, String elementName) {
        try {
            WebElement element = waitUntilClickable(xPath);
            element.clear();
            element.sendKeys(Keys.BACK_SPACE);
            element.sendKeys(value);

            ExtentReport.node.info("Successfully set value of element : '" + elementName + "' to " + value);
        } catch (Exception e) {
            ExtentReport.reportError(e);
            throw e;
        }
    }
    protected void selectOptionWithText(String xPath, String optionText, String elementName){
        try {
            WebElement element = driver.findElement(getByXpath(xPath));
            Select selectList = new Select(element);
            selectList.selectByVisibleText(optionText);
            ExtentReport.node.info("Option : " + optionText + " is selected successfully");
        } catch (Exception e) {
            ExtentReport.reportError(e);
            throw e;
        }
    }

    protected void scrollIntoView(WebElement element) {
        ((JavascriptExecutor)driver).executeScript("arguments[0].scrollIntoView(true);", element);
    }

    protected WebElement waitUntilClickable(String xPath) {
        return wait.until(ExpectedConditions.elementToBeClickable(getByXpath(xPath)));
    }

    protected void waitUntilVisible(String xPath) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(getByXpath(xPath)));
    }

    protected void waitUntilInvisible(String xPath) {
        wait.until(ExpectedConditions.invisibilityOfElementLocated(getByXpath(xPath)));
    }
    protected String getAttributeValue(String xPath, String attributeName) {
        try {
            WebElement element = driver.findElement(getByXpath(xPath));

            if(!element.getTagName().equalsIgnoreCase("Select")) {
                return element.getAttribute(attributeName).strip();
            } else {
                Select selectList = new Select(element);
                return selectList.getFirstSelectedOption().getAttribute(attributeName).strip();
            }
        } catch (Exception e) {
            ExtentReport.reportError(e);
            throw e;
        }
    }

    protected List<WebElement> getElements(String xpath) {
        return driver.findElements(getByXpath(xpath));
    }

    protected void validateAttributeValue(AttributeValidationType type, String xpath, String attributeName, String attributeValue, String elementName) {
        boolean validationSuccess = false;
        String actualValue = "";

        switch (type) {
            case Equals:
                actualValue = getAttributeValue(xpath, attributeName);
                validationSuccess = actualValue.replaceAll("\u00A0","").equalsIgnoreCase(attributeValue);
                break;

            case Contains:
                actualValue = getAttributeValue(xpath, attributeName);
                validationSuccess = actualValue.contains(attributeValue);
                break;
        }

        String message = elementName + ", Attribute : " + attributeName + ", actual value : " + actualValue + ", expected value : " + attributeValue;

        if (validationSuccess) {
            try {
                ExtentReport.node.pass("Validation '" + type + "'  successful. element: " + message, addBase64ScreenShotToReport());
            } catch(IOException e) {
                e.printStackTrace();
            }

        } else {
            ExtentReport.reportError(new CustomException("Validation '" + type + "'  failed. element: " + message));
            throw new CustomException("Validation '" + type + "'  failed. element: " + message);
        }
    }

}
