package rent;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.MessageFormat;
import java.util.Enumeration;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.ExpandVetoException;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

@SuppressWarnings("serial")
class TreePanel extends JPanel implements TreeSelectionListener,
	TreeModelListener, ActionListener, TreeWillExpandListener {

    private RentManager rentManager;
    private JTree tree;
    private SearchField searchField;
    private JButton b_search;
    private JButton b_add;
    private JButton b_remove;
    private AddElementTabbedPane addElement;
    private boolean isChangingSelection = false;
    private Object currSelection = null;

    public TreePanel() {
	rentManager = RentManager.rm;
	setLayout(new GridBagLayout());
	setPreferredSize(new Dimension(150, 700));
	setMinimumSize(new Dimension(120, 400));
	addElement = new AddElementTabbedPane();
	b_search = new JButton(rentManager.searchIcon);
	searchField = new SearchField(b_search);
	b_search.setMargin(new Insets(0, 0, 0, 0));
	b_search.setBorder(null);
	b_search.setToolTipText(RentManager.rm.getString("tip.search"));
	b_search.addActionListener(this);
	b_add = new JButton(rentManager.addIcon);
	b_add.addActionListener(this);
	b_add.setMargin(new Insets(0, 0, 0, 0));
	b_add.setBorder(null);
	b_add.setToolTipText(rentManager.getString("tip.add"));
	b_remove = new JButton(rentManager.removeIcon);
	b_remove.addActionListener(this);
	b_remove.setMargin(new Insets(0, 0, 0, 0));
	b_remove.setBorder(null);
	b_remove.setToolTipText(rentManager.getString("tip.remove"));
	JPanel top = new JPanel(new GridBagLayout());
	GridBagConstraints c = new GridBagConstraints();
	c.gridx = 0;
	c.gridy = 0;
	c.insets = new Insets(5, 5, 5, 5);
	c.weightx = 1;
	c.weighty = 1;
	c.fill = GridBagConstraints.HORIZONTAL;
	top.add(searchField, c);

	c = new GridBagConstraints();
	c.gridx = 1;
	c.gridy = 0;
	c.weightx = 0;
	c.weighty = 1;
	c.insets = new Insets(5, 0, 5, 5);
	top.add(b_search, c);

	c = new GridBagConstraints();
	c.gridx = 2;
	c.gridy = 0;
	c.weightx = 0;
	c.weighty = 1;
	c.insets = new Insets(5, 0, 5, 5);
	top.add(b_add, c);

	c = new GridBagConstraints();
	c.gridx = 3;
	c.gridy = 0;
	c.weightx = 0;
	c.weighty = 1;
	c.insets = new Insets(5, 0, 5, 5);
	top.add(b_remove, c);

	DefaultTreeModel treeModel = new DefaultTreeModel(rentManager.getRoot());
	treeModel.addTreeModelListener(this);
	tree = new JTree(treeModel);
	tree.addTreeSelectionListener(this);
	tree.getSelectionModel().setSelectionMode(
		TreeSelectionModel.SINGLE_TREE_SELECTION);
	tree.addTreeWillExpandListener(this);
	tree.setCellRenderer(new DefaultTreeCellRenderer() {
	    public Component getTreeCellRendererComponent(JTree tree,
		    Object value, boolean selected, boolean expanded,
		    boolean leaf, int row, boolean hasFocus) {
		super.getTreeCellRendererComponent(tree, value, selected,
			expanded, leaf, row, hasFocus);
		MutableTreeNode node = (MutableTreeNode) value;
		if (node instanceof Building) {
		    setIcon(node.isLeaf() ? rentManager.buildingEmptyIcon
			    : rentManager.buildingSmallIcon);
		} else if (node instanceof Room) {
		    setIcon(node.isLeaf() ? rentManager.roomEmptyIcon
			    : rentManager.roomSmallIcon);
		} else if (node instanceof Building.Story) {
		    setIcon(rentManager.floorIcon);
		} else if (node instanceof Tenant) {
		    setIcon(rentManager.tenantSmallIcon);
		}
		return this;
	    }
	});
	JScrollPane treeView = new JScrollPane(tree);
	c = new GridBagConstraints();
	c.gridx = 0;
	c.gridy = 0;
	c.weightx = 1;
	c.weighty = 0;
	c.fill = GridBagConstraints.HORIZONTAL;
	add(top, c);

	c = new GridBagConstraints();
	c.gridx = 0;
	c.gridy = 1;
	c.weighty = 1;
	c.weightx = 1;
	c.insets = new Insets(0, 5, 5, 5);
	c.fill = GridBagConstraints.BOTH;
	add(treeView, c);
    }

    public JTree getTree() {
	return tree;
    }

    public void valueChanged(TreeSelectionEvent e) {
	TreePath newPath = e.getNewLeadSelectionPath();
	if (newPath == null) {
	    rentManager.getInfoPanel().switchPanel(null);
	    currSelection = null;
	    return;
	}
	isChangingSelection = true;
	final Object source = e.getNewLeadSelectionPath()
		.getLastPathComponent();
	currSelection = source;
	rentManager.getTablePanel().switchElement(source);
	SwingUtilities.invokeLater(new Runnable() {
	    public void run() {
		rentManager.getInfoPanel().switchPanel(source);
	    }
	});
	isChangingSelection = false;
    }

    public boolean isChangingSelection() {
	return isChangingSelection;
    }

    public void actionPerformed(ActionEvent e) {
	Object source = e.getSource();
	if (source.equals(b_search)) {
	    searchField.search();
	    searchField.showMenu();
	} else if (source.equals(b_add)) {
	    if (!rentManager.isGlobalEditable()) {
		JLabel message = new JLabel("<html><p style='width:200px'>"
			+ rentManager.getString("message.not.editable")
			+ "</html>");
		JOptionPane.showMessageDialog(rentManager, message);
		return;
	    }
	    if (currSelection == null
		    || currSelection instanceof DefaultMutableTreeNode) {
		addElement.setSelectedIndex(AddElementTabbedPane.BLDG_PANEL);
	    } else if (currSelection instanceof Building
		    || currSelection instanceof Building.Story) {
		addElement.setSelectedIndex(AddElementTabbedPane.ROOM_PANEL);
		addElement.getRoomPanel().updateData(currSelection);
	    } else if (currSelection instanceof Room
		    || currSelection instanceof Tenant) {
		addElement.setSelectedIndex(AddElementTabbedPane.TENANT_PANEL);
		addElement.getTenantPanel().updateData(currSelection);
		if (!searchField.getText().equals("")) {
		    addElement.getTenantPanel().setTenantName(
			    searchField.getText());
		}
	    }
	    int option = JOptionPane.showConfirmDialog(rentManager, addElement,
		    rentManager.getString("title.add.element.pane"),
		    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
	    if (option == JOptionPane.OK_OPTION) {
		ElementsAdder adderPane = (ElementsAdder) addElement
			.getSelectedComponent();
		if (adderPane.addElement()) {
		    MutableTreeNode newElement = adderPane.getNewElement();
		    RentManager.logger.info("New element "
			    + newElement.getClass().getSimpleName() + " ["
			    + newElement.toString() + "] is added to "
			    + newElement.getParent().getClass().getSimpleName() + " ["
			    + newElement.getParent().toString() + "].");
		    ((DefaultTreeModel) tree.getModel()).reload(adderPane
			    .getNewElement().getParent());
		    if (adderPane instanceof AddBuildingPanel) {
			addElement.getRoomPanel().updateData(newElement);
			addElement.getTenantPanel().updateData(newElement);
		    } else if (adderPane instanceof AddRoomPanel) {
			addElement.getTenantPanel().updateData(newElement);
		    }
		    tree.scrollPathToVisible(getPathToRoot(newElement));
		    rentManager.setHasModified(true);
		}
	    }
	} else if (source.equals(b_remove)) {
	    if (!rentManager.isGlobalEditable()) {
		JLabel message = new JLabel("<html><p style='width:200px'>"
			+ rentManager.getString("message.not.editable")
			+ "</html>");
		JOptionPane.showMessageDialog(rentManager, message);
		return;
	    }
	    if (tree.getSelectionPath() != null) {
		MutableTreeNode element = (MutableTreeNode) tree
			.getSelectionPath().getLastPathComponent();
		if (element.getParent() == null)
		    return;
		String className = "";
		if (element instanceof Building)
		    className = rentManager.getString("class.bldg");
		else if (element instanceof Building.Story)
		    className = rentManager.getString("class.story");
		else if (element instanceof Room)
		    className = rentManager.getString("class.room");
		else if (element instanceof Tenant)
		    className = rentManager.getString("class.tenant");
		String message = MessageFormat.format(
			rentManager.getString("message.remove"), className,
			element.toString());
		int result = JOptionPane.showConfirmDialog(rentManager,
			"<html><body><p style='width:200px'>" + message
				+ "</p></body></html>", "Message",
			JOptionPane.YES_NO_OPTION);

		if (result == JOptionPane.OK_OPTION) {
		    RentManager.logger.info("Element "
			    + element.getClass().getSimpleName() + " ["
			    + element.toString()
			    + "] and its children are removed.");
		    if (className.equals(rentManager.getString("class.bldg"))) {
			rentManager.getTablePanel().removeTable(
				(Building) element);
			((DefaultTreeModel) tree.getModel())
				.removeNodeFromParent(element);
			addElement.getRoomPanel().updateData(null);
			addElement.getTenantPanel().updateData(null);
		    } else if (className.equals(rentManager
			    .getString("class.story"))) {
			rentManager.getTablePanel().removeStory(
				(Building.Story) element);
			((DefaultTreeModel) tree.getModel())
				.removeNodeFromParent(element);
			addElement.getTenantPanel().updateData(null);
		    } else if (className.equals(rentManager
			    .getString("class.room"))) {
			rentManager.getTablePanel().removeRoom((Room) element);
			((DefaultTreeModel) tree.getModel())
				.removeNodeFromParent(element);
			addElement.getTenantPanel().updateData(null);
		    } else if (className.equals(rentManager
			    .getString("class.tenant"))) {
			rentManager.getTablePanel().removeTenant(
				(Tenant) element);
			((DefaultTreeModel) tree.getModel())
				.removeNodeFromParent(element);
		    }
		    rentManager.setHasModified(true);
		}
	    }
	}

    }

    public static TreePath getPathToRoot(TreeNode aNode) {
	return new TreePath(getPathToRoot(aNode, 0));
    }

    private static TreeNode[] getPathToRoot(TreeNode aNode, int depth) {
	TreeNode[] nodes;
	if (aNode == null) {
	    nodes = new TreeNode[depth];
	} else {
	    depth++;
	    nodes = getPathToRoot(aNode.getParent(), depth);
	    nodes[nodes.length - depth] = aNode;
	}
	return nodes;
    }

    @Override
    public void treeNodesChanged(final TreeModelEvent e) {
    }

    @Override
    public void treeNodesInserted(final TreeModelEvent e) {
	SwingUtilities.invokeLater(new Runnable() {
	    public void run() {
		Object child = e.getChildren()[0];
		tree.setSelectionPath(e.getTreePath().pathByAddingChild(child));
	    }
	});
    }

    @Override
    public void treeNodesRemoved(TreeModelEvent e) {
    }

    @Override
    public void treeStructureChanged(TreeModelEvent e) {
    }

    @Override
    public void treeWillExpand(TreeExpansionEvent event)
	    throws ExpandVetoException {
    }

    private boolean isCollapsing;

    @Override
    public void treeWillCollapse(TreeExpansionEvent event)
	    throws ExpandVetoException {
	if (!isCollapsing) {
	    isCollapsing = true;
	    collapseAll((TreeNode) event.getPath().getLastPathComponent());
	    isCollapsing = false;
	}
    }

    @SuppressWarnings("rawtypes")
    private void collapseAll(TreeNode aNode) {
	if (aNode.getAllowsChildren()) {
	    Enumeration children = aNode.children();
	    while (children.hasMoreElements()) {
		TreeNode child = (TreeNode) children.nextElement();
		collapseAll(child);
	    }
	    tree.collapsePath(getPathToRoot(aNode));
	}
    }

}