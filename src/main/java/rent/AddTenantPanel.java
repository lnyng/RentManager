package rent;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.text.JTextComponent;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;

@SuppressWarnings("serial")
public class AddTenantPanel extends JPanel implements ElementsAdder {

    private RentManager rentManager;
    private JTextField tf_name = new JTextField(16);
    private JTextField tf_id = new JTextField("1234567890", 16);
    private JTextField tf_phone = new JTextField("1234567890", 16);
    private JComboBox<Building> cb_building = new JComboBox<Building>();
    private JComboBox<Building.Story> cb_story = new JComboBox<Building.Story>();
    private JComboBox<Room> cb_room = new JComboBox<Room>();
    private JCheckBox cb_isSigner = new JCheckBox();
    private JTextField tf_contract = new JTextField(new SimpleDateFormat(
	    "MM/dd/yyyy").format(new Date()), 16);
    private JTextArea ta_note = new JTextArea("", 3, 0);
    private Tenant newTenant;

    public AddTenantPanel() {
	rentManager = RentManager.rm;
	tf_name.setText(RentManagerMain.getString("default.tenant.name"));

	FocusListener fl = new FocusListener() {
	    public void focusGained(FocusEvent e) {
		((JTextComponent) e.getSource()).selectAll();
	    }

	    public void focusLost(FocusEvent e) {
	    }

	};
	tf_name.addFocusListener(fl);
	tf_id.addFocusListener(fl);
	tf_phone.addFocusListener(fl);
	ta_note.addFocusListener(fl);

	ItemListener il = new ItemListener() {
	    public void itemStateChanged(ItemEvent e) {
		if (e.getSource().equals(cb_building)) {
		    if (cb_building.getSelectedItem() != null) {
			Enumeration<Building.Story> e_story = ((Building) cb_building
				.getSelectedItem()).children();
			cb_story.removeAllItems();
			while (e_story.hasMoreElements())
			    cb_story.addItem(e_story.nextElement());
		    } else {
			cb_story.removeAllItems();
		    }
		} else if (e.getSource().equals(cb_story)) {
		    if (cb_story.getSelectedItem() != null) {
			Enumeration<Room> e_room = ((Building.Story) cb_story
				.getSelectedItem()).children();
			cb_room.removeAllItems();
			while (e_room.hasMoreElements())
			    cb_room.addItem(e_room.nextElement());
		    } else {
			cb_room.removeAllItems();
		    }
		} else if (e.getSource().equals(cb_room)) {
		    Room room = (Room) cb_room.getSelectedItem();
		    if (room != null && room.getChildCount() != 0) {
			String date = new SimpleDateFormat("MM/dd/yyyy")
				.format(room.getContractChangeDate());
			tf_contract.setText(date);
			tf_contract.setEditable(false);
			tf_contract.setOpaque(false);
			tf_contract.setBorder(null);
		    } else {
			tf_contract.setText(new SimpleDateFormat("MM/dd/yyyy")
				.format(new Date()));
			tf_contract.setEditable(true);
			tf_contract.setOpaque(true);
			tf_contract.setBorder(UIManager
				.getBorder("TextField.border"));
			revalidate();
		    }
		}
	    }
	};
	cb_building.addItemListener(il);
	cb_story.addItemListener(il);
	cb_room.addItemListener(il);

	JLabel l_icon = new JLabel(rentManager.tenantIcon);
	JLabel l_name = new JLabel(
		RentManagerMain.getString("label.tenant.name"));
	JLabel l_id = new JLabel(RentManagerMain.getString("label.tenant.id"));
	JLabel l_phone = new JLabel(
		RentManagerMain.getString("label.tenant.phone"));

	this.setLayout((new GridBagLayout()));
	GridBagConstraints c = new GridBagConstraints();
	c.gridx = 0;
	c.gridy = 0;
	c.gridheight = 3;
	c.insets = new Insets(0, 15, 0, 0);
	this.add(l_icon, c);

	c = new GridBagConstraints();
	c.gridx = 1;
	c.gridy = 0;
	c.insets = new Insets(15, 0, 0, 5);
	c.anchor = GridBagConstraints.LINE_END;
	this.add(l_name, c);

	c = new GridBagConstraints();
	c.gridx = 2;
	c.gridy = 0;
	c.weightx = 1.0;
	c.gridwidth = 4;
	c.insets = new Insets(15, 0, 0, 10);
	c.fill = GridBagConstraints.BOTH;
	c.anchor = GridBagConstraints.LINE_START;
	this.add(tf_name, c);

	c = new GridBagConstraints();
	c.gridx = 1;
	c.gridy = 1;
	c.insets = new Insets(10, 0, 0, 5);
	c.anchor = GridBagConstraints.LINE_END;
	this.add(l_id, c);

	c = new GridBagConstraints();
	c.gridx = 2;
	c.gridy = 1;
	c.weightx = 1.0;
	c.gridwidth = 4;
	c.insets = new Insets(10, 0, 0, 10);
	c.fill = GridBagConstraints.BOTH;
	c.anchor = GridBagConstraints.LINE_START;
	this.add(tf_id, c);

	c = new GridBagConstraints();
	c.gridx = 1;
	c.gridy = 2;
	c.insets = new Insets(10, 0, 0, 5);
	c.anchor = GridBagConstraints.LINE_END;
	this.add(l_phone, c);

	c = new GridBagConstraints();
	c.gridx = 2;
	c.gridy = 2;
	c.weightx = 1.0;
	c.gridwidth = 4;
	c.insets = new Insets(10, 0, 0, 10);
	c.fill = GridBagConstraints.BOTH;
	c.anchor = GridBagConstraints.LINE_START;
	this.add(tf_phone, c);

	JLabel l_building = new JLabel(
		RentManagerMain.getString("label.tenant.bldg"));
	JLabel l_story = new JLabel(
		RentManagerMain.getString("label.tenant.story"));
	JLabel l_room = new JLabel(
		RentManagerMain.getString("label.tenant.room"));

	c = new GridBagConstraints();
	c.gridx = 0;
	c.gridy = 3;
	c.insets = new Insets(10, 10, 0, 5);
	c.anchor = GridBagConstraints.LINE_END;
	this.add(l_building, c);

	c = new GridBagConstraints();
	c.gridx = 1;
	c.gridy = 3;
	c.insets = new Insets(10, 0, 0, 10);
	c.anchor = GridBagConstraints.LINE_START;
	this.add(cb_building, c);

	c = new GridBagConstraints();
	c.gridx = 2;
	c.gridy = 3;
	c.insets = new Insets(10, 0, 0, 5);
	c.anchor = GridBagConstraints.LINE_END;
	this.add(l_story, c);

	c = new GridBagConstraints();
	c.gridx = 3;
	c.gridy = 3;
	c.insets = new Insets(10, 0, 0, 10);
	c.anchor = GridBagConstraints.LINE_START;
	this.add(cb_story, c);

	c = new GridBagConstraints();
	c.gridx = 4;
	c.gridy = 3;
	c.insets = new Insets(10, 0, 0, 5);
	c.anchor = GridBagConstraints.LINE_END;
	this.add(l_room, c);

	c = new GridBagConstraints();
	c.gridx = 5;
	c.gridy = 3;
	c.insets = new Insets(10, 0, 0, 10);
	c.anchor = GridBagConstraints.LINE_START;
	this.add(cb_room, c);

	JLabel l_isSign = new JLabel(
		RentManagerMain.getString("label.tenant.is.signer"));
	c = new GridBagConstraints();
	c.gridx = 0;
	c.gridy = 4;
	c.gridwidth = 2;
	c.insets = new Insets(10, 0, 0, 5);
	c.anchor = GridBagConstraints.LINE_END;
	this.add(l_isSign, c);

	c = new GridBagConstraints();
	c.gridx = 2;
	c.gridy = 4;
	c.gridwidth = 4;
	c.insets = new Insets(10, 0, 0, 10);
	c.fill = GridBagConstraints.BOTH;
	c.anchor = GridBagConstraints.LINE_START;
	this.add(cb_isSigner, c);

	JLabel l_contract = new JLabel(
		RentManagerMain.getString("label.tenant.contract.start"));
	c = new GridBagConstraints();
	c.gridx = 0;
	c.gridy = 5;
	c.gridwidth = 2;
	c.insets = new Insets(10, 10, 0, 5);
	c.anchor = GridBagConstraints.LINE_END;
	this.add(l_contract, c);

	c = new GridBagConstraints();
	c.gridx = 2;
	c.gridy = 5;
	c.gridwidth = 4;
	c.insets = new Insets(10, 0, 0, 10);
	c.fill = GridBagConstraints.BOTH;
	c.anchor = GridBagConstraints.LINE_START;
	this.add(tf_contract, c);

	c = new GridBagConstraints();
	c.gridx = 0;
	c.gridy = 6;
	c.weightx = 1;
	c.weighty = 1;
	c.gridwidth = 6;
	c.fill = GridBagConstraints.BOTH;
	c.insets = new Insets(10, 15, 15, 15);
	TitledBorder tb_note = BorderFactory.createTitledBorder(RentManagerMain
		.getString("border.title.note"));
	JScrollPane sp_note = new JScrollPane(ta_note);
	sp_note.setMinimumSize(new Dimension(0, 79));
	sp_note.setBorder(tb_note);
	this.add(sp_note, c);
	updateData(null);
    }

