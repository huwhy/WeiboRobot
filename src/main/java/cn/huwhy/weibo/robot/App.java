package cn.huwhy.weibo.robot;

import cn.huwhy.weibo.robot.view.MainWindow;
import org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.swing.*;

public class App {

    private static Logger LOGGER = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        try {
            //设置窗口边框样式
            BeautyEyeLNFHelper.frameBorderStyle = BeautyEyeLNFHelper.FrameBorderStyle.translucencyAppleLike;
            BeautyEyeLNFHelper.launchBeautyEyeLNF();
            //隐藏设置按钮
            UIManager.put("RootPane.setupButtonVisible", false);
            AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
            context.registerShutdownHook();
//            new LoginJFrame(context);
            new MainWindow();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
