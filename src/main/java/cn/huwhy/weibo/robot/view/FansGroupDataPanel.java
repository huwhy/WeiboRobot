package cn.huwhy.weibo.robot.view;

import cn.huwhy.interfaces.Paging;
import cn.huwhy.weibo.robot.model.BaseTableModule;
import cn.huwhy.weibo.robot.model.MyFans;
import cn.huwhy.weibo.robot.model.MyFansTerm;
import cn.huwhy.weibo.robot.model.WordType;
import cn.huwhy.weibo.robot.service.FansService;
import cn.huwhy.weibo.robot.util.JComboBoxItem;
import cn.huwhy.weibo.robot.util.MyFont;
import cn.huwhy.weibo.robot.util.ResourcesUtil;
import cn.huwhy.weibo.robot.util.SpringContentUtil;
import cn.huwhy.weibo.robot.util.Tools;
import org.jb2011.lnf.beautyeye.ch3_button.BEButtonUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

public class FansGroupDataPanel extends JPanel implements ActionListener, MouseListener {
    private Logger logger = LoggerFactory.getLogger(getClass());
    // 定义全局组件
    JPanel topPanel, toolPanel, searchPanel, tablePanel, pagePanel;
    JComboBox cbType;
    BaseTableModule baseTableModule;
    JTable table;
    JScrollPane jScrollPane;
    JLabel lbType;

    private MyFansTerm term = new MyFansTerm();
    private FansService fansService;
    private ModifyFansJFrame modifyFansJFrame;
    private int id;
    private volatile boolean init;

    public FansGroupDataPanel(int id) {
        this.id = id;
        setLayout(new BorderLayout());
        this.fansService = SpringContentUtil.getBean(FansService.class);
        this.modifyFansJFrame = new ModifyFansJFrame();
        init();
    }

    public void init() {
        if (!init) {
            init = true;
            initTopPanel();
            initTablePanel();
            initBottomPanel();
        } else {
            this.refreshTablePanel(1, null);
        }
    }

    // 初始化顶部面板
    public void initTopPanel() {

        topPanel = new JPanel(new BorderLayout());

        initToolPanel();
        initSearchPanel();

        add(topPanel, BorderLayout.NORTH);
    }

    // 初始化工具面板
    public void initToolPanel() {

        toolPanel = new JPanel();
        Icon iconAdd = new ImageIcon(ResourcesUtil.getImage("add.png"));
//        tool_add = new JLabel(iconAdd);
//        tool_add.setToolTipText("添加");
//        tool_add.addMouseListener(this);

        Icon iconModify = new ImageIcon(ResourcesUtil.getImage("modify.png"));
//        tool_modify = new JLabel(iconModify);
//        tool_modify.setToolTipText("修改");
//        tool_modify.addMouseListener(this);

        Icon iconDelete = new ImageIcon(ResourcesUtil.getImage("delete.png"));
//        tool_delete = new JLabel(iconDelete);
//        tool_delete.setToolTipText("删除");
//        tool_delete.addMouseListener(this);

        JButton btnRefresh = new JButton("刷新");
        btnRefresh.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.lightBlue));
        btnRefresh.setForeground(Color.white);
        btnRefresh.setFont(MyFont.Static);
        btnRefresh.setActionCommand("refresh");
        btnRefresh.addActionListener(this);

//        toolPanel.add(tool_add);
//        toolPanel.add(tool_modify);
//        toolPanel.add(tool_delete);
        toolPanel.add(btnRefresh);

        topPanel.add(toolPanel, BorderLayout.WEST);
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
        lbType = new JLabel("粉丝类型");
        lbType.setFont(MyFont.Static);

        searchPanel.add(lbType);
        searchPanel.add(cbType);

        topPanel.add(searchPanel, BorderLayout.EAST);
    }

    // 初始化数据表格面板
    public void initTablePanel() {
        String params[] = {"ID", "昵称", "主页", "头图", "类型"};
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

        add(tablePanel, "Center");
    }

    private List<List<String>> loadData(long page, WordType type) {
        List<List<String>> data = new ArrayList<>();//列表数据
        term.setPage(page);
        term.setType(type);
        term.setMemberId(id);
        Paging<MyFans> paging = fansService.findMyFans(term);
        for (MyFans fans : paging.getData()) {
            List<String> ll = new ArrayList<>();
            ll.add(Long.toString(fans.getId()));
            ll.add(fans.getNick());
            ll.add(fans.getHome());
            ll.add(fans.getHeadImg());
            ll.add(fans.getType().getName());
            data.add(ll);
        }
        this.term = (MyFansTerm) paging.getTerm();
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
            btn.setFont(MyFont.Static);
            pagePanel.add(btn);
        }
    }

    // 更新数据表格
    protected void refreshTablePanel(long page, WordType type) {
        remove(tablePanel);
        String params[] = {"ID", "昵称", "主页", "头图", "类型"};
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
        String params[] = {"ID", "昵称", "主页", "头图", "类型"};
        List<List<String>> data = loadData(term.getPage(), null);
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
        if ("page".equals(e.getActionCommand())) {
            String page = ((JButton) e.getSource()).getText();
            logger.debug("page change: {} - {}", e.getActionCommand(), page);
            refreshTablePanel(Long.valueOf(page), null);
            //todo:  页面改变
        } else if (e.getSource() == cbType) {
            refreshTablePanel(1, ((JComboBoxItem<WordType>) cbType.getSelectedItem()).getData());
        } else if (e.getActionCommand().equals("refresh")) {
            refreshTablePanel();
        }
    }

    // 鼠标点击事件
    @Override
    public void mouseClicked(MouseEvent e) {
//        if (e.getSource() == tool_modify) {
//            int row = table.getSelectedRow();
//            if (row < 0) {
//                JOptionPane.showMessageDialog(null, "请选择数据");
//            } else {
//                String id = (String) table.getValueAt(row, 0);
//                this.modifyFansJFrame.init(this, Long.valueOf(id));
//            }
//
//        }
    }

    // 鼠标划入事件
    @Override
    public void mouseEntered(MouseEvent e) {
//        if (e.getSource() == tool_add) {
//            tool_add.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
//        } else if (e.getSource() == tool_modify) {
//            tool_modify.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
//        } else if (e.getSource() == tool_delete) {
//            tool_delete.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
//        }
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
