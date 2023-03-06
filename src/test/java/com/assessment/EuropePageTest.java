package com.assessment;

import org.junit.Test;

public class EuropePageTest extends BaseTest {

    @Test
    public void sortByPricePerDayAsc() {
        try {
            EuropeToursPage europeToursPage = new EuropeToursPage();
            europeToursPage.navigate();
            europeToursPage.sortByCriteria("Price/day: Lowest first");
            europeToursPage.isSortedCorrectly("Price/day: Lowest first");
        } catch (Exception e) {
            ExtentReport.reportError(e);
            ExtentReport.flushReport();
            throw e;
        }
    }


    @Test
    public void filterByAgeRange() {
        try {
            EuropeToursPage europeToursPage = new EuropeToursPage();
            europeToursPage.navigate();
            europeToursPage.filterByAgeRange("18 to 39");
        } catch (Exception e) {
            ExtentReport.reportError(e);
            ExtentReport.flushReport();
            throw e;
        }
    }

    @Test
    public void downloadBrochure() {
        try {
            EuropeToursPage europeToursPage = new EuropeToursPage();
            europeToursPage.navigate();
            String email = String.valueOf(System.getProperty("Email"));
            if(isNotEmptyOrNull("Email", email)) {
                europeToursPage.downloadBrochure(email);
            }

        } catch (Exception e) {
            ExtentReport.reportError(e);
            ExtentReport.flushReport();
            throw e;
        }
    }

    private boolean isNotEmptyOrNull(String argName, String argValue) {
        if(String.valueOf(argValue).equalsIgnoreCase("null")) {
            throw new CustomException("no value is passed argument :'" + argName + "'. please provide value");
        }

        if(argValue.isEmpty()) {
            throw new CustomException("Value for argument :'" + argName + "' should not be blank. please provide correct value");
        }
        return true;
    }
}
