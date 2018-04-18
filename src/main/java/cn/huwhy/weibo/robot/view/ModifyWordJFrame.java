package cn.huwhy.weibo.robot.view;

import cn.huwhy.weibo.robot.model.Word;
import cn.huwhy.weibo.robot.model.WordType;
import cn.huwhy.weibo.robot.service.WordService;
import cn.huwhy.weibo.robot.util.JComboBoxItem;
import cn.huwhy.weibo.robot.util.MyFont;
import cn.huwhy.weibo.robot.util.SpringContentUtil;
import org.jb2011.lnf.beautyeye.ch3_button.BEButtonUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import static java.awt.BorderLayout.CENTER;
import static java.awt.BorderLayout.NORTH;
import static java.awt.BorderLayout.SOUTH;

public class ModifyWordJFrame extends JFrame implements MouseListener {

    // 定义全局组件
    JPanel backgroundPanel, labelPanel, contentPanel, buttonPanel;
    JLabel lbWord, lbType;
    JTextField txWord;
    JComboBox cbType;
    JButton btnModify;

    // 表格对象
    WordDataPanel parentPanel;
    private Word word;

    private WordService wordService;

    public void init(WordDataPanel parentPanel, long id) {
        this.parentPanel = parentPanel;
        this.wordService = SpringContentUtil.getBean(WordService.class);
        this.word = wordService.get(id);
        initBackgroundPanel();
        this.add(backgroundPanel);
        this.setTitle("修改关键词");
        this.setSize(640, 360);
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
    }

    // 初始化背景面板
    public void initBackgroundPanel() {
        backgroundPanel = new JPanel(new BorderLayout());

        initLabelPanel();
        initContentPanel();
        initButtonPanel();

        backgroundPanel.add(labelPanel, NORTH);
        backgroundPanel.add(contentPanel, CENTER);
        backgroundPanel.add(buttonPanel, SOUTH);
    }

    // 初始化label面板
    public void initLabelPanel() {

        labelPanel = new JPanel();

        JLabel title = new JLabel("关键词信息");
        title.setFont(MyFont.Static);

        labelPanel.add(title);
    }

    // 初始化商品信息面板
    public void initContentPanel() {
        contentPanel = new JPanel(new GridLayout(6, 2));
        lbWord = new JLabel("关键词", JLabel.CENTER);
        lbType = new JLabel("类型", JLabel.CENTER);

        txWord = new JTextField(word.getWord());
        // 商品种类下拉框
        cbType = new JComboBox();
        int cbIndex = 0, i = 0;
        for (WordType type : WordType.values()) {
            if (word.getType().equals(type)) {
                cbIndex = i;
            }
            i++;
            cbType.addItem(new JComboBoxItem<WordType>(type) {
                @Override
                public String toString() {
                    return this.getData().getName();
                }
            });
        }
        cbType.setSelectedIndex(cbIndex);

        contentPanel.add(lbWord);
        contentPanel.add(txWord);
        contentPanel.add(lbType);
        contentPanel.add(cbType);
    }

    // 初始化按钮面板
    public void initButtonPanel() {
        buttonPanel = new JPanel();

        btnModify = new JButton("保存修改");
        btnModify.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.lightBlue));
        btnModify.setForeground(Color.white);
        btnModify.setFont(MyFont.Static);
        btnModify.addMouseListener(this);

        buttonPanel.add(btnModify);
    }

    // 鼠标点击事件
    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == btnModify) {

            String txValue = txWord.getText().trim();
            if (txValue.isEmpty()) {
                JOptionPane.showMessageDialog(null, "请输入关键词");
            } else {
                WordType type = ((JComboBoxItem<WordType>) cbType.getSelectedItem()).getData();
                word.setType(type);
                word.setWord(txValue);
                wordService.save(word);
                JOptionPane.showMessageDialog(null, "修改成功");
                this.setVisible(false);
                parentPanel.refreshTablePanel();
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

}
