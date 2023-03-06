package com.assessment;

import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.model.Media;
import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.rules.TestName;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class BaseTest implements ISystemProperties {

    protected static WebDriver driver;

    @Rule
    public TestName testName = new TestName();

    @Before
    public void beforeEachTest() {
        if(ExtentReport.test.getModel().getName().equalsIgnoreCase("test environment preparation")) {
            if(!ExtentReport.test.getModel().hasException()) {
                ExtentReport.removeTestFromReport(ExtentReport.test);
            }
        }

        ExtentReport.createTestInstance(testName.getMethodName());
    }

    @BeforeClass
    public static void beforeTestClass() {

        String driverName = "chromedriver.exe";
        if(OS.contains("Mac")) {
            driverName = "chromedriver";
        }

        ExtentReport.createReportInstance(currentDir + pathSeperator + "Reports" + pathSeperator + "Report.html");
        ExtentReport.createTestInstance("Test Environment Preparation");

        try {

            FileUtils.cleanDirectory(new File(currentDir + pathSeperator + "Reports" + pathSeperator));
            System.setProperty("webdriver.chrome.driver", currentDir + pathSeperator + "Drivers" + pathSeperator + driverName);

           // ChromeOptions options = new ChromeOptions();
           /* HashMap<String, Object> chromePrefs = new HashMap<>();
            chromePrefs.put("profile.default_content_setting_values.cookies", 0);
            chromePrefs.put("profile.cookie_controls_mode", 0);

            options.setExperimentalOption("prefs", chromePrefs);*/
            driver = new ChromeDriver();
            driver.manage().window().maximize();

        } catch(Exception e) {
            e.printStackTrace();
            ExtentReport.test.fail(e);
        }
    }


   @AfterClass
   public static void afterTestClass() {
        ExtentReport.flushReport();
        driver.quit();
    }

    public static String getScreenshotAsString() throws IOException {
        File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        byte[] fileContent = FileUtils.readFileToByteArray(src);
        return Base64.getEncoder().encodeToString(fileContent);
    }

    public static Media addBase64ScreenShotToReport() throws IOException {
        return MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotAsString()).build();
    }

}
