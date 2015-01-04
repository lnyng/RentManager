package rent;

import java.awt.Component;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.text.MessageFormat;
import java.util.Enumeration;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.text.JTextComponent;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

@SuppressWarnings("serial")
public class BuildingInfoPanel extends ElementInfoPanel {
    private RentManager rentManager;
    private Building currBuilding;
    private JTextField tf_name = new JTextField(7);
    private JTextField tf_address = new JTextField(10);
    private JTextField tf_numberStories = new JTextField(3);
    private JTextField tf_electricityPrice = new JTextField(5);
    private JTextField tf_waterPrice = new JTextField(5);
    private JTextField tf_cleaningPrice = new JTextField(5);
    private JTextField tf_internetPrice = new JTextField(5);
    private JTextField tf_acPrice = new JTextField(5);
    private JTextArea ta_info = new JTextArea();
    private boolean isUpdatingInfo;

    public BuildingInfoPanel() {
	rentManager = RentManager.rm;
	BuildingInfoListener bil = new BuildingInfoListener();
	tf_name.addFocusListener(bil);
	tf_numberStories.addFocusListener(bil);
	tf_address.addFocusListener(bil);
	tf_electricityPrice.addFocusListener(bil);
	tf_waterPrice.addFocusListener(bil);
	tf_cleaningPrice.addFocusListener(bil);
	tf_internetPrice.addFocusListener(bil);
	tf_acPrice.addFocusListener(bil);

	tf_name.setMinimumSize(tf_name.getPreferredSize());
	tf_numberStories.setMinimumSize(tf_numberStories.getPreferredSize());
	tf_address.setMinimumSize(tf_address.getPreferredSize());
	tf_electricityPrice.setMinimumSize(tf_electricityPrice
		.getPreferredSize());
	tf_waterPrice.setMinimumSize(tf_waterPrice.getPreferredSize());
	tf_cleaningPrice.setMinimumSize(tf_cleaningPrice.getPreferredSize());
	tf_internetPrice.setMinimumSize(tf_internetPrice.getPreferredSize());
	tf_acPrice.setMinimumSize(tf_acPrice.getPreferredSize());

	JLabel l_icon = new JLabel(rentManager.buildingIcon);
	JLabel l_name = new JLabel(RentManagerMain.getString("label.bldg.name"));
	JLabel l_numberStories = new JLabel(
		RentManagerMain.getString("label.bldg.num.stories"));
	JLabel l_address = new JLabel(
		RentManagerMain.getString("label.bldg.address"));
	JLabel l_electricityPrice = new JLabel(
		RentManagerMain.getString("label.bldg.elect.price"));
	JLabel l_waterPrice = new JLabel(
		RentManagerMain.getString("label.bldg.water.price"));
	JLabel l_cleaningPrice = new JLabel(
		RentManagerMain.getString("label.bldg.clean.price"));
	JLabel l_internetPrice = new JLabel(
		RentManagerMain.getString("label.bldg.internet.price"));
	JLabel l_acPrice = new JLabel(
		RentManagerMain.getString("label.bldg.ac.price"));

	this.setLayout(new GridBagLayout());
	GridBagConstraints c = new GridBagConstraints();
	c.gridx = 0;
	c.gridy = 0;
	c.gridheight = 4;
	c.weightx = 0.1;
	c.weighty = 1;
	c.insets = new Insets(0, 10, 0, 5);
	add(l_icon, c);

	c = new GridBagConstraints();
	c.gridx = 1;
	c.gridy = 0;
	c.insets = new Insets(10, 0, 0, 5);
	c.weightx = 0.1;
	c.weighty = 0.25;
	c.anchor = GridBagConstraints.LINE_END;
	add(l_name, c);

	c = new GridBagConstraints();
	c.gridx = 2;
	c.gridy = 0;
	c.insets = new Insets(10, 0, 0, 10);
	c.weightx = 0.2;
	c.weighty = 0.25;
	c.anchor = GridBagConstraints.LINE_START;
	c.fill = GridBagConstraints.HORIZONTAL;
	add(tf_name, c);

	c = new GridBagConstraints();
	c.gridx = 3;
	c.gridy = 0;
	c.insets = new Insets(10, 0, 0, 5);
	c.weightx = 0.1;
	c.weighty = 0.25;
	c.anchor = GridBagConstraints.LINE_END;
	add(l_numberStories, c);

	c = new GridBagConstraints();
	c.gridx = 4;
	c.gridy = 0;
	c.insets = new Insets(10, 0, 0, 10);
	c.weightx = 0.1;
	c.weighty = 0.25;
	c.fill = GridBagConstraints.HORIZONTAL;
	c.anchor = GridBagConstraints.LINE_START;
	add(tf_numberStories, c);

	c = new GridBagConstraints();
	c.gridx = 1;
	c.gridy = 1;
	c.insets = new Insets(5, 0, 0, 5);
	c.weightx = 0.1;
	c.weighty = 0.25;
	c.anchor = GridBagConstraints.LINE_END;
	add(l_address, c);

	c = new GridBagConstraints();
	c.gridx = 2;
	c.gridy = 1;
	c.gridwidth = 3;
	c.weightx = 0.4;
	c.weighty = 0.25;
	c.insets = new Insets(5, 0, 0, 10);
	c.fill = GridBagConstraints.HORIZONTAL;
	c.anchor = GridBagConstraints.LINE_START;
	add(tf_address, c);

	JPanel p_price = new JPanel(new GridBagLayout());
	c = new GridBagConstraints();
	c.gridx = 0;
	c.gridy = 0;
	c.insets = new Insets(0, 5, 0, 5);
	c.weightx = 0.25;
	c.weighty = 0.33;
	c.anchor = GridBagConstraints.LINE_END;
	p_price.add(l_electricityPrice, c);

	c = new GridBagConstraints();
	c.gridx = 1;
	c.gridy = 0;
	c.insets = new Insets(0, 0, 0, 10);
	c.weightx = 0.25;
	c.weighty = 0.33;
	c.anchor = GridBagConstraints.LINE_START;
	p_price.add(tf_electricityPrice, c);

	c = new GridBagConstraints();
	c.gridx = 2;
	c.gridy = 0;
	c.insets = new Insets(0, 0, 0, 5);
	c.weightx = 0.25;
	c.weighty = 0.33;
	c.anchor = GridBagConstraints.LINE_END;
	p_price.add(l_waterPrice, c);

	c = new GridBagConstraints();
	c.gridx = 3;
	c.gridy = 0;
	c.insets = new Insets(0, 0, 0, 0);
	c.weightx = 0.25;
	c.weighty = 0.33;
	c.anchor = GridBagConstraints.LINE_START;
	p_price.add(tf_waterPrice, c);

	c = new GridBagConstraints();
	c.gridx = 0;
	c.gridy = 1;
	c.insets = new Insets(5, 5, 0, 5);
	c.weightx = 0.25;
	c.weighty = 0.33;
	c.anchor = GridBagConstraints.LINE_END;
	p_price.add(l_cleaningPrice, c);

	c = new GridBagConstraints();
	c.gridx = 1;
	c.gridy = 1;
	c.insets = new Insets(5, 0, 0, 10);
	c.weightx = 0.25;
	c.weighty = 0.33;
	c.anchor = GridBagConstraints.LINE_START;
	p_price.add(tf_cleaningPrice, c);

	c = new GridBagConstraints();
	c.gridx = 2;
	c.gridy = 1;
	c.insets = new Insets(5, 0, 0, 5);
	c.weightx = 0.25;
	c.weighty = 0.33;
	c.anchor = GridBagConstraints.LINE_END;
	p_price.add(l_internetPrice, c);

	c = new GridBagConstraints();
	c.gridx = 3;
	c.gridy = 1;
	c.insets = new Insets(5, 0, 0, 0);
	c.weightx = 0.25;
	c.weighty = 0.33;
	c.anchor = GridBagConstraints.LINE_START;
	p_price.add(tf_internetPrice, c);

	c = new GridBagConstraints();
	c.gridx = 0;
	c.gridy = 2;
	c.insets = new Insets(5, 0, 0, 5);
	c.weightx = 0.25;
	c.weighty = 0.33;
	c.anchor = GridBagConstraints.LINE_END;
	p_price.add(l_acPrice, c);

	c = new GridBagConstraints();
	c.gridx = 1;
	c.gridy = 2;
	c.insets = new Insets(5, 0, 0, 0);
	c.weightx = 0.25;
	c.weighty = 0.33;
	c.anchor = GridBagConstraints.LINE_START;
	p_price.add(tf_acPrice, c);

	TitledBorder tb_price = BorderFactory
		.createTitledBorder(RentManagerMain
			.getString("border.title.prices"));
	p_price.setBorder(tb_price);

	c = new GridBagConstraints();
	c.gridx = 1;
	c.gridy = 2;
	c.gridwidth = 4;
	c.gridheight = 2;
	c.weightx = 0.5;
	c.weighty = 0.5;
	c.fill = GridBagConstraints.HORIZONTAL;
	c.insets = new Insets(0, 0, 10, 10);
	add(p_price, c);

	TitledBorder tb_info = BorderFactory.createTitledBorder(RentManagerMain
		.getString("border.title.info"));
	ta_info.setBorder(tb_info);
	ta_info.setOpaque(false);
	ta_info.setLineWrap(true);
	ta_info.setWrapStyleWord(true);
	ta_info.setEditable(false);
	c = new GridBagConstraints();
	c.gridx = 5;
	c.gridy = 0;
	c.gridwidth = 1;
	c.gridheight = 4;
	c.weightx = 0.4;
	c.weighty = 1;
	c.fill = GridBagConstraints.BOTH;
	c.insets = new Insets(10, 0, 10, 10);
	add(ta_info, c);

	if (!rentManager.isGlobalEditable()) {
	    disableAllTextComp(this);
	}
    }

