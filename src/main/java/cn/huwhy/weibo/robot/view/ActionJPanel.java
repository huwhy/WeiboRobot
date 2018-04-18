package cn.huwhy.weibo.robot.view;

import cn.huwhy.weibo.robot.action.CommentAction;
import cn.huwhy.weibo.robot.action.CommentSettingAction;
import cn.huwhy.weibo.robot.model.Member;
import cn.huwhy.weibo.robot.service.ChromeBrowserService;
import cn.huwhy.weibo.robot.service.MemberService;
import cn.huwhy.weibo.robot.util.MyFont;
import cn.huwhy.weibo.robot.util.SpringContentUtil;
import jdk.nashorn.internal.parser.DateParser;
import org.apache.commons.lang.math.NumberUtils;
import org.jb2011.lnf.beautyeye.ch3_button.BEButtonUI;
import org.jdesktop.swingx.JXDatePicker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

/**
 * 用户管理功能面板
 */
public class ActionJPanel extends JPanel implements ActionListener {

    // 定义全局组件
    private JPanel contentPanel, labelPanel, textPanel, buttonPanel;
    private JTextField username = new JTextField(10);
    private JPasswordField password = new JPasswordField(10);
    private JTextField wName = new JTextField(10);
    private JPasswordField wPassword = new JPasswordField(10);
    private JTextField txBadNum = new JTextField(10);
    private JXDatePicker datePicker;
    private JButton btnWbLogin, btnDelComment;

    // 定义用户对象
    private Member member = null;
    private MainWindow mainWindow = null;
    private MemberService memberService;
    private ChromeBrowserService chromeBrowserService;

    public ActionJPanel(Member member, MainWindow mainWindow) {

        super();

        this.member = member;
        this.mainWindow = mainWindow;
        initContentPanel();

        this.memberService = SpringContentUtil.getBean(MemberService.class);
        this.chromeBrowserService = SpringContentUtil.getBean(ChromeBrowserService.class);
    }

    public void initContentPanel() {
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
        ;
        if (member != null) {
            username.setText(member.getName());
            password.setText(member.getPassword());
            wName.setText(member.getWbName());
            wPassword.setText(member.getWbPassword());
            datePicker = new JXDatePicker();
            datePicker.setFormats("yyyy-MM-dd HH:mm");
            datePicker.setDate(new Date());
            if (member.getConfig() != null) {
                txBadNum.setText(member.getConfig().getBadNumLimit() + "");
            }
        }

        username.setFont(MyFont.Static);
        username.setEditable(false);
        password.setFont(MyFont.Static);
        password.setEditable(false);
        wName.setFont(MyFont.Static);
        wName.setEditable(false);
        wPassword.setFont(MyFont.Static);
        wPassword.setEditable(false);
        txBadNum.setFont(MyFont.Static);
        txBadNum.setEditable(false);

        btnWbLogin = new JButton("登录微博");
        btnWbLogin.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.normal));
        btnWbLogin.setForeground(Color.BLACK);
        btnWbLogin.setFont(MyFont.Static);
        btnWbLogin.setActionCommand("loginWb");
        btnWbLogin.addActionListener(this);

        btnDelComment = new JButton("删除评论");
        btnDelComment.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.normal));
        btnDelComment.setForeground(Color.BLACK);
        btnDelComment.setFont(MyFont.Static);
        btnDelComment.setActionCommand("delComment");
        btnDelComment.addActionListener(this);

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
        textPanel.add(datePicker);
        textPanel.add(lbBadNum);
        textPanel.add(txBadNum);

        buttonPanel.add(btnWbLogin);
        buttonPanel.add(btnDelComment);

        contentPanel.add(labelPanel, BorderLayout.NORTH);
        contentPanel.add(textPanel, BorderLayout.CENTER);
        contentPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(contentPanel, BorderLayout.CENTER);
        finishModifyUserContentPanel();
    }

    public void modifyUserContentPanel() {
        username.setEditable(true);
        password.setEditable(true);
        wName.setEditable(true);
        wPassword.setEditable(true);
        txBadNum.setEditable(true);

        btnWbLogin.setVisible(false);
        btnDelComment.setVisible(false);

    }

    public void finishModifyUserContentPanel() {
        username.setEditable(false);
        password.setEditable(false);
        wName.setEditable(false);
        wPassword.setEditable(false);
        txBadNum.setEditable(false);

        btnWbLogin.setVisible(true);
        btnDelComment.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("modify")) {
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
                    mainWindow.showLogin();
                }
            }
        } else if (e.getActionCommand().equals("save")) {
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
                this.memberService.save(member);
                if (chgPwd) {
                    JOptionPane.showMessageDialog(null, "用户信息修改成功,请您重新登陆");
                    mainWindow.showLogin();
                } else {
                    JOptionPane.showMessageDialog(null, "用户信息修改成功");
                    finishModifyUserContentPanel();
                }
            }
        } else if (e.getActionCommand().equals("loginWb")) {
            this.chromeBrowserService.login(this.member);
        } else if (e.getActionCommand().equals("delComment")) {
            CommentAction action = SpringContentUtil.getBean(CommentAction.class);
            action.init(this.chromeBrowserService.getDriver(), this.member, datePicker.getDate());
            action.run();
//            CommentSettingAction action = SpringContentUtil.getBean(CommentSettingAction.class);
//            action.setDriver(this.chromeBrowserService.getDriver());
//            action.setMemberConfig(this.member.getConfig());
//            action.run();
        }
    }
}
