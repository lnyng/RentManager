package rent;
import java.awt.BorderLayout;
import java.awt.Rectangle;
import java.util.Enumeration;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

@SuppressWarnings("serial")
public class TablePanel extends JPanel implements ChangeListener {

    private RentManager rentManager;
    private JTabbedPane bldgTabbedPane;
    private boolean isInitializing;
    private boolean isSwitchingElement;

    @SuppressWarnings("unchecked")
    public TablePanel() {
	rentManager = RentManager.rm;
	bldgTabbedPane = new JTabbedPane();
	bldgTabbedPane.addChangeListener(this);
	this.setLayout(new BorderLayout());
	isInitializing = true;
	Enumeration<Building> e_bldg = rentManager.getRoot().children();
	while (e_bldg.hasMoreElements()) {
	    Building bldg = (Building) e_bldg.nextElement();
	    addTable(bldg);
	}
	isInitializing = false;
	this.add(bldgTabbedPane, BorderLayout.CENTER);
    }

    public void refresh() {
	for (int i = 0; i < bldgTabbedPane.getTabCount(); i++) {
	    BillTableModel btm = getTable(i).getModel();
	    for (int j = 0; j < btm.getRowCount(); j++)
		btm.fireTableRowsUpdated(j, j);
	}
    }

    public JTabbedPane getTabbedPane() {
	return bldgTabbedPane;
    }

    public BillTable getTable(int index) {
	JScrollPane sp = (JScrollPane) bldgTabbedPane.getComponentAt(index);
	JViewport vp = sp.getViewport();
	BillTable t = (BillTable) vp.getView();
	return t;
    }

    public void switchElement(Object element) {
	isSwitchingElement = true;
	if (element instanceof Building) {
	    Building bldg = (Building) element;
	    int index = rentManager.getRoot().getIndex(bldg);
	    if (bldgTabbedPane.getSelectedIndex() != index) {
		bldgTabbedPane.setSelectedIndex(index);
	    }
	} else if (element instanceof Room) {
	    Room room = (Room) element;
	    switchElement(room.getBuilding());
	    int bldgIndex = rentManager.getRoot().getIndex(room.getBuilding());
	    BillTable table = getTable(bldgIndex);
	    int roomRow = table.getModel().getRoomRow(room);
	    if (table.getSelectedRow() != roomRow) {
		table.setRowSelectionInterval(roomRow, roomRow);
		table.scrollRectToVisible(new Rectangle(table.getCellRect(
			roomRow, 0, true)));
	    }
	} else if (element instanceof Tenant) {
	    switchElement(((Tenant) element).getParent());
	}
	isSwitchingElement = false;
    }

    public void addTable(Building bldg) {
	int bldgIndex = ((DefaultMutableTreeNode) bldg.getParent())
		.getIndex(bldg);
	JTable newTable = new BillTable(bldg);
	JScrollPane sp = new JScrollPane(newTable,
		JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
		JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	bldgTabbedPane.insertTab(bldg.toString(), null, sp,
		rentManager.getString("tip.bldg"), bldgIndex);
	bldgTabbedPane.setSelectedIndex(bldgIndex);
    }

    public void removeTable(Building bldg) {
	int index = rentManager.getRoot().getIndex(bldg);
	bldgTabbedPane.removeTabAt(index);
    }

    public void addRoom(Room room, boolean isOnNewStory) {
	int bldgIndex = rentManager.getRoot().getIndex(room.getBuilding());
	BillTable table = getTable(bldgIndex);
	table.getModel().addRoom(room, isOnNewStory);
	if (bldgTabbedPane.getSelectedIndex() != bldgIndex)
	    bldgTabbedPane.setSelectedIndex(bldgIndex);
	int roomRow = table.getModel().getRoomRow(room);
	table.scrollRectToVisible(new Rectangle(table.getCellRect(roomRow, 0,
		true)));
	table.setRowSelectionInterval(roomRow, roomRow);
    }

    public void removeStory(Building.Story story) {
	int bldgIndex = rentManager.getRoot().getIndex(story.getBuilding());
	getTable(bldgIndex).getModel().removeStory(story);
    }

    public void removeRoom(Room room) {
	int bldgIndex = rentManager.getRoot().getIndex(room.getBuilding());
	getTable(bldgIndex).getModel().removeRoom(room);
    }

    public void addTenant(Tenant tenant) {
	int bldgIndex = rentManager.getRoot().getIndex(tenant.getBuilding());
	getTable(bldgIndex).getModel().updateTenant(tenant);
    }

    public void removeTenant(Tenant tenant) {
	int bldgIndex = rentManager.getRoot().getIndex(tenant.getBuilding());
	getTable(bldgIndex).getModel().removeTenant(tenant);
    }

    @Override
    public void stateChanged(ChangeEvent e) {
	if (isSwitchingElement)
	    return;
	if (!isInitializing && e.getSource().equals(bldgTabbedPane)) {
	    int index = bldgTabbedPane.getSelectedIndex();
	    if (index == -1)
		return;
	    TreeNode bldg = (Building) rentManager.getRoot().getChildAt(index);
	    TreePath path = TreePanel.getPathToRoot(bldg);
	    rentManager.getTreePanel().getTree().setSelectionPath(path);
	}

    }
}
