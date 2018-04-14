package cn.huwhy.weibo.robot.view;

import cn.huwhy.weibo.robot.App;
import cn.huwhy.weibo.robot.action.CommentAction;
import cn.huwhy.weibo.robot.model.Member;
import cn.huwhy.weibo.robot.util.MyFont;
import cn.huwhy.weibo.robot.util.ResourcesUtil;
import cn.huwhy.weibo.robot.util.SpringContentUtil;
import org.apache.commons.lang.math.NumberUtils;
import org.jb2011.lnf.beautyeye.ch3_button.BEButtonUI;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

/**
 * 用户管理功能面板
 */
public class MemberManagerJPanel implements MouseListener {

    // 定义全局组件
    ImagePanel backgroundPanel;
    private JPanel contentPanel, labelPanel, textPanel, buttonPanel;
    private JTextField username = new JTextField(10);
    private JPasswordField password = new JPasswordField(10);
    private JTextField wName = new JTextField(10);
    private JPasswordField wPassword = new JPasswordField(10);
    private JTextField identify = new JTextField(10);
    private JTextField txBadNum = new JTextField(10);
    private JButton btnModify, btnSave, btnWbLogin, btnTest;

    // 定义用户对象
    private Member member = null;
    private IndexJFrame frame = null;

    public MemberManagerJPanel(Member member, IndexJFrame frame) {
        this.member = member;
        this.frame = frame;

        try {
            Image image = ImageIO.read(ResourcesUtil.getImage("userbackground.jpg"));
            backgroundPanel = new ImagePanel(image);
//			// 获取背景面板大小
//			this.width = mainPanel.getWidth();
//			this.height = mainPanel.getHeight();
        } catch (IOException e) {
            e.printStackTrace();
        }

        initContentPanel();
    }

