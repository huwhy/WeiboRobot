package cn.huwhy.weibo.robot;

public class App {

    public static void main(String[] args) {
        try {
            AppWindow.main(args);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
