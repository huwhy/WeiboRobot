package cn.huwhy.weibo.robot.view;

import cn.huwhy.weibo.robot.model.Member;
import cn.huwhy.weibo.robot.service.MemberService;
import cn.huwhy.weibo.robot.util.MyFont;
import cn.huwhy.weibo.robot.util.ResourcesUtil;
import cn.huwhy.weibo.robot.util.SpringContentUtil;
import org.jb2011.lnf.beautyeye.ch3_button.BEButtonUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.Arrays;

public class LoginJFrame extends JFrame implements MouseListener, FocusListener {

    private static Logger logger = LoggerFactory.getLogger(LoginJFrame.class);

    JTextField username = new JTextField(20);
    JPasswordField password = new JPasswordField(20);
    ImagePanel backgroundPanel = null;
    JButton btnLogin, btnReset;
    private ApplicationContext context;

    public LoginJFrame(ApplicationContext context) {
        this.context = context;
        Image backgroundImage = null;
        try {
            backgroundImage = ImageIO.read(ResourcesUtil.getImage("loginbackground.png"));
            Image image = ImageIO.read(ResourcesUtil.getImage("logo.png"));
            setIconImage(image);
        } catch (IOException e) {
            e.printStackTrace();
        }

        backgroundPanel = new ImagePanel(backgroundImage);
        backgroundPanel.setLayout(null);
        username.setBounds(230, 197, 173, 30);
        username.setFont(MyFont.Static);
        username.addFocusListener(this);
        username.setToolTipText("用户名/账号");
        username.setText("");

        password.setBounds(230, 260, 173, 30);
        password.setFont(MyFont.Static);
        password.addFocusListener(this);
        password.setToolTipText("密码");
        password.setEchoChar('*');

        btnLogin = new JButton("登录");
        btnLogin.setBounds(230, 320, 70, 27);
        btnLogin.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.lightBlue));
        btnLogin.setForeground(Color.white);
        btnLogin.setFont(MyFont.Static);
        btnLogin.addMouseListener(this);

        btnReset = new JButton("重置");
        btnReset.setBounds(330, 320, 70, 27);
        btnReset.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.red));
        btnReset.setForeground(Color.white);
        btnReset.setFont(MyFont.Static);
        btnReset.addMouseListener(this);

        backgroundPanel.add(username);
        backgroundPanel.add(password);
        backgroundPanel.add(btnLogin);
        backgroundPanel.add(btnReset);

        this.add(backgroundPanel);
        this.setTitle(" 微博舆情系统");
        this.setSize(830, 530);
        this.setVisible(true);
        this.requestFocus();
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setResizable(false);
    }

    public void focusGained(FocusEvent e) {
//        if (e.getSource() == username) {
//            if (username.getText().equals("用户名/帐号")) {
//                username.setText("");
//            }
//        } else if (e.getSource() == password) {
//            if (password.getText().equals("密码")) {
//                password.setText("");
//                password.setEchoChar('*');
//            }
//        }
    }

    public void focusLost(FocusEvent e) {
//        if (e.getSource() == username) {
//            if (username.getText().equals("")) {
////                username.setText("用户名/帐号");
//            }
//        } else if (e.getSource() == password) {
//            if (password.getText().equals("")) {
//                password.setText("密码");
//                password.setEchoChar('\0');
//            }
//        }
    }

    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == btnLogin) {
            if ("".equals(username.getText())) {
                JOptionPane.showMessageDialog(null, "用户名不能为空");
            } else if ("".equals(password.getText())) {
                JOptionPane.showMessageDialog(null, "密码不能为空");
            } else {
                logger.info("login...: {} - {}", username.getText(), password.getPassword());
                MemberService memberService = context.getBean(MemberService.class);
                Member member = memberService.getByName(username.getText());
                if (member != null && Arrays.equals(member.getPassword().toCharArray(), password.getPassword())) {
                    IndexJFrame indexJFrame = context.getBean(IndexJFrame.class);
                    indexJFrame.setMember(member);
                    indexJFrame.setVisible(true);
                    this.setVisible(false);
                } else {
                    JOptionPane.showMessageDialog(null, "用户密码错误");
                }
            }
        } else if (e.getSource() == btnReset) {
            username.setText("");
            password.setText("");
        }
    }

    public void mousePressed(MouseEvent e) {

    }

    public void mouseReleased(MouseEvent e) {

    }

    public void mouseEntered(MouseEvent e) {

    }

    public void mouseExited(MouseEvent e) {

    }
}
