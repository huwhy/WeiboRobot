package cn.huwhy.weibo.robot.view;

import cn.huwhy.common.util.ThreadUtil;
import cn.huwhy.interfaces.Paging;
import cn.huwhy.weibo.robot.action.ActionUtil;
import cn.huwhy.weibo.robot.action.CommentAction;
import cn.huwhy.weibo.robot.model.BaseTableModule;
import cn.huwhy.weibo.robot.model.Member;
import cn.huwhy.weibo.robot.model.Task;
import cn.huwhy.weibo.robot.model.TaskTerm;
import cn.huwhy.weibo.robot.service.ChromeBrowserService;
import cn.huwhy.weibo.robot.service.MemberService;
import cn.huwhy.weibo.robot.service.TaskService;
import cn.huwhy.weibo.robot.util.MyFont;
import cn.huwhy.weibo.robot.util.SpringContentUtil;
import cn.huwhy.weibo.robot.util.Tools;
import javafx.scene.web.WebEngine;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.jb2011.lnf.beautyeye.ch3_button.BEButtonUI;
import org.jb2011.lnf.beautyeye.widget.border.BEShadowBorder;
import org.jdesktop.swingx.JXDatePicker;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 用户管理功能面板
 */
public class ActionJPanel extends JPanel implements ActionListener {

    // 定义全局组件
    private JPanel contentPanel, labelPanel, textPanel, buttonPanel, tablePanel, pagePanel;
    private JTextField txBadNum = new JTextField(10);
    private JXDatePicker datePicker;
    private JCheckBox ckOpenBlack;
    private JButton btnTest, btnDelComment;
    private TaskTerm term = new TaskTerm();
    private BaseTableModule tableData;
    private JTable table;
    private JScrollPane jScrollPane;

    // 定义用户对象
    private Member member = null;
    private MainWindow mainWindow = null;
    private MemberService memberService;
    private ChromeBrowserService chromeBrowserService;
    private TaskService taskService;

    public ActionJPanel(Member member, MainWindow mainWindow) {

        super();
        setLayout(new BorderLayout());
        this.member = member;
        this.mainWindow = mainWindow;
        this.memberService = SpringContentUtil.getBean(MemberService.class);
        this.chromeBrowserService = SpringContentUtil.getBean(ChromeBrowserService.class);
        this.taskService = SpringContentUtil.getBean(TaskService.class);
        initContentPanel();
        initTable();

    }

