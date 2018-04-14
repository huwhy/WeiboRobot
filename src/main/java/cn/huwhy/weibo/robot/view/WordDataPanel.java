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

@Component
@Lazy
public class WordDataPanel implements ActionListener, MouseListener {
    private Logger logger = LoggerFactory.getLogger(getClass());
    // 定义全局组件
    JPanel backgroundPanel, topPanel, toolPanel, searchPanel, tablePanel, pagePanel;
    JComboBox cbType;
    BaseTableModule baseTableModule;
    JTable table;
    JScrollPane jScrollPane;
    JLabel lbType, tool_add, tool_modify, tool_delete;

    private WordTerm term = new WordTerm();
    @Autowired
    private WordService wordService;
    @Autowired
    private AddWordJFrame addWordJFrame;
    @Autowired
    private ModifyWordJFrame modifyWordJFrame;
    private Member member;
    private volatile boolean init;

    public WordDataPanel() {
        backgroundPanel = new JPanel(new BorderLayout());
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public void init() {
        if (!init) {
            init = true;
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

        backgroundPanel.add(topPanel, "North");
    }

    // 初始化工具面板
    public void initToolPanel() {

        toolPanel = new JPanel();
        // 工具图标
        Icon icon_add = new ImageIcon(ResourcesUtil.getImage("add.png"));
        tool_add = new JLabel(icon_add);
        tool_add.setToolTipText("添加");
        tool_add.addMouseListener(this);

        Icon icon_modify = new ImageIcon(ResourcesUtil.getImage("modify.png"));
        tool_modify = new JLabel(icon_modify);
        tool_modify.setToolTipText("修改");
        tool_modify.addMouseListener(this);

        Icon icon_delete = new ImageIcon(ResourcesUtil.getImage("delete.png"));
        tool_delete = new JLabel(icon_delete);
        tool_delete.setToolTipText("删除");
        tool_delete.addMouseListener(this);

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
        String params[] = {"ID", "关键词", "类型"};
        List<List<String>> data = loadData(1, WordType.BLACK);
        baseTableModule = new BaseTableModule(params, data);
        table = new JTable(baseTableModule);
        Tools.setTableStyle(table);
//        DefaultTableColumnModel dcm = (DefaultTableColumnModel) table.getColumnModel();// 获取列模型
//        dcm.getColumn(0).setMinWidth(0); // 将第一列的最小宽度、最大宽度都设置为0
//        dcm.getColumn(0).setMaxWidth(0);
//        dcm.getColumn(1).setMinWidth(0); // 将第8列的最小宽度、最大宽度都设置为0
//        dcm.getColumn(1).setMaxWidth(0);
//        dcm.getColumn(2).setMinWidth(0); // 将第9列的最小宽度、最大宽度都设置为0
//        dcm.getColumn(2).setMaxWidth(0);
        jScrollPane = new JScrollPane(table);
        Tools.setJspStyle(jScrollPane);

        tablePanel = new JPanel(new BorderLayout());
        tablePanel.setOpaque(false);

        tablePanel.add(jScrollPane);

        backgroundPanel.add(tablePanel, "Center");
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
            backgroundPanel.add(pagePanel, BorderLayout.SOUTH);
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
        backgroundPanel.remove(tablePanel);
        String params[] = {"ID", "关键词", "类型"};
        List<List<String>> data = loadData(page, type);
        baseTableModule = new BaseTableModule(params, data);
        table = new JTable(baseTableModule);
        Tools.setTableStyle(table);
        jScrollPane = new JScrollPane(table);
        Tools.setJspStyle(jScrollPane);

        tablePanel = new JPanel(new BorderLayout());
        tablePanel.setOpaque(false);

        tablePanel.add(jScrollPane);

        backgroundPanel.add(tablePanel, "Center");
        backgroundPanel.validate();
    }

    // 更新数据表格
    protected void refreshTablePanel() {
        backgroundPanel.remove(tablePanel);
        String params[] = {"ID", "关键词", "类型"};
        List<List<String>> data = loadData(term.getPage(), term.getType());
        baseTableModule = new BaseTableModule(params, data);
        table = new JTable(baseTableModule);
        Tools.setTableStyle(table);
        jScrollPane = new JScrollPane(table);
        Tools.setJspStyle(jScrollPane);

        tablePanel = new JPanel(new BorderLayout());
        tablePanel.setOpaque(false);

        tablePanel.add(jScrollPane);

        backgroundPanel.add(tablePanel, "Center");
        backgroundPanel.validate();
    }

    // 下拉框改变事件
    @Override
    public void actionPerformed(ActionEvent e) {
        if ("page".equals(e.getActionCommand())) {
            String page = ((JButton) e.getSource()).getText();
            logger.debug("page change: {} - {}", e.getActionCommand(), page);
            refreshTablePanel(Long.valueOf(page), null);
            //todo:  页面改变
        } else if (e.getSource() == cbType) {
            refreshTablePanel(1, ((JComboBoxItem<WordType>) cbType.getSelectedItem()).getData());
        }
    }

    // 鼠标点击事件
    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == tool_add) {
            this.addWordJFrame.setMember(member);
            this.addWordJFrame.init(this);
        } else if (e.getSource() == tool_modify) {
            int row = table.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(null, "请选择数据");
            } else {
                String id = (String) table.getValueAt(row, 0);
                this.modifyWordJFrame.init(this, Long.valueOf(id));
            }

        } else if (e.getSource() == tool_delete) {
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
        }

    }

    // 鼠标划入事件
    @Override
    public void mouseEntered(MouseEvent e) {
        if (e.getSource() == tool_add) {
            tool_add.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        } else if (e.getSource() == tool_modify) {
            tool_modify.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        } else if (e.getSource() == tool_delete) {
            tool_delete.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }
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
