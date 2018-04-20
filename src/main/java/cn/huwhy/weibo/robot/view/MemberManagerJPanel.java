package cn.huwhy.weibo.robot.view;

import cn.huwhy.common.util.RandomUtil;
import cn.huwhy.weibo.robot.model.Member;
import cn.huwhy.weibo.robot.service.ChromeBrowserService;
import cn.huwhy.weibo.robot.service.MemberService;
import cn.huwhy.weibo.robot.util.MyFont;
import cn.huwhy.weibo.robot.util.SpringContentUtil;
import org.apache.logging.log4j.util.Strings;
import org.jb2011.lnf.beautyeye.ch3_button.BEButtonUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 用户管理功能面板
 */
public class MemberManagerJPanel extends JPanel implements ActionListener {

    // 定义全局组件
    private JPanel contentPanel, labelPanel, textPanel, buttonPanel;
    private JTextField wName = new JTextField(10);
    private JTextField txCode = new JTextField(10);
    private JPasswordField wPassword = new JPasswordField(10);
    private JButton btnSave, btnLogout, btnWbLogin;
    private String code;
    private JButton codeRefresh;
    private JLabel lbCodeTip, lbCode;

    // 定义用户对象
    private Member member = null;
    private MainWindow mainWindow = null;
    private MemberService memberService;
    private ChromeBrowserService chromeBrowserService;

    public MemberManagerJPanel(Member member, MainWindow mainWindow) {

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
        textPanel = new JPanel(new GridLayout(5, 2, 0, 20));
        textPanel.setOpaque(false);
        buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);

        JLabel label = new JLabel();
        label.setText("<html><h2 style='text-align:center;'>微博帐户登录</h2></html>");
        label.setFont(MyFont.Static);

        JLabel lbWName = new JLabel("微博帐号:", JLabel.LEFT);
        lbWName.setFont(MyFont.Static);
        JLabel lbWPwd = new JLabel("微博密码:", JLabel.LEFT);
        lbWPwd.setFont(MyFont.Static);

        if (member != null) {
            wName.setText(member.getWbName());
            wPassword.setText(member.getWbPassword());
        }

        wName.setFont(MyFont.Static);
        wName.setEditable(false);
        wPassword.setFont(MyFont.Static);
        wPassword.setEditable(true);


        btnWbLogin = new JButton("登录微博");
        btnWbLogin.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.normal));
        btnWbLogin.setForeground(Color.BLACK);
        btnWbLogin.setFont(MyFont.Static);
        btnWbLogin.setActionCommand("loginWb");
        btnWbLogin.addActionListener(this);

//        btnModify = new JButton("修改信息");
//        btnModify.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.lightBlue));
//        btnModify.setForeground(Color.white);
//        btnModify.setFont(MyFont.Static);
//        btnModify.setActionCommand("modify");
//        btnModify.addActionListener(this);

        btnSave = new JButton("保存修改");
        btnSave.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.green));
        btnSave.setForeground(Color.white);
        btnSave.setFont(MyFont.Static);
        btnSave.setActionCommand("save");
        btnSave.addActionListener(this);

        btnLogout = new JButton("退出");
        btnLogout.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.lightBlue));
        btnLogout.setForeground(Color.white);
        btnLogout.setFont(MyFont.Static);
        btnLogout.setActionCommand("logout");
        btnLogout.addActionListener(this);

        labelPanel.add(label);

        textPanel.add(lbWName);
        textPanel.add(wName);
        textPanel.add(new JLabel("备注:", JLabel.LEFT));
        textPanel.add(new JLabel("微博帐户设置后不能更改"));
        textPanel.add(lbWPwd);
        textPanel.add(wPassword);
        lbCode = new JLabel("验证码:", JLabel.LEFT);
        lbCode.setFont(MyFont.Static);
        txCode.setFont(MyFont.Static);
        txCode.setEditable(true);
        textPanel.add(lbCode);
        textPanel.add(txCode);
        this.code = RandomUtil.getRandomString(4);
        lbCodeTip = new JLabel("请输入验证码:", JLabel.LEFT);
        lbCodeTip.setFont(MyFont.Static);
        textPanel.add(lbCodeTip);
        codeRefresh = new JButton(code + " 刷新");
        codeRefresh.addActionListener(e -> {
            MemberManagerJPanel.this.code = RandomUtil.getRandomString(4);
            MemberManagerJPanel.this.codeRefresh.setText("" + code + " 刷新");
        });
        textPanel.add(codeRefresh);

        buttonPanel.setLayout(new BorderLayout());
        JPanel btnPanel = new JPanel();
        btnPanel.add(btnWbLogin);
        btnPanel.add(btnSave);
        btnPanel.add(btnLogout);
        buttonPanel.add(btnPanel, BorderLayout.NORTH);
        JLabel jbTip = new JLabel("神盾微博舆情监测系统, 私人定制！V1.0", JLabel.CENTER);
        jbTip.setForeground(Color.red);
        jbTip.setFont(MyFont.Static2);
        buttonPanel.add(new JLabel(""), BorderLayout.CENTER);
        buttonPanel.add(jbTip, BorderLayout.SOUTH);

        contentPanel.add(labelPanel, BorderLayout.NORTH);
        contentPanel.add(textPanel, BorderLayout.CENTER);
        contentPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(contentPanel, BorderLayout.CENTER);
        finishModifyUserContentPanel();
    }

    public void modifyUserContentPanel() {
        if (Strings.isBlank(wName.getText())) {
            wName.setEditable(true);
        }
        wPassword.setEditable(true);

        btnSave.setVisible(true);


    }

    public void finishModifyUserContentPanel() {
        wName.setEditable(false);
//        wPassword.setEditable(false);
        btnSave.setVisible(false);
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
//        } else if (e.getActionCommand().equals("save")) {
//            String wbName = wName.getText().trim();
//            String wbPwd = new String(wPassword.getPassword());
//            this.member.setWbName(wbName);
//            this.member.setWbPassword(wbPwd);
//            this.memberService.save(member);
//            JOptionPane.showMessageDialog(null, "用户信息修改成功");
//            finishModifyUserContentPanel();
        } else if ("logout".equals(e.getActionCommand())) {
            mainWindow.showLogin();
        } else if (e.getActionCommand().equals("loginWb")) {
            if (code.equalsIgnoreCase(txCode.getText())) {
                this.codeRefresh.setVisible(false);
                this.lbCodeTip.setVisible(false);
                this.lbCode.setVisible(false);
                this.txCode.setVisible(false);
                mainWindow.addTabs();
                this.chromeBrowserService.login(this.member);
            } else {
                JOptionPane.showMessageDialog(null, "请输入正确的验证码");
            }
        }
    }
}
