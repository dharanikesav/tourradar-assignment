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
            europeToursPage.downloadBrochure("dharanikesav@gmail.com");
        } catch (Exception e) {
            ExtentReport.reportError(e);
            ExtentReport.flushReport();
            throw e;
        }
    }
}
