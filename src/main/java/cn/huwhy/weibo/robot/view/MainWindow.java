package cn.huwhy.weibo.robot.view;

import cn.huwhy.weibo.robot.model.Member;
import cn.huwhy.weibo.robot.util.ResourcesUtil;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class MainWindow extends JFrame {

    private JPanel loginPanel;
    private JTabbedPane mainPanel;

    public MainWindow() {

        UIManager.put("TabbedPane.tabAreaInsets", new javax.swing.plaf.InsetsUIResource(0, 0, 0, 0));
        try {
            Image image = ImageIO.read(ResourcesUtil.getImage("logo.png"));
            this.setIconImage(image);
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.setTitle(" 神盾微博舆情监测系统");
        this.setSize(1152, 720);


        showLogin();
        this.setLocationRelativeTo(null);    // 此窗口将置于屏幕的中央。
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    public void showLogin() {
        if (loginPanel == null) {
            loginPanel = new LoginPanel(this);
            this.add(loginPanel);
        }
        loginPanel.setEnabled(true);
        loginPanel.setVisible(true);
        if (mainPanel != null) {
            mainPanel.setEnabled(false);
            mainPanel.setVisible(false);
        }
    }

    public void showMain(Member member) {

        if (mainPanel == null) {
            mainPanel = new MainPanel(this, member);
            this.add(mainPanel);
        }
        mainPanel.setEnabled(true);
        mainPanel.setVisible(true);
        if (loginPanel != null) {
            loginPanel.setEnabled(false);
            loginPanel.setVisible(false);
        }
    }
}
