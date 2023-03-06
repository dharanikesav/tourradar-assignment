package com.assessment;

import org.openqa.selenium.WebElement;

import java.util.List;

public class EuropeToursPage extends BasePage {
    private static final String sortCriteriaSelect = "//select[@aria-label='Sort by filter']";
    private static final String pricePerDayText = "//dt[contains(text(),'Price per day')]/following-sibling::dd";
    private static final String ageRangeText = "//dt[contains(text(),'Age Range')]/following-sibling::dd[1]";
    private static final String ageRangeFilterCriteria = "//ul[@data-cy='serp-filters--age-range-list']/li[@data-pid='AGE_RANGE_FILTER_CRITERIA']";
    private static final String downloadBrochureButton = "//button[@data-cy='serp-tour--download-brochure']";
    private static final String emailInput = "//input[@data-cy='common-download-brochure--email-input']";
    private static final String downloadBrochureDialogButton = "//a[@data-cy='common-download-brochure--submit']";
    private static final String downloadSuccessText = "//div[@id='callback_popup']//h3";
    private static final String closeDownloadBrochureDialog = "//div[@id='callback_popup']//button[@title='Close (Esc)']";

    public EuropeToursPage() {
        ExtentReport.createAndGetNodeInstance("Europe Tours Page");
    }

    public void navigate() {
        driver.get("https://www.tourradar.com/d/europe");
    }

    public void sortByCriteria(String criteria) {
        ExtentReport.createAndGetNodeInstance("Sort Page");
        selectOptionWithText(sortCriteriaSelect, criteria, "Sort Criteria");
        waitUntilInvisible(loadingCircle);
    }

    public void isSortedCorrectly(String criteria) {
        ExtentReport.createAndGetNodeInstance("Validating Sorted Page");
        String elemntToBeValidated;
        switch (criteria.toLowerCase()) {
            case "price/day: lowest first", "price/day: highest first" -> elemntToBeValidated = pricePerDayText;
            case "total price: lowest first", "total price: highest first" -> elemntToBeValidated = pricePerDayText;
            default -> elemntToBeValidated = pricePerDayText;
        }

        List<WebElement> elements = getElements(elemntToBeValidated);
        for(int index=0; index < elements.size(); index++) {
            if(index != 0) {
                String previousValue = elements.get(index - 1).getAttribute("innerText").substring(1);
                String currentValue = elements.get(index).getAttribute("innerText").substring(1);
                if(criteria.toLowerCase().contains("lowest") || criteria.toLowerCase().contains("shortest")) {
                    if(Integer.parseInt(previousValue) > Integer.parseInt(currentValue)) {
                        scrollIntoView(elements.get(index - 2));
                        throw new CustomException("List is not sorted correctly as per criteria: " + criteria +
                                ". Because price per day value of row number " + index + " is '" + previousValue + "' where as for row number: " +
                                (index + 1) + " is '" + currentValue + "'");
                    }
                }
            }
        }
        ExtentReport.node.info("Successfully Sorted by criteria : " + criteria);
    }

    public void filterByAgeRange(String criteria) {
        ExtentReport.createAndGetNodeInstance("Filter BY Age Range");
        String ageRangeFilterElement = "";
        String minAge = "";
        String maxAge = "";
        if(criteria.contains("18")) {
            ageRangeFilterElement = ageRangeFilterCriteria.replaceAll("AGE_RANGE_FILTER_CRITERIA", "18-39");
            minAge = "18";
            maxAge = "39";
        }
        clickElement(ageRangeFilterElement, "Age Range Filter Criteria");
        waitUntilInvisible(loadingCircle);

        List<WebElement> elements = getElements(ageRangeText);
        for (int index=0; index<elements.size(); index++) {
            String ageRangeTextValue = elements.get(index).getAttribute("innerText");
            ageRangeTextValue = ageRangeTextValue.replaceAll(" year olds", "").trim();
            String currentMaxAge = ageRangeTextValue.split(" to ")[1];
            String currentMinAge = ageRangeTextValue.split(" to ")[0];
            if(
                    Integer.parseInt(currentMaxAge) < Integer.parseInt(minAge) ||
                            Integer.parseInt(currentMinAge) > Integer.parseInt(maxAge)
            ) {
                scrollIntoView(elements.get(index-1));
                throw new CustomException("List is not filtered correctly as per agerange criteria: " + criteria +
                        ". Because age range value of row number " + index + " is '" + ageRangeTextValue);
            }
        }
        ExtentReport.node.info("Successfully Filtered by Age Range criteria : " + criteria);
    }

    public void downloadBrochure(String email) {
        ExtentReport.createAndGetNodeInstance("Download Brochure");
        clickElement(downloadBrochureButton, "Download Brochure Button");
        waitUntilClickable(emailInput);
        setValue(emailInput, email, "Email field");
        clickElement(downloadBrochureDialogButton, "Download Brochure Dialog Button");
        waitUntilInvisible(emailInput);
        validateAttributeValue(
                AttributeValidationType.Equals,
                downloadSuccessText,
                "innerText",
                "Brochure successfully sent!",
                "Download Brochure Success Message"
        );
        clickElement(closeDownloadBrochureDialog, "Close Download Brochure Dialog");

    }
}
