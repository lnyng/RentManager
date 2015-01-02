package rent;

import java.awt.Color;
import java.awt.Component;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumnModel;
import javax.swing.text.JTextComponent;
import javax.swing.tree.TreePath;

@SuppressWarnings("serial")
public class BillTable extends JTable implements Popuppable {
    private RentManager rentManager;

    public BillTable(Building bldg) {
	super(new BillTableModel(bldg));
	PopupManager.sharedInstance().registerComponent(this);
	rentManager = RentManager.rm;
	setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	setRowHeight(20);
	getTableHeader().setReorderingAllowed(false);
	DefaultTableCellRenderer midRenderer = new DefaultTableCellRenderer();
	midRenderer.setHorizontalAlignment(SwingConstants.CENTER);
	TableColumnModel columnModel = getColumnModel();
	for (int i = 0; i < columnModel.getColumnCount(); i++) {
	    if (i != BillTableModel.AC && i != BillTableModel.INTERNET)
		columnModel.getColumn(i).setCellRenderer(midRenderer);
	}

	UtilCellRenderer utilRenderer = new UtilCellRenderer();
	columnModel.getColumn(BillTableModel.WATER).setCellRenderer(
		utilRenderer);
	columnModel.getColumn(BillTableModel.ELECTRICITY).setCellRenderer(
		utilRenderer);

	setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	BillTableListener btl = new BillTableListener();
	getModel().addTableModelListener(btl);
	getSelectionModel().addListSelectionListener(btl);
    }

    class UtilCellRenderer extends DefaultTableCellRenderer {
	private int previousRow = -1;
	private int previousCol = -1;

	public Component getTableCellRendererComponent(JTable table,
		Object value, boolean isSelected, boolean hasFocus, int row,
		int col) {
	    JLabel l = (JLabel) super.getTableCellRendererComponent(table,
		    value, isSelected, hasFocus, row, col);
	    BillTableModel btm = getModel();
	    Object[] r = btm.getRow(row);
	    Room room = (Room) r[BillTableModel.ROOM];
	    if (col == BillTableModel.WATER) {
		l.setToolTipText(rentManager.getString("tip.table.utilities")
			+ room.getWaterRecord());
		if (r[col].equals(room.getWaterRecord()))
		    l.setBackground(new Color(252, 188, 188));
		else
		    l.setBackground(new Color(160, 230, 180));
		if (previousRow != row || previousCol != col) {
		    ToolTipManager.sharedInstance().setEnabled(false);
		    ToolTipManager.sharedInstance().setEnabled(true);
		    previousRow = row;
		    previousCol = col;
		}
	    } else if (col == BillTableModel.ELECTRICITY) {
		l.setToolTipText(rentManager.getString("tip.table.utilities")
			+ room.getElectricityRecord());
		if (r[col].equals(room.getElectricityRecord()))
		    l.setBackground(new Color(252, 188, 188));
		else
		    l.setBackground(new Color(160, 230, 180));
		if (previousRow != row || previousCol != col) {
		    ToolTipManager.sharedInstance().setEnabled(false);
		    ToolTipManager.sharedInstance().setEnabled(true);
		    previousRow = row;
		    previousCol = col;
		}
	    }
	    l.setHorizontalAlignment(SwingUtilities.CENTER);
	    return l;
	}
    }

    public Component prepareEditor(TableCellEditor editor, int row, int column) {
	Object value = getValueAt(row, column);
	boolean isSelected = isCellSelected(row, column);
	Component comp = editor.getTableCellEditorComponent(this, value,
		isSelected, row, column);
	if (comp instanceof JTextComponent)
	    ((JTextComponent) comp).selectAll();
	return comp;
    }

    public boolean getScrollableTracksViewportWidth() {
	return getPreferredSize().width < getParent().getWidth();
    }

    public BillTableModel getModel() {
	return (BillTableModel) super.getModel();
    }

