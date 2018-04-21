package cn.huwhy.weibo.robot.view;

import cn.huwhy.interfaces.Paging;
import cn.huwhy.weibo.robot.model.BaseTableModule;
import cn.huwhy.weibo.robot.model.Member;
import cn.huwhy.weibo.robot.model.Word;
import cn.huwhy.weibo.robot.model.WordTerm;
import cn.huwhy.weibo.robot.model.WordType;
import cn.huwhy.weibo.robot.service.WordService;
import cn.huwhy.weibo.robot.util.JComboBoxItem;
import cn.huwhy.weibo.robot.util.ResourcesUtil;
import cn.huwhy.weibo.robot.util.SpringContentUtil;
import cn.huwhy.weibo.robot.util.Tools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

public class WordDataPanel extends JPanel implements ActionListener {
    private Logger logger = LoggerFactory.getLogger(getClass());
    // 定义全局组件
    JPanel topPanel, toolPanel, searchPanel, tablePanel, pagePanel;
    JComboBox cbType;
    BaseTableModule baseTableModule;
    JTable table;
    JScrollPane jScrollPane;
    JLabel lbType;
    JButton tool_add, tool_modify, tool_delete;

    private WordTerm term = new WordTerm();
    private WordService wordService;
    private AddWordJFrame addWordJFrame;
    private ModifyWordJFrame modifyWordJFrame;
    private Member member;
    private volatile boolean init;

    public WordDataPanel(Member member) {
        super(new BorderLayout());
        this.member = member;
        this.addWordJFrame = new AddWordJFrame(this);
        this.modifyWordJFrame = new ModifyWordJFrame();
        init();
    }

    public void init() {
        if (!init) {
            init = true;
            this.wordService = SpringContentUtil.getBean(WordService.class);
            initTopPanel();
            initTablePanel();
            initBottomPanel();
        }
    }

    // 初始化顶部面板
    public void initTopPanel() {

        topPanel = new JPanel(new BorderLayout());

        initToolPanel();
        initSearchPanel();

        add(topPanel, "North");
    }

    // 初始化工具面板
    public void initToolPanel() {

        toolPanel = new JPanel();
        // 工具图标
        tool_add = new JButton("添加敏感词");
        tool_add.setToolTipText("添加");
        tool_add.setActionCommand("addWord");
        tool_add.addActionListener(this);

        tool_modify = new JButton("修改敏感词");
        tool_modify.setToolTipText("修改");
        tool_modify.setActionCommand("chgWord");
        tool_modify.addActionListener(this);

        tool_delete = new JButton("删除敏感词");
        tool_delete.setToolTipText("删除");
        tool_delete.setActionCommand("delWord");
        tool_delete.addActionListener(this);

        toolPanel.add(tool_add);
        toolPanel.add(tool_modify);
        toolPanel.add(tool_delete);

        topPanel.add(toolPanel, "West");
    }

    // 初始化搜素条件面板
    public void initSearchPanel() {

        searchPanel = new JPanel();
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
        cbType.addActionListener(this);

        // 标签
        lbType = new JLabel("类型");

        searchPanel.add(lbType);
        searchPanel.add(cbType);

        topPanel.add(searchPanel, BorderLayout.EAST);
    }

    // 初始化数据表格面板
    public void initTablePanel() {
        String params[] = {"ID", "敏感词", "类型", "出现次数"};
        List<List<String>> data = loadData(1, WordType.BLACK);
        baseTableModule = new BaseTableModule(params, data);
        table = new JTable(baseTableModule);
        Tools.setTableStyle(table);
        jScrollPane = new JScrollPane(table);
        Tools.setJspStyle(jScrollPane);

        tablePanel = new JPanel(new BorderLayout());
        tablePanel.setOpaque(false);

        tablePanel.add(jScrollPane);

        add(tablePanel, "Center");
    }

