package cn.huwhy.weibo.robot.view;

import cn.huwhy.weibo.robot.model.Member;
import cn.huwhy.weibo.robot.model.Word;
import cn.huwhy.weibo.robot.model.WordType;
import cn.huwhy.weibo.robot.service.WordService;
import cn.huwhy.weibo.robot.util.JComboBoxItem;
import cn.huwhy.weibo.robot.util.MyFont;
import org.jb2011.lnf.beautyeye.ch3_button.BEButtonUI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

@Component
public class AddWordJFrame extends JFrame implements MouseListener {

    // 定义全局组件
    JPanel backgroundPanel, labelPanel, contentPanel, buttonPanel;
    JLabel lbWord, lbType;
    JTextField txWord;
    JComboBox cbType;
    JButton btnAdd;

    // 获得屏幕的大小
    final static int width = Toolkit.getDefaultToolkit().getScreenSize().width;
    final static int height = Toolkit.getDefaultToolkit().getScreenSize().height;

    // 父面板对象
    WordDataPanel parentPanel;

    @Autowired
    private WordService wordService;
    private volatile boolean init;
    private Member member;

    public AddWordJFrame() {
        super();
    }

    public AddWordJFrame(WordDataPanel parentPanel) {
        this.parentPanel = parentPanel;
    }

    public void init(WordDataPanel parentPanel) {
        if (!init) {
            this.init = true;
            this.parentPanel = parentPanel;
            initBackgroundPanel();
            this.add(backgroundPanel);
            this.setTitle("添加关键词");
            this.setSize(640, 360);
            this.setVisible(true);
            this.setLocationRelativeTo(null);
            this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        }
    }

    // 初始化背景面板
    public void initBackgroundPanel() {
        backgroundPanel = new JPanel(new BorderLayout());

        initContentPanel();
        initButtonPanel();
        initLabelPanel();

        backgroundPanel.add(labelPanel, "North");
        backgroundPanel.add(contentPanel, "Center");
        backgroundPanel.add(buttonPanel, "South");
    }

    // 初始化label面板
    public void initLabelPanel() {

        labelPanel = new JPanel();

        JLabel title = new JLabel("添加关键词");
        title.setFont(MyFont.Static);

        labelPanel.add(title);
    }

    // 初始化商品信息面板
    public void initContentPanel() {
        contentPanel = new JPanel(new GridLayout(6, 2));

        lbWord = new JLabel("关键词", JLabel.CENTER);
        lbType = new JLabel("类型", JLabel.CENTER);

        txWord = new JTextField("");

        // 商品种类下拉框
        cbType = new JComboBox();
        for (WordType type : WordType.values()) {
            cbType.addItem(new JComboBoxItem<WordType>(type) {
                @Override
                public String toString() {
                    return this.getData().getName();
                }
            });
        }
        contentPanel.add(lbWord);
        contentPanel.add(txWord);
        contentPanel.add(lbType);
        contentPanel.add(cbType);
    }

    // 初始化按钮面板
    public void initButtonPanel() {
        buttonPanel = new JPanel();

        btnAdd = new JButton("保存");
        btnAdd.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.lightBlue));
        btnAdd.setForeground(Color.white);
        btnAdd.setFont(MyFont.Static);
        btnAdd.addMouseListener(this);

        buttonPanel.add(btnAdd);
    }

    // 鼠标点击事件
    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == btnAdd) {
            String word = txWord.getText().trim();
            if (word.isEmpty()) {
                JOptionPane.showMessageDialog(null, "请输入关键词");
            } else {
                WordType type = ((JComboBoxItem<WordType>) cbType.getSelectedItem()).getData();
                Word word1 = new Word();
                word1.setWord(word);
                word1.setType(type);
                word1.setMemberId(this.member.getId());
                wordService.save(word1);
                JOptionPane.showMessageDialog(null, "添加关键词成功");
                this.setVisible(false);
                parentPanel.refreshTablePanel(1, null);
            }
        }

    }

    @Override
    public void mouseEntered(MouseEvent arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseExited(MouseEvent arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mousePressed(MouseEvent arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseReleased(MouseEvent arg0) {
        // TODO Auto-generated method stub

    }

    public void setMember(Member member) {
        this.member = member;
    }
}