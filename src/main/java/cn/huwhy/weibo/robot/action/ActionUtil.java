package cn.huwhy.weibo.robot.action;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

public class ActionUtil {

    public static void click(WebDriver driver, String cssSelector) {
        while (true) {
            driver.findElement(By.cssSelector(cssSelector)).click();
            break;
        }
    }

    public static void click(WebElement element, String cssSelector) {
        while (true) {
            element.findElement(By.cssSelector(cssSelector)).click();
            break;
        }
    }

    public static void moveToEl(WebDriver driver, String cssSelector) {
        while (true) {
            new Actions(driver).moveToElement(driver.findElement(By.cssSelector(cssSelector))).perform();
            break;
        }
    }

    public static void moveToEl(WebDriver driver, WebElement element) {
        while (true) {
            new Actions(driver).moveToElement(element).perform();
            break;
        }
    }
}
