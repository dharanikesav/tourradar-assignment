package com.assessment;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import java.io.IOException;

public class ExtentReport implements ISystemProperties {
    private static ExtentReports report;
    public static ExtentTest test;
    public static ExtentTest node;

    public static void createReportInstance(String filePath) {
        report = new ExtentReports();
        report.attachReporter(new ExtentSparkReporter(filePath));
        report.setSystemInfo("Host Name", machineName);
        report.setSystemInfo("Operating System", OS);
    }

    public static void createTestInstance(String testName) {
        test = report.createTest(testName);
    }

    public static ExtentTest createAndGetNodeInstance(String nodeName) {
        node = test.createNode(nodeName);
        return node;
    }

    public static void removeTestFromReport(ExtentTest testName) {
        report.removeTest(testName);
    }

    public static void flushReport() { report.flush();}

    public static void reportError(Exception e) {
        try {
            node.fail(e, BaseTest.addBase64ScreenShotToReport());
        } catch (IOException newError) {
            newError.printStackTrace();
        } catch (Exception nodeError) {
            nodeError.printStackTrace();
            test.fail(nodeError);
            throw nodeError;
        }

    }

}