    public void initContentPanel() {
        contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout(0, 30));
        contentPanel.setOpaque(false);
//        contentPanel.setPreferredSize(new Dimension(600, 420));//关键代码,设置JPanel的大小
//        contentPanel.setSize(600, 420);
        contentPanel.setBorder(new BEShadowBorder());
        labelPanel = new JPanel();
        labelPanel.setOpaque(false);
        textPanel = new JPanel(new GridLayout(4, 1, 0, 10));
        textPanel.setOpaque(false);
        buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);

        JLabel label = new JLabel();
        label.setText("<html><h2 style='text-align:center;'>舆情监测自定义设置</h2></html>");
        label.setFont(MyFont.Static);

        JLabel label_username = new JLabel("用户名:", JLabel.CENTER);
        label_username.setFont(MyFont.Static);
        JLabel label_password = new JLabel("密码:", JLabel.CENTER);
        label_password.setFont(MyFont.Static);
        JLabel lbWName = new JLabel("微博帐号:", JLabel.CENTER);
        lbWName.setFont(MyFont.Static);
        JLabel lbWPwd = new JLabel("微博密码:", JLabel.CENTER);
        lbWPwd.setFont(MyFont.Static);
        JLabel label_identify = new JLabel("选择日期:", JLabel.CENTER);
        label_identify.setFont(MyFont.Static);
        JLabel lbBadNum = new JLabel("自定义黑粉设置", JLabel.CENTER);
        lbBadNum.setFont(MyFont.Static);

        if (member != null) {
            datePicker = new JXDatePicker();
            datePicker.setFormats("yyyy-MM-dd HH:mm");
            datePicker.setDate(new Date());
            if (member.getConfig() != null) {
                txBadNum.setText(member.getConfig().getBadNumLimit() + "");
            }
        }
        JLabel lbRadio = new JLabel("加入黑名单开启:", JLabel.CENTER);
        lbRadio.setFont(MyFont.Static);
        ckOpenBlack = new JCheckBox("是", true);


        txBadNum.setFont(MyFont.Static);
        txBadNum.setEditable(true);

        btnTest = new JButton("测试私信");
        btnTest.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.normal));
        btnTest.setForeground(Color.BLACK);
        btnTest.setFont(MyFont.Static);
        btnTest.setActionCommand("loginWb");
        btnTest.addActionListener(this);

        btnDelComment = new JButton("执行删除评论任务");
        btnDelComment.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.normal));
        btnDelComment.setForeground(Color.BLACK);
        btnDelComment.setFont(MyFont.Static);
        btnDelComment.setActionCommand("delComment");
        btnDelComment.addActionListener(this);

        JButton btnRefresh = new JButton("刷新表格");
        btnRefresh.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.normal));
        btnRefresh.setForeground(Color.BLACK);
        btnRefresh.setFont(MyFont.Static);
        btnRefresh.setActionCommand("refreshTable");
        btnRefresh.addActionListener(this);

        labelPanel.add(label);

        textPanel.add(label_identify);
        datePicker.setFont(MyFont.Static);
        datePicker.setSize(200, 24);
        datePicker.setPreferredSize(new Dimension(200, 24));
        textPanel.add(datePicker);
        textPanel.add(lbBadNum);
        txBadNum.setFont(MyFont.Static);
        textPanel.add(txBadNum);
        textPanel.add(lbRadio);
        ckOpenBlack.setFont(MyFont.Static);
        textPanel.add(ckOpenBlack);
        JLabel lbOpt = new JLabel("操作说明：", JLabel.CENTER);
        lbOpt.setFont(MyFont.Static);
        textPanel.add(lbOpt);
        JLabel lbRemark = new JLabel("自定义多少个黑评论设为黑名单粉丝", JLabel.LEFT);
        lbRemark.setFont(MyFont.Static);
        textPanel.add(lbRemark);

        buttonPanel.add(btnDelComment);
        buttonPanel.add(btnRefresh);
        buttonPanel.add(btnTest);

        contentPanel.add(labelPanel, BorderLayout.NORTH);
        contentPanel.add(textPanel, BorderLayout.CENTER);
        contentPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(contentPanel, BorderLayout.CENTER);
        finishModifyUserContentPanel();
    }

    public void initTable() {
        String[] titles = {"ID", "任务名称", "开始时间", "状态", "描述", "结束时间"};
        List<List<String>> data = loadData(1);
        tableData = new BaseTableModule(titles, data);
        table = new JTable(tableData);
        Tools.setTableStyle(table);
        if (jScrollPane != null) {
            tablePanel.removeAll();
        }
        jScrollPane = new JScrollPane(table);
        Tools.setJspStyle(jScrollPane);
        tablePanel = new JPanel(new BorderLayout());
        tablePanel.setOpaque(false);
        tablePanel.setPreferredSize(new Dimension(1152, 300));//关键代码,设置JPanel的大小
        tablePanel.setSize(1152, 300);
        tablePanel.add(jScrollPane, BorderLayout.CENTER);
        tablePanel.setBorder(new BEShadowBorder());
        if (pagePanel != null) {
            pagePanel.removeAll();
        } else {
            pagePanel = new JPanel();
        }
        JLabel total = new JLabel("总共:" + term.getTotal() + "记录");
        pagePanel.add(total);
        for (int i = 1; i <= term.getTotalPage(); i++) {
            JButton btn = new JButton("" + i);
            btn.addActionListener(this);
            btn.setActionCommand("page");
            pagePanel.add(btn);
        }
        tablePanel.add(pagePanel, BorderLayout.SOUTH);
        add(tablePanel, BorderLayout.SOUTH);
    }

    public void refreshTable(int page) {
        remove(tablePanel);
        String[] titles = {"ID", "任务名称", "开始时间", "状态", "描述", "结束时间"};
        List<List<String>> data = loadData(page == 0 ? 1 : page);
        tableData = new BaseTableModule(titles, data);
        table = new JTable(tableData);
        Tools.setTableStyle(table);
        if (jScrollPane != null) {
            tablePanel.removeAll();
        }
        jScrollPane = new JScrollPane(table);
        Tools.setJspStyle(jScrollPane);
        tablePanel = new JPanel(new BorderLayout());
        tablePanel.setOpaque(false);
        tablePanel.setPreferredSize(new Dimension(1152, 300));//关键代码,设置JPanel的大小
        tablePanel.setSize(1152, 300);
        tablePanel.add(jScrollPane, BorderLayout.CENTER);
        tablePanel.setBorder(new BEShadowBorder());
        if (pagePanel != null) {
            pagePanel.removeAll();
        } else {
            pagePanel = new JPanel();
        }
        JLabel total = new JLabel("总共:" + term.getTotal() + "记录");
        pagePanel.add(total);
        for (int i = 1; i <= term.getTotalPage(); i++) {
            JButton btn = new JButton("" + i);
            btn.addActionListener(this);
            btn.setActionCommand("page");
            pagePanel.add(btn);
        }
        tablePanel.add(pagePanel, BorderLayout.SOUTH);
        add(tablePanel, BorderLayout.SOUTH);
    }

    private List<List<String>> loadData(int page) {
        List<List<String>> data = new ArrayList<>();
        term.setPage(page);
        term.setSize(10);
        term.setMemberId(member.getId());
        Paging<Task> paging = taskService.findTasks(term);
        for (Task task : paging.getData()) {
            List<String> ll = new ArrayList<>();
            ll.add(task.getId() + "");
            ll.add(task.getName());
            ll.add(DateFormatUtils.format(task.getStartTime(), "yyyy-MM-dd HH:mm"));
            ll.add(task.getStatus().getName());
            ll.add(task.getSummary());
            if (task.getEndTime() != null) {
                ll.add(DateFormatUtils.format(task.getEndTime(), "yyyy-MM-dd HH:mm"));
            } else {
                ll.add("");
            }
            data.add(ll);
        }
        return data;
    }

    public void modifyUserContentPanel() {
        txBadNum.setEditable(true);

        btnTest.setVisible(false);
        btnDelComment.setVisible(false);

    }

    public void finishModifyUserContentPanel() {
        txBadNum.setEditable(false);

        btnTest.setVisible(true);
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
            String badNum = txBadNum.getText();
            if (!NumberUtils.isDigits(badNum)) {
                JOptionPane.showMessageDialog(null, "加入黑名单黑评数必需为整数");
                return;
            }
            this.member.getConfig().setBadNumLimit(Integer.parseInt(badNum));
            this.memberService.save(member);
            JOptionPane.showMessageDialog(null, "用户信息修改成功");
            finishModifyUserContentPanel();
        } else if (e.getActionCommand().equals("loginWb")) {
            WebDriver driver = getDriver();
            driver.get("https://weibo.com/u/6377564397");
            List<WebElement> list = driver.findElements(By.cssSelector(".btn_bed a[action-type]"));
            for (WebElement el : list) {
                if (el.getText().equals("私信")) {
                    el.click();
                    while (true) {
                        try {
                            ThreadUtil.sleepSeconds(1);
                            driver.findElement(By.cssSelector("li[node-type=contact_item_6377564397]"));
                            break;
                        } catch (Throwable err) {
                            err.printStackTrace();
                        }
                    }
                    while (true){
                        try {
                            ThreadUtil.sleepSeconds(1);
                            driver.findElement(By.cssSelector(".sendbox_area .W_input")).sendKeys("hello");
                            driver.findElement(By.cssSelector(".sendbox_area .W_input")).sendKeys("\r\n");
                            break;
                        } catch (Throwable err) {
                            err.printStackTrace();
                        }
                    }
                    break;
                }
            }
        } else if (e.getActionCommand().equals("delComment")) {
            CommentAction action = SpringContentUtil.getBean(CommentAction.class);
            WebDriver driver = getDriver();
            member.getConfig().setOpenBlack(ckOpenBlack.isSelected());
            action.init(driver, this.member, datePicker.getDate());
            action.run(() -> ActionJPanel.this.refreshTable(0));
//        } else if (e.getActionCommand().equals("closeComment")) {
//            taskService.submit(() -> {
//                CommentSettingAction action = SpringContentUtil.getBean(CommentSettingAction.class);
//                WebDriver driver = getDriver();
//                action.setDriver(driver);
//                action.setMemberConfig(this.member.getConfig());
//                action.run();
//            });
        } else if (e.getActionCommand().equalsIgnoreCase("refreshTable")) {
            this.refreshTable(0);
        } else if ("page".equals(e.getActionCommand())) {
            String page = ((JButton) e.getSource()).getText();
            refreshTable(Integer.parseInt(page));
        }
    }

    private WebDriver getDriver() {
        WebDriver driver = this.chromeBrowserService.getDriver();
//        if (driver != null) {
//            try {
//                driver.findElement(By.cssSelector(".gn_set"));
//                return driver;
//            } catch (Exception e) {
//            }
//        }
        this.chromeBrowserService.login(member);
        ThreadUtil.sleepSeconds(1);
        while (true) {
            driver = this.chromeBrowserService.getDriver();
            if (!driver.getCurrentUrl().startsWith("https://weibo.com/u/")) {
                ThreadUtil.sleepSeconds(1);
            } else {
                break;
            }
        }
        return driver;
    }
}