    public boolean addElement() {
	if (cb_room.getSelectedItem() == null) {
	    cb_isSigner.setSelected(false);
	    return false;
	}
	String name = tf_name.getText();
	String id = tf_id.getText();
	String phone = tf_phone.getText();
	Room room = (Room) cb_room.getSelectedItem();
	boolean isSigner = cb_isSigner.isSelected();
	Date contractStartDate = new Date();
	try {
	    contractStartDate = new SimpleDateFormat("MM/dd/yyyy")
		    .parse(tf_contract.getText());
	} catch (ParseException e) {
	    e.printStackTrace();
	    JOptionPane.showMessageDialog(rentManager,
		    RentManagerMain.getString("message.date.format.error"));
	    return false;
	}
	if (room.getContractChangeDate() != null) {
	    if (contractStartDate.compareTo(room.getContractChangeDate()) < 0) {
		JOptionPane.showMessageDialog(this, RentManagerMain
			.getString("message.contract.date.error"));
		return false;
	    }
	}
	room.setContractChangeDate(contractStartDate);
	String note = ta_note.getText();
	newTenant = new Tenant(name, id, phone, note, isSigner,
		contractStartDate);
	if (room.getContractSigner() == null) {
	    newTenant.setIsContractSigner(true);
	    room.setContractSigner(newTenant);
	} else if (isSigner) {
	    room.getContractSigner().setIsContractSigner(false);
	    room.setContractSigner(newTenant);
	}
	DefaultTreeModel tm = ((DefaultTreeModel) rentManager.getTreePanel()
		.getTree().getModel());
	tm.insertNodeInto(newTenant, room, room.getChildCount());
	rentManager.getTablePanel().addTenant(newTenant);
	cb_isSigner.setSelected(false);
	return true;
    }

    public MutableTreeNode getNewElement() {
	return newTenant;
    }

    public void setTenantName(String name) {
	tf_name.setText(name);
    }

    @SuppressWarnings("unchecked")
    public void updateData(Object newElement) {
	if (newElement == null) {
	    cb_building.removeAllItems();
	    cb_story.removeAllItems();
	    cb_room.removeAllItems();
	    Enumeration<Building> e_bldg = rentManager.getRoot().children();
	    while (e_bldg.hasMoreElements())
		cb_building.addItem(e_bldg.nextElement());
	    cb_building.setSelectedItem(null);
	} else if (newElement instanceof Building) {
	    updateData(null);
	    cb_building.setSelectedItem(newElement);
	} else if (newElement instanceof Room) {
	    updateData(null);
	    Room room = (Room) newElement;
	    cb_building.setSelectedItem(room.getBuilding());
	    cb_story.setSelectedItem(room.getParent());
	    cb_room.setSelectedItem(room);
	} else if (newElement instanceof Tenant) {
	    updateData(((Tenant) newElement).getParent());
	}
    }
}
