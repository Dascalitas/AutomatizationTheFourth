package com.dascalitas;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class parkingCalculator {

    @DataProvider
    public Object [] [] StringList() {
        return new Object[][] {
             {"Valet Parking", "12:00", "PM", "10/15/2018", "12:00", "PM", "10/16/2018", "$ 42.00", "(1 Days, 0 Hours, 0 Minutes)"},
             {"Economy Parking", "12:00", "PM", "10/15/2018", "12:00", "PM", "11/15/2018", "$ 243.00", "(31 Days, 0 Hours, 0 Minutes)"},
             {"Short-Term Parking", "12:00", "PM", "10/15/2018", "12:00", "PM", "10/15/2018", "$ 2.00", "(0 Days, 0 Hours, 0 Minutes)"},
             {"Economy Parking", "12:00", "AM", "10/15/2018", "12:00", "PM", "10/15/2018", "$ 9.00", "(0 Days, 12 Hours, 0 Minutes)"},
             {"Valet Parking", "12:00", "AM", "10/15/2018", "12:00", "PM", "10/15/2018", "$ 30.00", "(0 Days, 12 Hours, 0 Minutes)"}
        };
    }

    @Test(dataProvider = "StringList")
    public void test(String typeParking, String initTime, String initHalf, String initDate, String finTime, String finHalf, String finDate, String calCost, String calDate) throws InterruptedException {
        WebDriver driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
        driver.get("http://adam.goucher.ca/parkcalc/index.php");

        Assert.assertEquals(driver.getTitle(), "Parking Calculator");

//        WebElement chooseALot = driver.findElement(By.xpath("//tr[td[contains(text(),'Choose a Lot')]]//select[@id='Lot']"));
//        Select chooseALotSelect = new Select(chooseALot);

        Select chooseALotSelect = new Select(driver.findElement(By.xpath("//tr[td[contains(text(),'Choose a Lot')]]//select[@id='Lot']")));
        chooseALotSelect.selectByVisibleText(typeParking);

        WebElement startTime = driver.findElement(By.xpath("//tr[td[contains(text(),'Choose Entry Date and Time')]]//input[@name='EntryTime']"));
        List<WebElement> startDateAmPm = driver.findElements(By.xpath("//tr[td[contains(text(),'Choose Entry Date and Time')]]//input[@name='EntryTimeAMPM']"));
        WebElement startDate = driver.findElement(By.xpath("//tr[td[contains(text(),'Choose Entry Date and Time')]]//input[@name='EntryDate']"));

        startTime.clear();
        startTime.sendKeys(initTime);
        selectRadioValue(startDateAmPm, initHalf);
        startDate.clear();
        startDate.sendKeys(initDate);


        WebElement endTime = driver.findElement(By.xpath("//tr[td[contains(text(),'Choose Leaving Date and Time')]]//input[@name='ExitTime']"));
        List<WebElement> endDateAmPm = driver.findElements(By.xpath("//tr[td[contains(text(),'Choose Leaving Date and Time')]]//input[@name='ExitTimeAMPM']"));
        WebElement endDate = driver.findElement(By.xpath("//tr[td[contains(text(),'Choose Leaving Date and Time')]]//input[@name='ExitDate']"));


        endTime.clear();
        endTime.sendKeys(finTime);
        selectRadioValue(endDateAmPm, finHalf);
        endDate.clear();
        endDate.sendKeys(finDate);


        WebElement submitBtn = driver.findElement(By.xpath("//input[@name='Submit' and @value='Calculate']"));

        submitBtn.click();

        Thread.sleep(1000);

        WebElement costValue = driver.findElement(By.xpath("//tr[contains(.,'COST')]/td//b[contains(text(),'$')]"));
        WebElement calculatedTime = driver.findElement(By.xpath("//tr[contains(.,'COST')]/td//b[contains(text(),'Days')]"));

        Assert.assertEquals(costValue.getText(),calCost);
        Assert.assertEquals(calculatedTime.getText().trim(), calDate);

        driver.quit();
    }

    public void selectRadioValue(List<WebElement> list, String selectValue){
        for(WebElement elem:list){
            String paramValue = elem.getAttribute("value");
            if (StringUtils.equals(selectValue,paramValue)){
                elem.click();
                return;
            }
        }
    }
}