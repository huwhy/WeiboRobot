package cn.huwhy.weibo.robot.view;

import cn.huwhy.common.util.RandomUtil;
import cn.huwhy.interfaces.Paging;
import cn.huwhy.weibo.robot.model.Member;
import cn.huwhy.weibo.robot.model.MyFansTerm;
import cn.huwhy.weibo.robot.model.WordType;
import cn.huwhy.weibo.robot.service.ChromeBrowserService;
import cn.huwhy.weibo.robot.service.FansService;
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
 * 数据统计模块
 */
public class DataCountManagerJPanel extends JPanel implements ActionListener {

    // 定义全局组件
    private JPanel contentPanel, labelPanel, textPanel, buttonPanel;
    private JButton btnRefresh;
    private JLabel lbTotalFans, lbRedFans, lbBlackFans;

    // 定义用户对象
    private Member member = null;
    private MainWindow mainWindow = null;
    private FansService fansService;

    public DataCountManagerJPanel(Member member, MainWindow mainWindow) {

        super();

        this.member = member;
        this.mainWindow = mainWindow;
        initContentPanel();

        this.fansService = SpringContentUtil.getBean(FansService.class);

        count();
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
        label.setText("<html><h2 style='text-align:center;'>数据统计</h2></html>");
        label.setFont(MyFont.Static);
        labelPanel.add(label);

        JLabel lbWPwd = new JLabel("微博密码:", JLabel.LEFT);
        lbWPwd.setFont(MyFont.Static);


        JLabel lbTotal = new JLabel("总粉丝数:", JLabel.LEFT);
        lbTotal.setFont(MyFont.Static);
        textPanel.add(lbTotal);
        lbTotalFans = new JLabel("");
        lbTotalFans.setFont(MyFont.Static);
        textPanel.add(lbTotalFans);

        JLabel lbRed = new JLabel("铁粉数:", JLabel.LEFT);
        lbRed.setFont(MyFont.Static);
        textPanel.add(lbRed);
        lbRedFans = new JLabel("");
        lbRedFans.setFont(MyFont.Static);
        textPanel.add(lbRedFans);

        JLabel lbBlack = new JLabel("黑粉数:", JLabel.LEFT);
        lbBlack.setFont(MyFont.Static);
        textPanel.add(lbBlack);
        lbBlackFans = new JLabel("");
        lbBlackFans.setFont(MyFont.Static);
        textPanel.add(lbBlackFans);

        btnRefresh = new JButton("刷新数据");
        btnRefresh.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.normal));
        btnRefresh.setForeground(Color.BLACK);
        btnRefresh.setFont(MyFont.Static);
        btnRefresh.setActionCommand("refresh");
        btnRefresh.addActionListener(this);
        JPanel btnPanel = new JPanel();
        btnPanel.add(btnRefresh);
        buttonPanel.setLayout(new BorderLayout());
        buttonPanel.add(btnPanel, BorderLayout.NORTH);

        contentPanel.add(labelPanel, BorderLayout.NORTH);
        contentPanel.add(textPanel, BorderLayout.CENTER);
        contentPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(contentPanel, BorderLayout.CENTER);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("refresh")) {
            count();
        }
    }

    private void count() {
        MyFansTerm term = new MyFansTerm();
        term.setPage(1);
        term.setSize(1);
        Paging paging = fansService.findMyFans(term);
        long totalNum, redNum, blackNum;
        totalNum = paging.getTotal();
        lbTotalFans.setText("" + totalNum);

        term.setType(WordType.IRON);
        paging = fansService.findMyFans(term);
        redNum = paging.getTotal();
        lbRedFans.setText(String.format("%d, 占比 %.2f%%", redNum, redNum  * 100.0 / totalNum));

        term.setType(WordType.BLACK);
        paging = fansService.findMyFans(term);
        blackNum = paging.getTotal();
        lbBlackFans.setText(String.format("%d, 占比 %.2f%%", blackNum, blackNum  * 100.0 / totalNum));
    }
}
