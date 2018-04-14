package cn.huwhy.weibo.robot.view;

import cn.huwhy.weibo.robot.model.Member;
import cn.huwhy.weibo.robot.service.ChromeBrowserService;
import cn.huwhy.weibo.robot.service.MemberService;
import cn.huwhy.weibo.robot.util.MyFont;
import cn.huwhy.weibo.robot.util.ResourcesUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

@Component
public class IndexJFrame extends JFrame implements MouseListener, ActionListener {

    // 定义用户对象
    private Member member;

    // 获得屏幕的大小
    final static int width = Toolkit.getDefaultToolkit().getScreenSize().width;
    final static int height = Toolkit.getDefaultToolkit().getScreenSize().height;

    // 定义全局组件
    JPanel backgroundPanel, topPanel, topMenu, topPrompt, centerPanel;
    JTabbedPane jTabbedPane;

    private JLabel memberManager, wordsManager, fansGroupManager, activeManager;

    @Autowired
    MemberService memberService;
    @Autowired
    WordDataPanel wordDataPanel;
    @Autowired
    FansGroupDataPanel fansGroupDataPanel;
    @Autowired
    ChromeBrowserService chromeBrowserService;

    public IndexJFrame() {
        //窗口淡入淡出
        //new WindowOpacity(this);
        // 设置tab面板缩进
        UIManager.put("TabbedPane.tabAreaInsets", new javax.swing.plaf.InsetsUIResource(0, 0, 0, 0));

        try {
            Image image = ImageIO.read(ResourcesUtil.getImage("logo.png"));
            this.setIconImage(image);
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.setTitle("微博舆情监测系统");
        this.setSize((int) (width * 0.8f), (int) (height * 0.8f));
        this.setLocationRelativeTo(null);    // 此窗口将置于屏幕的中央。
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public void setMember(Member member) {
        this.member = member;
        member.setConfig(memberService.getConfig(member.getId()));
        initBackgroundPanel();
    }

    // 初始化背景面板
    public void initBackgroundPanel() {

        backgroundPanel = new JPanel(new BorderLayout());
        initTop();
        initCenterPanel();

        backgroundPanel.add(topPanel, "North");
        backgroundPanel.add(centerPanel, "Center");

        this.add(backgroundPanel);
    }

    // 初始化顶部顶部面板
    public void initTop() {

        initTopMenu();
        initTopPrompt();
        topPanel = new JPanel(new BorderLayout());
//        topPanel.setBorder(BorderFactory.createLineBorder(Color.red));
        topPanel.setPreferredSize(new Dimension(width, 40));
        topPanel.add(topMenu, "West");
        topPanel.add(topPrompt, BorderLayout.EAST);
    }

    // 初始化顶部菜单
    public void initTopMenu() {

        topMenu = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topMenu.setPreferredSize(new Dimension((int) (this.getSize().getWidth()), 40));
        topMenu.setOpaque(false);
        memberManager = createMenuLabel("用户设置", "icon_user_setting", topMenu, true);
        memberManager.setName("menu_user");
        memberManager.setText("<html><font color='#336699' style='font-weight:bold'>" + "用户管理" + "</font>&nbsp;</html>");
        wordsManager = createMenuLabel("关键词设置", "icon_words_setting", topMenu, true);
        wordsManager.setName("menu_words");
        fansGroupManager = createMenuLabel("人群识别", "male", topMenu, false);
        fansGroupManager.setName("menu_fans_group");
        activeManager = memberManager;
    }

    // 创建顶部菜单Label
    public JLabel createMenuLabel(String text, String name, JPanel jpanel, boolean hasLine) {
        Icon icon = new ImageIcon(ResourcesUtil.getImage(name + ".png"));
        JLabel jlabel = new JLabel(icon);
        jlabel.setText("<html><font color='black'>" + text + "</font>&nbsp;</html>");
        jlabel.addMouseListener(this);
        jlabel.setFont(MyFont.Static);
        jpanel.add(jlabel);
        if (hasLine) {
            JLabel line = new JLabel("<html>&nbsp;<font color='#D2D2D2'>|</font>&nbsp;</html>");
            jpanel.add(line);
        }
        return jlabel;
    }

    // 初始化顶部欢迎面板
    public void initTopPrompt() {

        Icon icon = new ImageIcon(ResourcesUtil.getImage("male.png"));
        JLabel label = new JLabel(icon);
        if (member != null) {
            label.setText("<html><font color='black'>欢迎您，</font><font color='#336699'><b>" + this.member.getName()
                    + "</b></font></html>");
        } else {
            label.setText("<html><font color='black'>欢迎您，</font><font color='#336699'><b></b></font></html>");
        }
        label.setFont(MyFont.Static);
        topPrompt = new JPanel();
        topPrompt.setPreferredSize(new Dimension(180, 40));
        topPrompt.setOpaque(false);
        topPrompt.add(label);
    }

    // 初始化中心面板
    public void initCenterPanel() {
        centerPanel = new JPanel(new BorderLayout());
        createMemberManagerTab();
        centerPanel.setOpaque(false);// 设置控件透明
    }

    // 创建基础数据面板
    public void createWordDataTab() {
        centerPanel.removeAll();
        wordDataPanel.setMember(this.member);
        wordDataPanel.init();
        // 设置tab标题位置
        jTabbedPane = new JTabbedPane(JTabbedPane.TOP);
        // 设置tab布局
        jTabbedPane.setTabLayoutPolicy(JTabbedPane.WRAP_TAB_LAYOUT);
        jTabbedPane.setFont(MyFont.Static);

        jTabbedPane.addTab("关键词设置", wordDataPanel.backgroundPanel);
        centerPanel.add(jTabbedPane, "Center");
    }

    // 创建用户管理面板
    public void createMemberManagerTab() {

        centerPanel.removeAll();
        // 设置tab标题位置
        jTabbedPane = new JTabbedPane(JTabbedPane.TOP);
        // 设置tab布局
        jTabbedPane.setTabLayoutPolicy(JTabbedPane.WRAP_TAB_LAYOUT);
        jTabbedPane.setFont(MyFont.Static);

        jTabbedPane.addTab("用户设置", new MemberManagerJPanel(member, this).backgroundPanel);
        centerPanel.add(jTabbedPane, "Center");
    }

    // 创建用户管理面板
    public void createFansManagerTab() {

        centerPanel.removeAll();
        fansGroupDataPanel.init();
        // 设置tab标题位置
        jTabbedPane = new JTabbedPane(JTabbedPane.TOP);
        // 设置tab布局
        jTabbedPane.setTabLayoutPolicy(JTabbedPane.WRAP_TAB_LAYOUT);
        jTabbedPane.setFont(MyFont.Static);

        jTabbedPane.addTab("人物识别", fansGroupDataPanel.mainPanel);
        centerPanel.add(jTabbedPane, "Center");
    }

    // 鼠标点击事件
    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == wordsManager) {
            this.activeManager = wordsManager;
            createWordDataTab();
            wordsManager.setText("<html><font color='#336699' style='font-weight:bold'>" + "关键词设置" + "</font>&nbsp;</html>");
            memberManager.setText("<html><font color='black'>" + "用户设置" + "</font>&nbsp;</html>");
            fansGroupManager.setText("<html><font color='black'>" + "人物识别" + "</font>&nbsp;</html>");
        } else if (e.getSource() == memberManager) {
            this.activeManager = memberManager;
            createMemberManagerTab();
            memberManager.setText("<html><font color='#336699' style='font-weight:bold'>" + "用户设置" + "</font>&nbsp;</html>");
            wordsManager.setText("<html><font color='black'>" + "关键词设置" + "</font>&nbsp;</html>");
            fansGroupManager.setText("<html><font color='black'>" + "人物识别" + "</font>&nbsp;</html>");
        } else if (e.getSource() == fansGroupManager) {
            this.activeManager = fansGroupManager;
            createFansManagerTab();
            fansGroupManager.setText("<html><font color='#336699' style='font-weight:bold'>" + "人物识别" + "</font>&nbsp;</html>");
            wordsManager.setText("<html><font color='black'>" + "关键词设置" + "</font>&nbsp;</html>");
            memberManager.setText("<html><font color='black'>" + "用户设置" + "</font>&nbsp;</html>");
        }

    }