    private void disableAllTextComp(Container c) {
	for (Component comp : c.getComponents()) {
	    if (comp instanceof JTextComponent) {
		((JTextComponent) comp).setEditable(false);
		((JTextComponent) comp).setOpaque(false);
		((JTextComponent) comp).setBorder(null);
	    } else if (comp instanceof AbstractButton) {
		comp.setEnabled(false);
	    }
	    disableAllTextComp((Container) comp);
	}
    }

    class BuildingInfoListener implements FocusListener {
	String oldValue = "";

	
	public void focusGained(FocusEvent e) {
	    if (!rentManager.isGlobalEditable())
		return;
	    if (isUpdatingInfo)
		return;
	    JTextComponent comp = (JTextComponent) e.getSource();
	    comp.selectAll();
	    oldValue = comp.getText();
	}

	
	public void focusLost(FocusEvent e) {
	    if (!rentManager.isGlobalEditable())
		return;
	    if (isUpdatingInfo)
		return;
	    JTextComponent comp = (JTextComponent) e.getSource();
	    String newValue = comp.getText();
	    if (newValue.equals(oldValue))
		return;
	    int bldgIndex = rentManager.getRoot().getIndex(currBuilding);
	    BillTableModel btm = rentManager.getTablePanel()
		    .getTable(bldgIndex).getModel();
	    if (comp.equals(tf_name)) {
		currBuilding.setName(newValue);
		DefaultTreeModel tm = ((DefaultTreeModel) rentManager
			.getTreePanel().getTree().getModel());
		JTabbedPane tp = rentManager.getTablePanel().getTabbedPane();
		int oldIndex = rentManager.getRoot().getIndex(currBuilding);
		tm.removeNodeFromParent(currBuilding);
		int newIndex = ElementComparator.getInsertIndex(
			rentManager.getRoot(), currBuilding);
		if (newIndex == -1) {
		    currBuilding.setName(oldValue);
		    tm.insertNodeInto(currBuilding, rentManager.getRoot(),
			    oldIndex);
		    tf_name.setText(oldValue);
		    String message = RentManagerMain
			    .getString("message.name.already.exist");
		    rentManager
			    .getTreePanel()
			    .getTree()
			    .setSelectionPath(
				    new TreePath(tm.getPathToRoot(currBuilding)));
		    JOptionPane.showMessageDialog(rentManager, message);
		    return;
		}
		tm.insertNodeInto(currBuilding, rentManager.getRoot(), newIndex);
		Component select = tp.getSelectedComponent();
		tp.remove(oldIndex);
		tp.insertTab(newValue, null, select,
			RentManagerMain.getString("tip.bldg"), newIndex);
	    } else if (comp.equals(tf_numberStories)) {
		int numStories = 0;
		try {
		    numStories = Integer.parseInt(newValue);
		} catch (NumberFormatException ext) {
		    tf_numberStories.setText(oldValue);
		    return;
		}
		if (numStories <= 0) {
		    tf_numberStories.setText(oldValue);
		    return;
		}
		int numChildren = currBuilding.getChildCount();
		if (numChildren != 0) {
		    Building.Story topStory = (Building.Story) currBuilding
			    .getChildAt(currBuilding.getChildCount() - 1);
		    if (numStories < topStory.getStoryNumber()) {
			tf_numberStories.setText(oldValue);
			String message = RentManagerMain
				.getString("message.story.num.error");
			JOptionPane.showMessageDialog(rentManager, message);
			return;
		    }
		}
		currBuilding.setNumberStories(numStories);
	    } else if (comp.equals(tf_address)) {
		currBuilding.setAddress(newValue);
	    } else if (comp.equals(tf_electricityPrice)) {
		double electPrice = 0.0;
		try {
		    electPrice = Double.parseDouble(newValue);
		} catch (NumberFormatException ext) {
		    tf_electricityPrice.setText(oldValue);
		    return;
		}
		if (electPrice < 0) {
		    tf_electricityPrice.setText(oldValue);
		    return;
		}
		currBuilding.setElectricityPrice(electPrice);
		for (int i = 0; i < btm.getRowCount(); i++)
		    btm.updateTotal(i);
	    } else if (comp.equals(tf_waterPrice)) {
		double waterPrice = 0.0;
		try {
		    waterPrice = Double.parseDouble(newValue);
		} catch (NumberFormatException ext) {
		    tf_waterPrice.setText(oldValue);
		    return;
		}
		if (waterPrice < 0) {
		    tf_waterPrice.setText(oldValue);
		    return;
		}
		currBuilding.setWaterPrice(waterPrice);
		for (int i = 0; i < btm.getRowCount(); i++)
		    btm.updateTotal(i);
	    } else if (comp.equals(tf_cleaningPrice)) {
		int cleaningPrice = 0;
		try {
		    cleaningPrice = Integer.parseInt(newValue);
		} catch (NumberFormatException ext) {
		    tf_cleaningPrice.setText(oldValue);
		    return;
		}
		if (cleaningPrice < 0) {
		    tf_cleaningPrice.setText(oldValue);
		    return;
		}
		currBuilding.setCleaningPrice(cleaningPrice);
		for (int i = 0; i < btm.getRowCount(); i++) {
		    btm.getRow(i)[BillTableModel.CLEANING] = cleaningPrice;
		    btm.fireTableCellUpdated(i, BillTableModel.CLEANING);
		}
	    } else if (comp.equals(tf_internetPrice)) {
		int internetPrice = 0;
		try {
		    internetPrice = Integer.parseInt(newValue);
		} catch (NumberFormatException ext) {
		    tf_internetPrice.setText(oldValue);
		    return;
		}
		if (internetPrice < 0) {
		    tf_internetPrice.setText(oldValue);
		    return;
		}
		currBuilding.setInternetPrice(internetPrice);
		for (int i = 0; i < btm.getRowCount(); i++)
		    btm.updateTotal(i);
	    } else if (comp.equals(tf_acPrice)) {
		int acPrice = 0;
		try {
		    acPrice = Integer.parseInt(newValue);
		} catch (NumberFormatException ext) {
		    tf_acPrice.setText(oldValue);
		    return;
		}
		if (acPrice < 0) {
		    tf_acPrice.setText(oldValue);
		    return;
		}
		currBuilding.setAcPrice(acPrice);

		for (int i = 0; i < btm.getRowCount(); i++)
		    btm.updateTotal(i);
	    }
	    rentManager.setHasModified(true);
	}
    }

    
    public void updateInfo(Object element) {
	isUpdatingInfo = true;
	currBuilding = (Building) element;
	tf_name.setText(currBuilding.getName());
	tf_numberStories.setText(currBuilding.getNumberStories() + "");
	tf_address.setText(currBuilding.getAddress());
	tf_electricityPrice.setText(currBuilding.getElectricityPrice() + "");
	tf_waterPrice.setText(currBuilding.getWaterPrice() + "");
	tf_cleaningPrice.setText(currBuilding.getCleaningPrice() + "");
	tf_internetPrice.setText(currBuilding.getInternetPrice() + "");
	tf_acPrice.setText(currBuilding.getAcPrice() + "");
	int numStories = RentManager.getNumElements(1, currBuilding);
	int numRooms = RentManager.getNumElements(2, currBuilding);
	int numTenants = RentManager.getNumElements(3, currBuilding);
	int waterUsed = 0;
	int electricityUsed = 0;
	int numOccupied = 0;
	Enumeration<Building.Story> e_story = currBuilding.children();
	while (e_story.hasMoreElements()) {
	    Building.Story story = e_story.nextElement();
	    Enumeration<Room> e_room = story.children();
	    while (e_room.hasMoreElements()) {
		Room room = e_room.nextElement();
		waterUsed += room.getWaterUsed();
		electricityUsed += room.getElectricityUsed();
		if (room.isOccupied())
		    numOccupied++;
	    }
	}
	String text = MessageFormat.format(
		RentManagerMain.getString("message.bldg.info"),
		currBuilding.getName(), numStories, numRooms, numOccupied,
		numTenants, waterUsed, electricityUsed);
	ta_info.setText(text);
	isUpdatingInfo = false;
    }

    
    public void updateInfo() {
	updateInfo(currBuilding);
    }

}