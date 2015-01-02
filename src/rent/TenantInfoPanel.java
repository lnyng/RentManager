package rent;

import java.awt.Component;
import java.awt.Container;
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

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.text.JTextComponent;
import javax.swing.tree.DefaultTreeModel;

@SuppressWarnings("serial")
public class TenantInfoPanel extends ElementInfoPanel {
    private RentManager rentManager;
    private Tenant currTenant;
    private JTextField tf_name = new JTextField(5);
    private JTextField tf_room = new JTextField(5);
    private JTextField tf_id = new JTextField(10);
    private JTextField tf_phone = new JTextField(10);
    private JCheckBox cb_signer = new JCheckBox();
    private JTextField tf_contractStart = new JTextField(10);
    private JTextArea ta_note = new JTextArea();
    private boolean isUpdatingInfo;

    public TenantInfoPanel() {
	rentManager = RentManager.rm;
	setLayout(new GridBagLayout());
	TenantInfoListener til = new TenantInfoListener();

	tf_name.addFocusListener(til);
	tf_id.addFocusListener(til);
	tf_phone.addFocusListener(til);
	tf_contractStart.addFocusListener(til);
	ta_note.addFocusListener(til);

	JLabel l_icon = new JLabel(rentManager.tenantIcon);
	JLabel l_name = new JLabel(rentManager.getString("label.tenant.name"));
	JLabel l_room = new JLabel(rentManager.getString("label.tenant.room"));
	JLabel l_id = new JLabel(rentManager.getString("label.tenant.id"));
	JLabel l_phone = new JLabel(rentManager.getString("label.tenant.phone"));
	JLabel l_signer = new JLabel(
		rentManager.getString("label.tenant.is.signer"));
	JLabel l_contractStart = new JLabel(
		rentManager.getString("label.tenant.contract.start"));

	cb_signer.addItemListener(til);

	tf_room.setEditable(false);
	tf_room.setOpaque(false);
	tf_room.setBorder(null);

	tf_name.setMinimumSize(tf_name.getPreferredSize());
	tf_room.setMinimumSize(tf_room.getPreferredSize());
	tf_id.setMinimumSize(tf_id.getPreferredSize());
	tf_phone.setMinimumSize(tf_phone.getPreferredSize());
	tf_contractStart.setMinimumSize(tf_contractStart.getPreferredSize());

	GridBagConstraints c = new GridBagConstraints();
	c.gridx = 0;
	c.gridy = 0;
	c.weightx = 0;
	c.weighty = 0;
	c.gridheight = 5;
	c.insets = new Insets(0, 10, 0, 10);
	add(l_icon, c);

	c = new GridBagConstraints();
	c.gridx = 1;
	c.gridy = 0;
	c.weightx = 0;
	c.weighty = 0.2;
	c.anchor = GridBagConstraints.LINE_END;
	c.insets = new Insets(10, 0, 0, 5);
	add(l_name, c);

	c = new GridBagConstraints();
	c.gridx = 2;
	c.gridy = 0;
	c.weightx = 0;
	c.weighty = 0.2;
	c.anchor = GridBagConstraints.LINE_START;
	c.fill = GridBagConstraints.BOTH;
	c.insets = new Insets(10, 0, 0, 10);
	add(tf_name, c);

	c = new GridBagConstraints();
	c.gridx = 3;
	c.gridy = 0;
	c.weightx = 0;
	c.weighty = 0.2;
	c.anchor = GridBagConstraints.LINE_END;
	c.insets = new Insets(10, 0, 0, 5);
	add(l_room, c);

	c = new GridBagConstraints();
	c.gridx = 4;
	c.gridy = 0;
	c.weightx = 0;
	c.weighty = 0.2;
	c.anchor = GridBagConstraints.LINE_START;
	c.fill = GridBagConstraints.BOTH;
	c.insets = new Insets(10, 0, 0, 5);
	add(tf_room, c);

	c = new GridBagConstraints();
	c.gridx = 1;
	c.gridy = 1;
	c.weightx = 0;
	c.weighty = 0.2;
	c.anchor = GridBagConstraints.LINE_END;
	c.insets = new Insets(5, 0, 0, 5);
	add(l_id, c);

	c = new GridBagConstraints();
	c.gridx = 2;
	c.gridy = 1;
	c.gridwidth = 3;
	c.weightx = 0;
	c.weighty = 0.2;
	c.anchor = GridBagConstraints.LINE_START;
	c.fill = GridBagConstraints.BOTH;
	c.insets = new Insets(5, 0, 0, 5);
	add(tf_id, c);

	c = new GridBagConstraints();
	c.gridx = 1;
	c.gridy = 2;
	c.weightx = 0;
	c.weighty = 0.2;
	c.anchor = GridBagConstraints.LINE_END;
	c.insets = new Insets(5, 0, 0, 5);
	add(l_phone, c);

	c = new GridBagConstraints();
	c.gridx = 2;
	c.gridy = 2;
	c.gridwidth = 3;
	c.weightx = 0;
	c.weighty = 0.2;
	c.anchor = GridBagConstraints.LINE_START;
	c.fill = GridBagConstraints.BOTH;
	c.insets = new Insets(5, 0, 0, 5);
	add(tf_phone, c);

	c = new GridBagConstraints();
	c.gridx = 1;
	c.gridy = 3;
	c.weightx = 0;
	c.weighty = 0.2;
	c.anchor = GridBagConstraints.LINE_END;
	c.insets = new Insets(5, 0, 0, 5);
	add(l_contractStart, c);

	c = new GridBagConstraints();
	c.gridx = 2;
	c.gridy = 3;
	c.gridwidth = 3;
	c.weightx = 0;
	c.weighty = 0.2;
	c.anchor = GridBagConstraints.LINE_START;
	c.fill = GridBagConstraints.BOTH;
	c.insets = new Insets(5, 0, 0, 5);
	add(tf_contractStart, c);

	c = new GridBagConstraints();
	c.gridx = 1;
	c.gridy = 4;
	c.gridwidth = 2;
	c.weightx = 0;
	c.weighty = 0.2;
	c.anchor = GridBagConstraints.LINE_END;
	c.insets = new Insets(5, 0, 10, 5);
	add(l_signer, c);

	c = new GridBagConstraints();
	c.gridx = 3;
	c.gridy = 4;
	c.gridwidth = 2;
	c.weightx = 0;
	c.weighty = 0.2;
	c.anchor = GridBagConstraints.LINE_START;
	c.fill = GridBagConstraints.BOTH;
	c.insets = new Insets(5, 0, 10, 5);
	add(cb_signer, c);

	TitledBorder tb_note = BorderFactory.createTitledBorder(rentManager
		.getString("border.title.note"));
	JScrollPane sp_note = new JScrollPane(ta_note);
	ta_note.setLineWrap(true);
	ta_note.setWrapStyleWord(true);
	sp_note.setBorder(tb_note);
	c = new GridBagConstraints();
	c.gridx = 5;
	c.gridy = 0;
	c.gridheight = 5;
	c.weightx = 1;
	c.weighty = 0.8;
	c.insets = new Insets(10, 0, 10, 10);
	c.fill = GridBagConstraints.BOTH;
	add(sp_note, c);

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

    class TenantInfoListener implements FocusListener, ItemListener {

	String oldValue;

	@Override
	public void itemStateChanged(ItemEvent e) {
	    if (isUpdatingInfo)
		return;
	    if (e.getSource().equals(cb_signer)) {
		RentManager.logger.info("Tenant [" + currTenant.toString()
			+ "] is set as the contract signer of room "
			+ currTenant.getParent().toString() + ".");
		cb_signer.setEnabled(false);
		Room room = (Room) currTenant.getParent();
		room.getContractSigner().setIsContractSigner(false);
		room.setContractSigner(currTenant);
		currTenant.setIsContractSigner(true);
	    }
	    rentManager.setHasModified(true);
	}

	@Override
	public void focusGained(FocusEvent e) {
	    if (!rentManager.isGlobalEditable())
		return;
	    if (isUpdatingInfo)
		return;
	    JTextComponent comp = (JTextComponent) e.getComponent();
	    if (comp.equals(tf_contractStart))
		if (!currTenant.isContractSigner()
			|| !currTenant.isInitializing())
		    return;
	    if (!comp.equals(tf_contractStart)) {
		comp.selectAll();
	    }
	    oldValue = comp.getText();
	}

	@Override
	public void focusLost(FocusEvent e) {
	    if (!rentManager.isGlobalEditable())
		return;
	    if (isUpdatingInfo)
		return;
	    JTextComponent comp = (JTextComponent) e.getSource();
	    if (comp.equals(tf_contractStart))
		if (!currTenant.isContractSigner()
			|| !currTenant.isInitializing())
		    return;
	    String newValue = comp.getText();
	    if (newValue.equals(oldValue))
		return;
	    if (comp.equals(tf_name)) {
		RentManager.logger.info("The name of tenant ["
			+ currTenant.toString() + "] at room ["
			+ currTenant.getParent().toString()
			+ "] has been updated from " + oldValue + " to "
			+ newValue + ".");
		currTenant.setName(newValue);
		DefaultTreeModel model = (DefaultTreeModel) rentManager
			.getTreePanel().getTree().getModel();
		model.nodeChanged(currTenant);
		if (currTenant.isContractSigner()) {
		    int bldgIndex = rentManager.getRoot().getIndex(
			    currTenant.getBuilding());
		    rentManager.getTablePanel().getTable(bldgIndex).getModel()
			    .updateTenant(currTenant);
		}
	    } else if (comp.equals(tf_id)) {
		RentManager.logger.info("The ID of tenant ["
			+ currTenant.toString() + "] at room ["
			+ currTenant.getParent().toString()
			+ "] has been updated from " + oldValue + " to "
			+ newValue + ".");
		currTenant.setId(newValue);
	    } else if (comp.equals(tf_phone)) {
		RentManager.logger.info("The phone number of tenant ["
			+ currTenant.toString() + "] at room ["
			+ currTenant.getParent().toString()
			+ "] has been updated from " + oldValue + " to "
			+ newValue + ".");
		currTenant.setPhoneNumber(newValue);
	    } else if (comp.equals(tf_contractStart)) {
		Date newDate = null;
		try {
		    newDate = new SimpleDateFormat("MM/dd/yyyy")
			    .parse(newValue);
		} catch (ParseException ext) {
		    tf_contractStart.setText(oldValue);
		    return;
		}
		RentManager.logger.info("The contract start date of tenant ["
			+ currTenant.toString() + "] at room ["
			+ currTenant.getParent().toString()
			+ "] has been updated from " + oldValue + " to "
			+ newValue + ".");
		((Room) currTenant.getParent()).setContractChangeDate(newDate);
	    } else if (comp.equals(ta_note)) {
		RentManager.logger.info("The note of tenant [" + currTenant.toString()
			+ "] at room [" + currTenant.getParent().toString()
			+ "] has been updated.");
		currTenant.setNote(newValue);
	    }
	    rentManager.setHasModified(true);
	}
    }

    @Override
    public void updateInfo(Object element) {
	isUpdatingInfo = true;
	currTenant = (Tenant) element;
	tf_name.setText(currTenant.getName());
	tf_room.setText(currTenant.getParent().toString());
	tf_id.setText(currTenant.getId());
	tf_phone.setText(currTenant.getPhoneNumber());
	cb_signer.setEnabled(true);
	cb_signer.setSelected(currTenant.isContractSigner());
	if (currTenant.isContractSigner())
	    cb_signer.setEnabled(false);
	tf_contractStart.setText((new SimpleDateFormat("MM/dd/yyyy"))
		.format(currTenant.getContractStartDate()));
	if (!rentManager.isGlobalEditable() || !currTenant.isContractSigner()
		|| !currTenant.isInitializing()) {
	    tf_contractStart.setEditable(false);
	    tf_contractStart.setOpaque(false);
	    tf_contractStart.setBorder(null);
	} else {
	    tf_contractStart.setEditable(true);
	    tf_contractStart.setOpaque(true);
	    tf_contractStart.setBorder(UIManager.getBorder("TextField.border"));
	}
	ta_note.setText(currTenant.getNote());
	revalidate();
	isUpdatingInfo = false;
    }

    @Override
    public void updateInfo() {
	updateInfo(currTenant);
    }

}
