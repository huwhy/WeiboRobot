package cn.huwhy.weibo.robot.view;

import javax.swing.*;
import java.awt.*;

public class ImagePanel extends JPanel {
    Image im;

    public ImagePanel(Image im) {
        this.im = im;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setSize(screenSize.width, screenSize.height);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(im, 0, 0, this.getWidth(), this.getHeight(), this);
    }
}
