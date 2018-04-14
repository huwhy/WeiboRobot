package cn.huwhy.weibo.robot.view;

import cn.huwhy.interfaces.Paging;
import cn.huwhy.interfaces.Term;
import cn.huwhy.weibo.robot.model.BaseTableModule;
import cn.huwhy.weibo.robot.util.JComboBoxItem;
import cn.huwhy.weibo.robot.util.ResourcesUtil;
import cn.huwhy.weibo.robot.util.Tools;

import javax.swing.*;
import javax.swing.table.DefaultTableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;


public class FormTablePanel implements ActionListener, MouseListener {

	// 定义全局组件
	JPanel backgroundPanel, topPanel, toolPanel, searchPanel, tablePanel, pagePanel;
	JComboBox select_category, select_warehouse;
	BaseTableModule baseTableModule;
	JTable table;
	JScrollPane jScrollPane;
	JLabel label_category, label_warehouse, tool_add, tool_modify, tool_delete;

	private Paging paging;

	public FormTablePanel() {

		backgroundPanel = new JPanel(new BorderLayout());

		initTopPanel();
		initTablePanel();
		initBottomPanel();
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
		tool_add.setToolTipText("新建商品");
		tool_add.addMouseListener(this);

		Icon icon_modify = new ImageIcon(ResourcesUtil.getImage("modify.png"));
		tool_modify = new JLabel(icon_modify);
//		tool_modify.setText("修改商品");
		tool_modify.setToolTipText("修改商品");
		tool_modify.addMouseListener(this);

		Icon icon_delete = new ImageIcon(ResourcesUtil.getImage("delete.png"));
		tool_delete = new JLabel(icon_delete);
		tool_delete.setToolTipText("删除商品");
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
		select_category = new JComboBox();
		List<Object[]> list_category = null;
		try {
			list_category = new ArrayList<>();
		} catch (Exception e) {
			e.printStackTrace();
		}
		select_category.addItem(new JComboBoxItem<String>("全部"));
		if (list_category != null) {
			for (Object[] object : list_category) {
				select_category.addItem(new JComboBoxItem((String) object[0]));
			}
		}
		select_category.addActionListener(this);

		// 仓库下拉框
		select_warehouse = new JComboBox();
		List<Object[]> list_warehouse = null;
		try {
			list_warehouse = new ArrayList<>();
		} catch (Exception e) {
			e.printStackTrace();
		}
		select_warehouse.addItem(new JComboBoxItem("全部"));
		if (list_warehouse != null) {
			for (Object[] object : list_warehouse) {
				select_warehouse.addItem(new JComboBoxItem((String) object[0]));
			}
		}
		select_warehouse.addActionListener(this);

		// 标签
		label_category = new JLabel("商品种类");
		label_warehouse = new JLabel("所属仓库");

		searchPanel.add(label_category);
		searchPanel.add(select_category);
		searchPanel.add(label_warehouse);
		searchPanel.add(select_warehouse);

		topPanel.add(searchPanel, "East");
	}

	// 初始化数据表格面板
	public void initTablePanel() {

		String params[] = { "商品id", "名称", "价格", "产地", "所属分类", "所属仓库", "库存", "仓库id", "分类id" };
		List<List<String>> data = new ArrayList<>();//列表数据
		paging = new Paging(new Term() {
			{
				setTotal(100L);
			}
		});
		baseTableModule = new BaseTableModule(params, data);

		table = new JTable(baseTableModule);
		Tools.setTableStyle(table);
		DefaultTableColumnModel dcm = (DefaultTableColumnModel) table.getColumnModel();// 获取列模型
		dcm.getColumn(0).setMinWidth(0); // 将第一列的最小宽度、最大宽度都设置为0
		dcm.getColumn(0).setMaxWidth(0);
		dcm.getColumn(7).setMinWidth(0); // 将第8列的最小宽度、最大宽度都设置为0
		dcm.getColumn(7).setMaxWidth(0);
		dcm.getColumn(8).setMinWidth(0); // 将第9列的最小宽度、最大宽度都设置为0
		dcm.getColumn(8).setMaxWidth(0);

		jScrollPane = new JScrollPane(table);
		Tools.setJspStyle(jScrollPane);

		tablePanel = new JPanel(new BorderLayout());
		tablePanel.setOpaque(false);

		tablePanel.add(jScrollPane);

		backgroundPanel.add(tablePanel, "Center");
	}

	public void initBottomPanel() {
		pagePanel = new JPanel();
		JLabel total = new JLabel("总共:" + paging.getTerm().getTotal());
		pagePanel.add(total);
		for (int i = 0; i < paging.getTerm().getTotalPage(); i++) {
			JButton btn = new JButton("" + (i + 1));
			btn.addMouseListener(this);
			pagePanel.add(btn);
		}
		backgroundPanel.add(pagePanel, BorderLayout.SOUTH);
	}

	// 更新数据表格
	public void refreshTablePanel() {

		backgroundPanel.remove(tablePanel);
		String params[] = { "商品id", "名称", "价格", "产地", "所属分类", "所属仓库", "库存", "仓库id", "分类id" };
		List<List<String>> vector = new ArrayList<>();
		baseTableModule = new BaseTableModule(params, vector);

		table = new JTable(baseTableModule);
		Tools.setTableStyle(table);
		DefaultTableColumnModel dcm = (DefaultTableColumnModel) table.getColumnModel();// 获取列模型
		dcm.getColumn(0).setMinWidth(0); // 将第一列的最小宽度、最大宽度都设置为0
		dcm.getColumn(0).setMaxWidth(0);
		dcm.getColumn(7).setMinWidth(0); // 将第8列的最小宽度、最大宽度都设置为0
		dcm.getColumn(7).setMaxWidth(0);
		dcm.getColumn(8).setMinWidth(0); // 将第9列的最小宽度、最大宽度都设置为0
		dcm.getColumn(8).setMaxWidth(0);

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
		if (e.getSource() == select_category) {
			refreshTablePanel();
		} else if (e.getSource() == select_warehouse) {
			refreshTablePanel();
		}
	}

	// 鼠标点击事件
	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getSource() == tool_add) {
//			new AddGoodsJFrame(this);
		} else if (e.getSource() == tool_modify) {
			int row = table.getSelectedRow();
			if (row < 0) {
				JOptionPane.showMessageDialog(null, "请选择商品");
			} else {
//				new ModifyWordJFrame(this, table, row);
			}

		} else if (e.getSource() == tool_delete) {
			int row = table.getSelectedRow();
			if (row < 0) {
				JOptionPane.showMessageDialog(null, "请选择商品");
			} else {
				String id = (String) table.getValueAt(row, 0);
				int result = JOptionPane.showConfirmDialog(null, "是否确定删除？", "用户提示", JOptionPane.YES_NO_OPTION);
				if (result == 0) {
//					String[] params = { id };
//					GoodsServiceImpl goodsService = new GoodsServiceImpl();
					try {
						int tempresult = 1;
						if (tempresult > 0) {
							JOptionPane.showMessageDialog(null, "商品删除成功！");
							refreshTablePanel();
						}
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