    public void initContentPanel() {

        backgroundPanel.removeAll();

        contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout(0, 30));
        contentPanel.setOpaque(false);
        labelPanel = new JPanel();
        labelPanel.setOpaque(false);
        textPanel = new JPanel(new GridLayout(3, 2, 0, 20));
        textPanel.setOpaque(false);
        buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);

        JLabel label = new JLabel();
        label.setText("<html><h2 style='text-align:center;'>个人信息</h2></html>");
        label.setFont(MyFont.Static);

        JLabel label_username = new JLabel("用户名:", JLabel.CENTER);
        label_username.setFont(MyFont.Static);
        JLabel label_password = new JLabel("密码:", JLabel.CENTER);
        label_password.setFont(MyFont.Static);
        JLabel lbWName = new JLabel("微博帐号:", JLabel.CENTER);
        lbWName.setFont(MyFont.Static);
        JLabel lbWPwd = new JLabel("微博密码:", JLabel.CENTER);
        lbWPwd.setFont(MyFont.Static);
        JLabel label_identify = new JLabel("身份:", JLabel.CENTER);
        label_identify.setFont(MyFont.Static);
        JLabel lbBadNum = new JLabel("多少个黑评加入黑名单", JLabel.CENTER);

        if (member != null) {
            username.setText(member.getName());
            password.setText(member.getPassword());
            wName.setText(member.getWbName());
            wPassword.setText(member.getWbPassword());
            identify.setText("管理员");
            txBadNum.setText(member.getConfig().getBadNumLimit() + "");
        }

        username.setFont(MyFont.Static);
        username.setEditable(false);
        password.setFont(MyFont.Static);
        password.setEditable(false);
        identify.setFont(MyFont.Static);
        identify.setEditable(false);
        wName.setFont(MyFont.Static);
        wName.setEditable(false);
        wPassword.setFont(MyFont.Static);
        wPassword.setEditable(false);
        txBadNum.setFont(MyFont.Static);
        txBadNum.setEditable(false);

        btnModify = new JButton("修改信息");
        btnModify.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.lightBlue));
        btnModify.setForeground(Color.white);
        btnModify.setFont(MyFont.Static);
        btnModify.addMouseListener(this);

        btnWbLogin = new JButton("登录微博");
        btnWbLogin.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.normal));
        btnWbLogin.setForeground(Color.BLACK);
        btnWbLogin.setFont(MyFont.Static);
        btnWbLogin.addMouseListener(this);
        btnTest = new JButton("测试-评论");
        btnTest.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.normal));
        btnTest.setForeground(Color.BLACK);
        btnTest.setFont(MyFont.Static);
        btnTest.addMouseListener(this);
        btnSave = new JButton("保存修改");
        btnSave.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.green));
        btnSave.setForeground(Color.white);
        btnSave.setFont(MyFont.Static);
        btnSave.addMouseListener(this);

        labelPanel.add(label);

        textPanel.add(label_username);
        textPanel.add(username);
        textPanel.add(label_password);
        textPanel.add(password);
        textPanel.add(lbWName);
        textPanel.add(wName);
        textPanel.add(lbWPwd);
        textPanel.add(wPassword);
        textPanel.add(label_identify);
        textPanel.add(identify);
        textPanel.add(lbBadNum);
        textPanel.add(txBadNum);

        buttonPanel.add(btnSave);
        buttonPanel.add(btnModify);
        buttonPanel.add(btnWbLogin);
        buttonPanel.add(btnTest);

        contentPanel.add(labelPanel, BorderLayout.NORTH);
        contentPanel.add(textPanel, BorderLayout.CENTER);
        contentPanel.add(buttonPanel, BorderLayout.SOUTH);
        backgroundPanel.add(contentPanel, BorderLayout.CENTER);
        finishModifyUserContentPanel();
    }

    public void modifyUserContentPanel() {
        username.setEditable(true);
        password.setEditable(true);
        wName.setEditable(true);
        wPassword.setEditable(true);
        txBadNum.setEditable(true);

        btnSave.setVisible(true);

        btnModify.setVisible(false);
        btnWbLogin.setVisible(false);
        btnTest.setVisible(false);

    }

    public void finishModifyUserContentPanel() {
        username.setEditable(false);
        password.setEditable(false);
        wName.setEditable(false);
        wPassword.setEditable(false);
        txBadNum.setEditable(false);

        btnSave.setVisible(false);

        btnModify.setVisible(true);
        btnWbLogin.setVisible(true);
        btnTest.setVisible(true);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == btnModify) {
            String input_password = JOptionPane.showInputDialog(null, "请输入原始密码", "用户验证",
                    JOptionPane.PLAIN_MESSAGE);
            if (input_password.trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "原始密码不能为空");
            } else {
                if (member != null) {
                    if (!input_password.equals(member.getPassword())) {
                        JOptionPane.showMessageDialog(null, "原始密码有误");
                    } else {
                        JOptionPane.showMessageDialog(null, "验证通过，请您修改信息");
                        modifyUserContentPanel();
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "登录超时，请您重新登录");
                    frame.setVisible(false);
                    App.main(null);
                }
            }
        } else if (e.getSource() == btnSave) {
            String username = this.username.getText().trim();
            String password = new String(this.password.getPassword());
            String wbName = wName.getText().trim();
            String wbPwd = new String(wPassword.getPassword());
            String badNum = txBadNum.getText();
            if (!NumberUtils.isDigits(badNum)) {
                JOptionPane.showMessageDialog(null, "加入黑名单黑评数必需为整数");
                return;
            }
            if (username.isEmpty()) {
                JOptionPane.showMessageDialog(null, "用户名不能为空");
            } else if (password.isEmpty()) {
                JOptionPane.showMessageDialog(null, "用户密码不能为空");
            } else {
                this.member.setName(username);
                boolean chgPwd = !this.member.getPassword().equals(password);
                this.member.setPassword(password);
                this.member.setWbName(wbName);
                this.member.setWbPassword(wbPwd);
                this.member.getConfig().setBadNumLimit(Integer.parseInt(badNum));
                this.frame.memberService.save(member);
                if (chgPwd) {
                    JOptionPane.showMessageDialog(null, "用户信息修改成功,请您重新登陆");
                    frame.setVisible(false);
                    App.main(null);
                } else {
                    JOptionPane.showMessageDialog(null, "用户信息修改成功");
                    finishModifyUserContentPanel();
                }
            }
        } else if (e.getSource() == btnWbLogin) {
            this.frame.chromeBrowserService.login(this.member);
        } else if (e.getSource() == btnTest) {
            CommentAction action = SpringContentUtil.getBean(CommentAction.class);
            action.setDriver(this.frame.chromeBrowserService.getDriver());
            action.setMember(this.member);
            action.run();
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseExited(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mousePressed(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // TODO Auto-generated method stub

    }
}
