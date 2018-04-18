package cn.huwhy.weibo.robot.view;

import cn.huwhy.weibo.robot.model.Member;
import cn.huwhy.weibo.robot.service.MemberService;
import cn.huwhy.weibo.robot.util.MyFont;
import cn.huwhy.weibo.robot.util.ResourcesUtil;
import cn.huwhy.weibo.robot.util.SpringContentUtil;
import org.jb2011.lnf.beautyeye.ch3_button.BEButtonUI;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Arrays;

public class LoginPanel extends JPanel implements ActionListener {

    private MainWindow mainWindow;

    private JTextField txUsername;
    private JPasswordField txPassword;

    private MemberService memberService;

    public LoginPanel(MainWindow mainWindow) {
        super();
        setLayout(null);
        this.mainWindow = mainWindow;

        txUsername = new JTextField(20);
        txUsername.setBounds(320, 290, 173, 30);
        txUsername.setFont(MyFont.Static);
        txUsername.setToolTipText("用户名/帐号");
        txUsername.setText("");
        add(txUsername);

        txPassword = new JPasswordField(20);
        txPassword.setBounds(320, 380, 173, 30);
        txPassword.setFont(MyFont.Static);
        txPassword.setToolTipText("密码");
        txPassword.setEchoChar('*');
        add(txPassword);

        JButton btnLogin = new JButton("登录");
        btnLogin.setSize(100, 50);
        btnLogin.setBounds(320, 438, 70, 27);
        btnLogin.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.lightBlue));
        btnLogin.setForeground(Color.white);
        btnLogin.setFont(MyFont.Static);
        btnLogin.setActionCommand("login");
        btnLogin.addActionListener(this);
        add(btnLogin);

        JButton btnReset = new JButton("重置");
        btnReset.setSize(100, 50);
        btnReset.setBounds(458, 438, 70, 27);
        btnReset.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.red));
        btnReset.setForeground(Color.white);
        btnReset.setFont(MyFont.Static);
        btnReset.setActionCommand("reset");
        btnReset.addActionListener(this);
        add(btnReset);

        this.memberService = SpringContentUtil.getBean(MemberService.class);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        try {
            Image im = ImageIO.read(ResourcesUtil.getImage("loginBg.jpg"));
            g.drawImage(im, 0, 0, this.getWidth(), this.getHeight(), this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("login")) {
            if (txUsername.getText().equals("") || txPassword.getPassword().length  == 0) {
                JOptionPane.showMessageDialog(null, "用户密码错误");
            } else {
                Member member = memberService.getByName(txUsername.getText());
                if (member != null && Arrays.equals(member.getPassword().toCharArray(), txPassword.getPassword())) {
                    member.setConfig(memberService.getConfig(member.getId()));
                    this.mainWindow.showMain(member);
                } else {
                    JOptionPane.showMessageDialog(null, "用户密码错误");
                }
            }
        } else if (e.getSource().equals("reset")) {
            this.txUsername.setText("");
            this.txPassword.setText("");
        }
    }
}
