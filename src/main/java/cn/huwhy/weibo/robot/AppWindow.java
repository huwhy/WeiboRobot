package cn.huwhy.weibo.robot;

import javafx.application.Application;
import javafx.stage.Stage;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class AppWindow extends Application {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        context.registerShutdownHook();
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("战狼微博舆情监测系统V1.0!");
        AppContext.setMainState(primaryStage);
        AppContext.showLogin();
        primaryStage.show();
    }


}