    // 鼠标划入事件
    @Override
    public void mouseEntered(MouseEvent e) {
        if (e.getSource() == wordsManager) {
            wordsManager.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            wordsManager.setText("<html><font color='#336699' style='font-weight:bold'>" + "关键词设置" + "</font>&nbsp;</html>");
        } else if (e.getSource() == memberManager) {
            memberManager.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            memberManager.setText("<html><font color='#336699' style='font-weight:bold'>" + "用户设置" + "</font>&nbsp;</html>");
        } else if (e.getSource() == fansGroupManager) {
            fansGroupManager.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            fansGroupManager.setText("<html><font color='#336699' style='font-weight:bold'>" + "人物识别" + "</font>&nbsp;</html>");
        }

    }

    // 鼠标划出事件
    @Override
    public void mouseExited(MouseEvent e) {
        if (e.getSource() == wordsManager) {
            if (activeManager != wordsManager) {
                wordsManager.setText("<html><font color='black'>" + "关键词设置" + "</font>&nbsp;</html>");
            }
        }
        if (e.getSource() == memberManager) {
            if (activeManager != memberManager) {
                memberManager.setText("<html><font color='black'>" + "用户设置" + "</font>&nbsp;</html>");
            }
        }
        if (e.getSource() == fansGroupManager) {
            if (activeManager != fansGroupManager) {
                fansGroupManager.setText("<html><font color='black'>" + "人物识别" + "</font>&nbsp;</html>");
            }
        }

    }

    @Override
    public void mousePressed(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