    class BillTableListener implements TableModelListener,
	    ListSelectionListener {
	private boolean isChangingTable = false;

	@Override
	public void tableChanged(TableModelEvent e) {
	    if (isChangingTable)
		return;
	    isChangingTable = true;
	    int firstRow = e.getFirstRow();
	    int col = e.getColumn();
	    BillTableModel model = (BillTableModel) e.getSource();
	    if (e.getType() == TableModelEvent.UPDATE
		    && col != BillTableModel.TOTAL) {
		Object[] row = model.getRow(firstRow);
		Room room = (Room) row[BillTableModel.ROOM];
		if (col == BillTableModel.INTERNET) {
		    room.setUseInternet((boolean) row[col]);
		} else if (col == BillTableModel.AC) {
		    room.setUseAC((boolean) row[col]);
		} else if (col == BillTableModel.WATER) {
		    int waterUsed = (int) row[col] - room.getWaterRecord();
		    if (waterUsed < 0) {
			String message = rentManager
				.getString("message.water.record.error");
			JOptionPane.showMessageDialog(BillTable.this, message);
			row[col] = room.getWaterRecord();
			model.fireTableCellUpdated(firstRow, col);
			isChangingTable = false;
			return;
		    }
		    room.setWaterUsed(waterUsed);
		} else if (col == BillTableModel.ELECTRICITY) {
		    int electricityUsed = (int) row[col]
			    - room.getElectricityRecord();
		    if (electricityUsed < 0) {
			String message = rentManager
				.getString("message.elect.record.error");
			JOptionPane.showMessageDialog(BillTable.this, message);
			row[col] = room.getElectricityRecord();
			model.fireTableCellUpdated(firstRow, col);
			isChangingTable = false;
			return;
		    }
		    room.setElectricityUsed(electricityUsed);
		} else if (col == BillTableModel.OTHER_FEE) {
		    int otherFee = (int) row[col];
		    if (otherFee < 0) {
			row[col] = room.getOtherFee();
			model.fireTableCellUpdated(firstRow, col);
			isChangingTable = false;
			return;
		    }
		    room.setOtherFee(otherFee);
		} else if (col == BillTableModel.CLEANING) {
		    int cleaningPrice = (int) row[col];
		    if (cleaningPrice < 0) {
			row[col] = room.getCleaningPrice();
			model.fireTableCellUpdated(firstRow, col);
			isChangingTable = false;
			return;
		    }
		    room.setCleaningPrice(cleaningPrice);
		}
		if (col != -1)
		    RentManager.logger.info("Bill table ["
			    + room.getBuilding().getName() + "] changed. (Row "
			    + firstRow + ", Col " + col + ") -> "
			    + row[col].toString());
		rentManager.getTreePanel().getTree()
			.setSelectionPath(TreePanel.getPathToRoot(room));
		rentManager.getInfoPanel().updateCurrPanel();
		if (!model.getValueAt(firstRow, BillTableModel.TENANT).equals(
			RentManager.nullTenant))
		    model.updateTotal(firstRow);
	    }
	    rentManager.setHasModified(true);
	    isChangingTable = false;
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
	    if (!e.getValueIsAdjusting()
		    && !rentManager.getTreePanel().isChangingSelection()) {
		if (getModel().getRowCount() == 0)
		    return;
		ListSelectionModel lsm = (ListSelectionModel) e.getSource();
		int rowIndex = 0;
		for (int i = e.getFirstIndex(); i <= e.getLastIndex(); i++)
		    if (lsm.isSelectedIndex(i)) {
			rowIndex = i;
			break;
		    }
		Object[] row = getModel().getRow(rowIndex);
		Room room = (Room) row[BillTableModel.ROOM];
		TreePath path = TreePanel.getPathToRoot(room);
		rentManager.getTreePanel().getTree().setSelectionPath(path);
	    }
	}
    }

    /**
     * A method similar to the getToolTipText
     */
    @Override
    public String getPopupText(MouseEvent event) {
	String text = null;
	Point p = event.getPoint();

	// Locate the renderer under the event location
	int hitColumnIndex = columnAtPoint(p);
	int hitRowIndex = rowAtPoint(p);

	if ((hitColumnIndex != -1) && (hitRowIndex != -1)) {
	    if (hitColumnIndex == BillTableModel.OTHER_FEE) {
		BillTableModel btm = getModel();
		Object[] r = btm.getRow(hitRowIndex);
		Room room = (Room) r[BillTableModel.ROOM];
		text = room.getOtherFeeNote();
	    }
	}
	return text;
    }

    @Override
    public Rectangle getActivatedRegion(MouseEvent event) {
	Rectangle rect = null;
	Point p = event.getPoint();

	// Locate the renderer under the event location
	int hitColumnIndex = columnAtPoint(p);
	int hitRowIndex = rowAtPoint(p);

	if ((hitColumnIndex != -1) && (hitRowIndex != -1)) {
	    if (hitColumnIndex == BillTableModel.OTHER_FEE) {
		rect = getCellRect(hitRowIndex, hitColumnIndex, true);
	    }
	}
	return rect;
    }

    @Override
    public JComponent getComponent() {
	return this;
    }

    @Override
    public void feedBack(Rectangle rect, Object feedBack) {
	Point p = rect.getLocation();
	int hitRowIndex = rowAtPoint(p);
	Room room = (Room) getModel().getRow(hitRowIndex)[BillTableModel.ROOM];
	if (feedBack.equals(room.getOtherFeeNote()))
	    return;
	RentManager.logger.info("Room [" + room.toString()
		+ "] has its note for the other fee updated.");
	room.setOtherFeeNote((String) feedBack);
	rentManager.setHasModified(true);
    }

}