    private List<List<String>> loadData(long page, WordType type) {
        List<List<String>> data = new ArrayList<>();//列表数据
        term.setPage(page);
        term.setType(type);
        term.setMemberId(this.member.getId());
        Paging<Word> paging = wordService.findWords(term);
        for (Word word : paging.getData()) {
            List<String> ll = new ArrayList<>();
            ll.add(Long.toString(word.getId()));
            ll.add(word.getWord());
            ll.add(word.getType().getName());
            ll.add(word.getHitNum() + "");
            data.add(ll);
        }
        this.term = (WordTerm) paging.getTerm();
        this.initBottomPanel();
        return data;
    }

    public void initBottomPanel() {
        if (pagePanel != null) {
            pagePanel.removeAll();
        } else {
            pagePanel = new JPanel();
            add(pagePanel, BorderLayout.SOUTH);
        }
        JLabel total = new JLabel("总共:" + term.getTotal() + "记录");
        pagePanel.add(total);
        for (int i = 1; i <= term.getTotalPage(); i++) {
            JButton btn = new JButton("" + i);
            btn.addActionListener(this);
            btn.setActionCommand("page");
            pagePanel.add(btn);
        }
    }

    // 更新数据表格
    protected void refreshTablePanel(long page, WordType type) {
        remove(tablePanel);
        String params[] = {"ID", "敏感词", "类型", "出现次数"};
        List<List<String>> data = loadData(page, type);
        baseTableModule = new BaseTableModule(params, data);
        table = new JTable(baseTableModule);
        Tools.setTableStyle(table);
        jScrollPane = new JScrollPane(table);
        Tools.setJspStyle(jScrollPane);

        tablePanel = new JPanel(new BorderLayout());
        tablePanel.setOpaque(false);

        tablePanel.add(jScrollPane);

        add(tablePanel, "Center");
        validate();
    }

    // 更新数据表格
    protected void refreshTablePanel() {
        remove(tablePanel);
        String params[] = {"ID", "敏感词", "类型", "出现次数"};
        List<List<String>> data = loadData(term.getPage(), term.getType());
        baseTableModule = new BaseTableModule(params, data);
        table = new JTable(baseTableModule);
        Tools.setTableStyle(table);
        jScrollPane = new JScrollPane(table);
        Tools.setJspStyle(jScrollPane);

        tablePanel = new JPanel(new BorderLayout());
        tablePanel.setOpaque(false);

        tablePanel.add(jScrollPane);

        add(tablePanel, "Center");
        validate();
    }

    // 下拉框改变事件
    @Override
    public void actionPerformed(ActionEvent e) {
        if ("addWord".equals(e.getActionCommand())) {
            this.addWordJFrame.setMember(member);
            this.addWordJFrame.init(this);
        } else if ("chgWord".equals(e.getActionCommand())) {
            int row = table.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(null, "请选择数据");
            } else {
                String id = (String) table.getValueAt(row, 0);
                this.modifyWordJFrame.init(this, Long.valueOf(id));
            }
        } else if ("delWord".equals(e.getActionCommand())) {
            int row = table.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(null, "请选择数据");
            } else {
                String id = (String) table.getValueAt(row, 0);
                int result = JOptionPane.showConfirmDialog(null, "是否确定删除？", "用户提示", JOptionPane.YES_NO_OPTION);
                if (result == 0) {
                    try {
                        this.wordService.delete(Long.valueOf(id));
                        JOptionPane.showMessageDialog(null, "删除成功！");
                        refreshTablePanel(1, null);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
            }
        } else if ("page".equals(e.getActionCommand())) {
            String page = ((JButton) e.getSource()).getText();
            logger.debug("page change: {} - {}", e.getActionCommand(), page);
            refreshTablePanel(Long.valueOf(page), null);
            //todo:  页面改变
        } else if (e.getSource() == cbType) {
            refreshTablePanel(1, ((JComboBoxItem<WordType>) cbType.getSelectedItem()).getData());
        }
    }

}
