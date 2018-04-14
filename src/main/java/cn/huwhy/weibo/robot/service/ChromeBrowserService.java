package cn.huwhy.weibo.robot.service;

import cn.huwhy.common.util.StringUtil;
import cn.huwhy.common.util.ThreadUtil;
import cn.huwhy.weibo.robot.model.Member;
import cn.huwhy.weibo.robot.util.ResourcesUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.service.DriverService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class ChromeBrowserService implements DisposableBean, InitializingBean {
    private Logger logger = LoggerFactory.getLogger(getClass());
    private DriverService service;
    private WebDriver driver;

    @Override
    public void destroy() throws Exception {
        service.stop();
        driver.quit();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        File driverFile = ResourcesUtil.getChromeDrivers();
        service = new ChromeDriverService.Builder()
                .usingDriverExecutable(driverFile)
                .usingAnyFreePort()
                .build();
        service.start();
    }

    private void initDriver() {
        if (this.driver != null) {
            try {
                this.driver.quit();
            } catch (Throwable ignore) {
                logger.warn("", ignore);
            } finally {
                this.driver = null;
            }
        }
        this.driver = new RemoteWebDriver(service.getUrl(),
                DesiredCapabilities.chrome());
    }

    public void login(Member member) {
        this.initDriver();
        this.driver.get("https://weibo.com/");
        ThreadUtil.sleep(1000);
        if (StringUtil.isNotEmpty(member.getWbName()) && StringUtil.isNotEmpty(member.getWbPassword())) {
            //得到浏览器的标题
            do {
                try {
                    driver.findElement(By.id("loginname")).sendKeys(member.getWbName());
                    driver.findElement(By.name("password")).sendKeys(member.getWbPassword());
                    driver.findElement(By.className("login_btn")).click();
                    break;
                } catch (Throwable err) {
                    ThreadUtil.sleep(1000);
                }
            } while (true);
        }
    }

    public WebDriver getDriver() {
        return driver;
    }
}
