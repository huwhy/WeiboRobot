package cn.huwhy.weibo.robot.view;

import cn.huwhy.weibo.robot.model.Member;
import cn.huwhy.weibo.robot.util.MyFont;
import org.jb2011.lnf.beautyeye.ch3_button.BEButtonUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainPanel extends JTabbedPane implements ActionListener {

    private MainWindow mainWindow;
    private Member member;
    private volatile boolean addTabs = false;
    private MemberManagerJPanel memberManagerJPanel;

    public MainPanel(MainWindow mainWindow, Member member) {
        super(JTabbedPane.TOP);

        this.member = member;
        this.mainWindow = mainWindow;
        // 设置tab布局
        setTabLayoutPolicy(JTabbedPane.WRAP_TAB_LAYOUT);
        setFont(MyFont.Static);
        setOpaque(true);
        JPanel panel1 = new JPanel();
        panel1.setLayout(null);
        JButton btnSecond = new JButton("切换到1");
        btnSecond.setSize(100, 50);
        btnSecond.setBounds(330, 320, 70, 27);
        btnSecond.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.red));
        btnSecond.setForeground(Color.white);
        btnSecond.setFont(MyFont.Static);
        btnSecond.setActionCommand("login");
        btnSecond.addActionListener(this);
        panel1.add(btnSecond);
        memberManagerJPanel = new MemberManagerJPanel(member, mainWindow);
        addTab("微博帐户登录", memberManagerJPanel);
//        addTab("自定义关键词", new WordDataPanel(member));
//        addTab("粉丝管理", new FansGroupDataPanel(member.getId()));
//        addTab("任务管理", new ActionJPanel(member, mainWindow));
    }

    public void addTabs() {
        if (!addTabs) {
            remove(memberManagerJPanel);
            addTabs = true;
            addTab("自定义敏感词", new WordDataPanel(member));
            addTab("任务中心", new ActionJPanel(member, mainWindow));
            addTab("粉丝管理", new FansGroupDataPanel(member.getId()));
            addTab("自动侦察兵", new AutoActionJPanel(member, mainWindow));
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("login")) {
            mainWindow.showLogin();
        }
    }
}
