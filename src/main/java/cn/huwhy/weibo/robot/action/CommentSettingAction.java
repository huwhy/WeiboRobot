package cn.huwhy.weibo.robot.action;

import cn.huwhy.weibo.robot.model.MemberConfig;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class CommentSettingAction {

    private static Logger logger = LoggerFactory.getLogger(CommentSettingAction.class);

    public MemberConfig memberConfig;
    private WebDriver driver;
    private volatile boolean running = false;
    public CommentSettingAction() {

    }

    public void run() {
        if (running) return;
        running = true;
        try {
            WebElement el = driver.findElement(By.cssSelector(".gn_set .gn_set_list:nth-child(2)"));
            ActionUtil.moveToEl(driver, ".gn_set .gn_set_list a[node-type=account] em");
            ActionUtil.click(el, ".gn_topmenulist_set ul li:nth-child(1)");
        } finally {
            running = false;
        }
    }

    public void setMemberConfig(MemberConfig memberConfig) {
        this.memberConfig = memberConfig;
    }

    public void setDriver(WebDriver driver) {
        this.driver = driver;
    }
}