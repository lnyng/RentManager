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
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.text.JTextComponent;

@SuppressWarnings("serial")
public class RoomInfoPanel extends ElementInfoPanel {
    private RentManager rentManager;
    private JTextField tf_roomNumber = new JTextField(5);
    private JTextField tf_numberTenants = new JTextField(5);
    private JTextField tf_signer = new JTextField(5);
    private JLabel l_contractChangeDate = new JLabel();
    private JTextField tf_contractChangeDate = new JTextField(6);
    private JTextField tf_waterRecord = new JTextField(5);
    private JTextField tf_electricityRecord = new JTextField(5);
    private JTextField tf_rent = new JTextField(5);
    private JTextField tf_deposit = new JTextField(5);
    private JCheckBox cb_useInternet = new JCheckBox();
    private JCheckBox cb_useAc = new JCheckBox();
    private JTextArea ta_note = new JTextArea();
    private Room currRoom;
    private boolean isUpdatingInfo;

    public RoomInfoPanel() {
	rentManager = RentManager.rm;
	RoomInfoListener rif = new RoomInfoListener();

	tf_rent.addFocusListener(rif);
	tf_deposit.addFocusListener(rif);
	ta_note.addFocusListener(rif);
	tf_waterRecord.addFocusListener(rif);
	tf_electricityRecord.addFocusListener(rif);
	cb_useInternet.addItemListener(rif);
	cb_useAc.addItemListener(rif);
	tf_contractChangeDate.addFocusListener(rif);

	tf_roomNumber.setEditable(false);
	tf_roomNumber.setOpaque(false);
	tf_roomNumber.setBorder(null);
	tf_numberTenants.setEditable(false);
	tf_numberTenants.setOpaque(false);
	tf_numberTenants.setBorder(null);
	tf_signer.setEditable(false);
	tf_signer.setOpaque(false);
	tf_signer.setBorder(null);
	tf_contractChangeDate.setEditable(false);
	tf_contractChangeDate.setOpaque(false);
	tf_contractChangeDate.setBorder(null);
	tf_waterRecord.setEditable(false);
	tf_waterRecord.setOpaque(false);
	tf_waterRecord.setBorder(null);
	tf_electricityRecord.setEditable(false);
	tf_electricityRecord.setOpaque(false);
	tf_electricityRecord.setBorder(null);

	tf_rent.setMinimumSize(tf_rent.getPreferredSize());
	tf_deposit.setMinimumSize(tf_deposit.getPreferredSize());
	tf_signer.setMinimumSize(tf_signer.getPreferredSize());
	tf_contractChangeDate.setMinimumSize(tf_contractChangeDate
		.getPreferredSize());
	tf_waterRecord.setMinimumSize(tf_waterRecord.getPreferredSize());
	tf_electricityRecord.setMinimumSize(tf_electricityRecord
		.getPreferredSize());

	JLabel l_icon = new JLabel(rentManager.roomIcon);
	JLabel l_roomNumber = new JLabel(
		rentManager.getString("label.room.num"));
	JLabel l_numberTenants = new JLabel(
		rentManager.getString("label.room.tenant.num"));
	JLabel l_signer = new JLabel(rentManager.getString("label.room.signer"));
	JLabel l_waterRecord = new JLabel(
		rentManager.getString("label.room.water.record"));
	JLabel l_electricityRecord = new JLabel(
		rentManager.getString("label.room.elect.record"));
	JLabel l_rent = new JLabel(rentManager.getString("label.room.rent"));
	JLabel l_deposit = new JLabel(
		rentManager.getString("label.room.deposit"));
	JLabel l_useInternet = new JLabel(
		rentManager.getString("label.room.use.internet"));
	JLabel l_useAc = new JLabel(rentManager.getString("label.room.use.ac"));

	setLayout(new GridBagLayout());

	GridBagConstraints c = new GridBagConstraints();
	c.gridx = 0;
	c.gridy = 0;
	c.insets = new Insets(0, 10, 0, 10);
	add(l_icon, c);

	JPanel p_left = new JPanel(new GridBagLayout());
	c = new GridBagConstraints();
	c.gridx = 0;
	c.gridy = 0;
	c.weightx = 0.25;
	c.weighty = 0.2;
	c.anchor = GridBagConstraints.LINE_END;
	c.fill = GridBagConstraints.VERTICAL;
	c.insets = new Insets(10, 0, 0, 5);
	p_left.add(l_roomNumber, c);

	c = new GridBagConstraints();
	c.gridx = 1;
	c.gridy = 0;
	c.weightx = 0.25;
	c.weighty = 0.2;
	c.anchor = GridBagConstraints.LINE_START;
	c.fill = GridBagConstraints.BOTH;
	c.insets = new Insets(10, 0, 0, 10);
	p_left.add(tf_roomNumber, c);

	c = new GridBagConstraints();
	c.gridx = 0;
	c.gridy = 1;
	c.weightx = 0.25;
	c.weighty = 0.2;
	c.anchor = GridBagConstraints.LINE_END;
	c.fill = GridBagConstraints.VERTICAL;
	c.insets = new Insets(10, 0, 0, 5);
	p_left.add(l_numberTenants, c);

	c = new GridBagConstraints();
	c.gridx = 1;
	c.gridy = 1;
	c.weightx = 0.25;
	c.weighty = 0.2;
	c.insets = new Insets(10, 0, 0, 10);
	c.anchor = GridBagConstraints.LINE_START;
	c.fill = GridBagConstraints.BOTH;
	p_left.add(tf_numberTenants, c);

	c = new GridBagConstraints();
	c.gridx = 0;
	c.gridy = 2;
	c.weightx = 0.25;
	c.weighty = 0.2;
	c.insets = new Insets(10, 0, 0, 5);
	c.anchor = GridBagConstraints.LINE_END;
	c.fill = GridBagConstraints.VERTICAL;
	p_left.add(l_signer, c);

	c = new GridBagConstraints();
	c.gridx = 1;
	c.gridy = 2;
	c.weightx = 0.25;
	c.weighty = 0.2;
	c.insets = new Insets(10, 0, 0, 10);
	c.anchor = GridBagConstraints.LINE_START;
	c.fill = GridBagConstraints.BOTH;
	p_left.add(tf_signer, c);

	c = new GridBagConstraints();
	c.gridx = 0;
	c.gridy = 3;
	c.weightx = 0.25;
	c.weighty = 0.2;
	c.insets = new Insets(10, 0, 0, 5);
	c.anchor = GridBagConstraints.LINE_END;
	c.fill = GridBagConstraints.VERTICAL;
	p_left.add(l_contractChangeDate, c);

	c = new GridBagConstraints();
	c.gridx = 1;
	c.gridy = 3;
	c.weightx = 0.25;
	c.weighty = 0.2;
	c.insets = new Insets(10, 0, 0, 10);
	c.anchor = GridBagConstraints.LINE_START;
	c.fill = GridBagConstraints.BOTH;
	p_left.add(tf_contractChangeDate, c);

	c = new GridBagConstraints();
	c.gridx = 2;
	c.gridy = 0;
	c.weightx = 0.25;
	c.weighty = 0.2;
	c.insets = new Insets(10, 0, 0, 5);
	c.anchor = GridBagConstraints.LINE_END;
	c.fill = GridBagConstraints.VERTICAL;
	p_left.add(l_waterRecord, c);

	c = new GridBagConstraints();
	c.gridx = 3;
	c.gridy = 0;
	c.weightx = 0.25;
	c.weighty = 0.2;
	c.insets = new Insets(10, 0, 0, 10);
	c.anchor = GridBagConstraints.LINE_START;
	c.fill = GridBagConstraints.BOTH;
	p_left.add(tf_waterRecord, c);

	c = new GridBagConstraints();
	c.gridx = 2;
	c.gridy = 1;
	c.weightx = 0.25;
	c.weighty = 0.2;
	c.insets = new Insets(10, 0, 0, 5);
	c.anchor = GridBagConstraints.LINE_END;
	c.fill = GridBagConstraints.VERTICAL;
	p_left.add(l_electricityRecord, c);

	c = new GridBagConstraints();
	c.gridx = 3;
	c.gridy = 1;
	c.weightx = 0.25;
	c.weighty = 0.2;
	c.insets = new Insets(10, 0, 0, 10);
	c.anchor = GridBagConstraints.LINE_START;
	c.fill = GridBagConstraints.BOTH;
	p_left.add(tf_electricityRecord, c);

	c = new GridBagConstraints();
	c.gridx = 2;
	c.gridy = 2;
	c.weightx = 0.25;
	c.weighty = 0.2;
	c.insets = new Insets(10, 0, 0, 5);
	c.anchor = GridBagConstraints.LINE_END;
	c.fill = GridBagConstraints.VERTICAL;
	p_left.add(l_rent, c);

	c = new GridBagConstraints();
	c.gridx = 3;
	c.gridy = 2;
	c.weightx = 0.25;
	c.weighty = 0.2;
	c.insets = new Insets(10, 0, 0, 10);
	c.anchor = GridBagConstraints.LINE_START;
	c.fill = GridBagConstraints.BOTH;
	p_left.add(tf_rent, c);

	c = new GridBagConstraints();
	c.gridx = 2;
	c.gridy = 3;
	c.weightx = 0.25;
	c.weighty = 0.2;
	c.insets = new Insets(10, 0, 0, 5);
	c.anchor = GridBagConstraints.LINE_END;
	c.fill = GridBagConstraints.VERTICAL;
	p_left.add(l_deposit, c);

	c = new GridBagConstraints();
	c.gridx = 3;
	c.gridy = 3;
	c.weightx = 0.25;
	c.weighty = 0.2;
	c.insets = new Insets(10, 0, 0, 10);
	c.anchor = GridBagConstraints.LINE_START;
	c.fill = GridBagConstraints.BOTH;
	p_left.add(tf_deposit, c);

	c = new GridBagConstraints();
	c.gridx = 0;
	c.gridy = 4;
	c.weightx = 0.25;
	c.weighty = 0.2;
	c.insets = new Insets(10, 0, 10, 5);
	c.anchor = GridBagConstraints.LINE_END;
	c.fill = GridBagConstraints.VERTICAL;
	p_left.add(l_useInternet, c);

	c = new GridBagConstraints();
	c.gridx = 1;
	c.gridy = 4;
	c.weightx = 0.25;
	c.weighty = 0.2;
	c.insets = new Insets(10, 0, 10, 10);
	c.anchor = GridBagConstraints.LINE_START;
	c.fill = GridBagConstraints.BOTH;
	p_left.add(cb_useInternet, c);

	c = new GridBagConstraints();
	c.gridx = 2;
	c.gridy = 4;
	c.weightx = 0.25;
	c.weighty = 0.2;
	c.insets = new Insets(10, 0, 10, 5);
	c.anchor = GridBagConstraints.LINE_END;
	c.fill = GridBagConstraints.VERTICAL;
	p_left.add(l_useAc, c);

	c = new GridBagConstraints();
	c.gridx = 3;
	c.gridy = 4;
	c.weightx = 0.25;
	c.weighty = 0.2;
	c.insets = new Insets(10, 0, 10, 10);
	c.anchor = GridBagConstraints.LINE_START;
	c.fill = GridBagConstraints.BOTH;
	p_left.add(cb_useAc, c);

	c = new GridBagConstraints();
	c.gridx = 1;
	c.gridy = 0;
	c.weightx = 0;
	c.weighty = 1;
	c.fill = GridBagConstraints.BOTH;
	add(p_left, c);

	TitledBorder tb_note = BorderFactory.createTitledBorder(rentManager
		.getString("border.title.note"));
	ta_note.setLineWrap(true);
	ta_note.setWrapStyleWord(true);
	JScrollPane sp_note = new JScrollPane(ta_note);
	sp_note.setBorder(tb_note);
	c = new GridBagConstraints();
	c.gridx = 2;
	c.gridy = 0;
	c.weightx = 1;
	c.weighty = 1;
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

    class RoomInfoListener implements FocusListener, ItemListener {
	String oldValue = "";

	@Override
	public void focusGained(FocusEvent e) {
	    if (!rentManager.isGlobalEditable())
		return;
	    if (isUpdatingInfo)
		return;
	    JTextComponent comp = (JTextComponent) e.getSource();
	    if (comp.equals(tf_contractChangeDate))
		if (currRoom.isOccupied()
			&& currRoom.getContractSigner().isInitializing()) {
		    oldValue = comp.getText();
		    return;
		} else
		    return;
	    if (comp.equals(tf_electricityRecord)
		    || comp.equals(tf_waterRecord))
		if (!currRoom.isInitializing())
		    return;
	    comp.selectAll();
	    oldValue = comp.getText();
	}

	@Override
	public void focusLost(FocusEvent e) {
	    if (!rentManager.isGlobalEditable())
		return;
	    if (isUpdatingInfo)
		return;
	    JTextComponent comp = (JTextComponent) e.getSource();
	    if (comp.equals(tf_contractChangeDate))
		if (!currRoom.isOccupied()
			|| !currRoom.getContractSigner().isInitializing())
		    return;
	    if (comp.equals(tf_electricityRecord)
		    || comp.equals(tf_waterRecord))
		if (!currRoom.isInitializing())
		    return;
	    String newValue = comp.getText();
	    if (newValue.equals(oldValue))
		return;
	    if (comp.equals(tf_rent)) {
		int rent = 0;
		try {
		    rent = Integer.parseInt(newValue);
		} catch (NumberFormatException ext) {
		    tf_rent.setText(oldValue);
		    return;
		}
		if (rent < 0) {
		    tf_rent.setText(oldValue);
		    return;
		}
		RentManager.logger.info("The rent of room ["
			+ currRoom.toString() + "] at building ["
			+ currRoom.getBuilding().toString()
			+ "] has been updated from " + oldValue + " to "
			+ newValue + ".");
		currRoom.setRent(rent);
		updateTable();
	    } else if (comp.equals(tf_deposit)) {
		int deposit = 0;
		try {
		    deposit = Integer.parseInt(newValue);
		} catch (NumberFormatException ext) {
		    tf_deposit.setText(oldValue);
		    return;
		}
		if (deposit < 0) {
		    tf_deposit.setText(oldValue);
		    return;
		}
		RentManager.logger.info("The deposit of room ["
			+ currRoom.toString() + "] at building ["
			+ currRoom.getBuilding().toString()
			+ "] has been updated from " + oldValue + " to "
			+ newValue + ".");
		currRoom.setDeposit(deposit);
	    } else if (comp.equals(ta_note)) {
		RentManager.logger.info("The note of room ["
			+ currRoom.toString() + "] at building ["
			+ currRoom.getBuilding().toString()
			+ "] has been updated .");
		currRoom.setNote(newValue);
	    } else if (comp.equals(tf_waterRecord)) {
		int waterRecord = 0;
		try {
		    waterRecord = Integer.parseInt(newValue);
		} catch (NumberFormatException ext) {
		    tf_waterRecord.setText(oldValue);
		    return;
		}
		if (waterRecord < currRoom.getWaterRecord()
			+ currRoom.getWaterUsed()) {
		    tf_waterRecord.setText(oldValue);
		    return;
		}
		RentManager.logger.info("The water record of room ["
			+ currRoom.toString() + "] at building ["
			+ currRoom.getBuilding().toString()
			+ "] has been updated from " + oldValue + " to "
			+ newValue + ".");
		currRoom.setWaterRecord(waterRecord);
		updateTable();
	    } else if (comp.equals(tf_electricityRecord)) {
		int electricityRecord = 0;
		try {
		    electricityRecord = Integer.parseInt(newValue);
		} catch (NumberFormatException ext) {
		    tf_electricityRecord.setText(oldValue);
		    return;
		}
		if (electricityRecord < currRoom.getElectricityRecord()
			+ currRoom.getElectricityUsed()) {
		    tf_electricityRecord.setText(oldValue);
		    return;
		}
		RentManager.logger.info("The electricity record of room ["
			+ currRoom.toString() + "] at building ["
			+ currRoom.getBuilding().toString()
			+ "] has been updated from " + oldValue + " to "
			+ newValue + ".");
		currRoom.setElectricityRecord(electricityRecord);
		updateTable();
	    } else if (comp.equals(tf_contractChangeDate)) {
		Date contractStart = null;
		try {
		    contractStart = new SimpleDateFormat("MM/dd/yyyy")
			    .parse(newValue);
		} catch (ParseException ext) {
		    tf_contractChangeDate.setText(oldValue);
		    return;
		}
		RentManager.logger.info("The contract change date of room ["
			+ currRoom.toString() + "] at building ["
			+ currRoom.getBuilding().toString()
			+ "] has been updated from " + oldValue + " to "
			+ newValue + ".");
		currRoom.setContractChangeDate(contractStart);
	    }
	    rentManager.setHasModified(true);
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
	    if (isUpdatingInfo)
		return;
	    Object comp = e.getSource();
	    boolean selected = ((JCheckBox) comp).isSelected();
	    if (comp.equals(cb_useAc)) {
		RentManager.logger.info("The AC using state of room ["
			+ currRoom.toString() + "] at building ["
			+ currRoom.getBuilding().toString()
			+ "] has been updated to " + selected + ".");
		currRoom.setUseAC(selected);
		updateTable();
	    } else if (comp.equals(cb_useInternet)) {
		RentManager.logger.info("The internet using state of room ["
			+ currRoom.toString() + "] at building ["
			+ currRoom.getBuilding().toString()
			+ "] has been updated to " + selected + ".");
		currRoom.setUseInternet(selected);
		updateTable();
	    }
	    rentManager.setHasModified(true);
	}

	private void updateTable() {
	    int bldgIndex = rentManager.getRoot().getIndex(
		    currRoom.getBuilding());
	    BillTable table = rentManager.getTablePanel().getTable(bldgIndex);
	    table.getModel().updateRoom(currRoom);
	}
    }

    @Override
    public void updateInfo(Object element) {
	isUpdatingInfo = true;
	currRoom = (Room) element;
	tf_roomNumber.setText(currRoom.getRoomNumber() + "");
	int numTenants = currRoom.getChildCount();
	tf_numberTenants.setText(numTenants + "");
	String text = ((numTenants == 0) ? rentManager
		.getString("default.none") : currRoom.getContractSigner()) + "";
	tf_signer.setText(text);
	Date contractChangeDate = currRoom.getContractChangeDate();
	text = (currRoom.isOccupied() ? rentManager
		.getString("label.contract.start") : rentManager
		.getString("label.contract.end"));
	l_contractChangeDate.setText(text);
	text = contractChangeDate == null ? rentManager
		.getString("default.n.a")
		: (new SimpleDateFormat("MM/dd/yyyy"))
			.format(contractChangeDate);
	tf_contractChangeDate.setText(text);
	if (rentManager.isGlobalEditable()
		&& currRoom.getContractSigner() != null
		&& currRoom.getContractSigner().isInitializing()) {
	    tf_contractChangeDate.setEditable(true);
	    tf_contractChangeDate.setOpaque(true);
	    tf_contractChangeDate.setBorder(UIManager
		    .getBorder("TextField.border"));
	} else {
	    tf_contractChangeDate.setEditable(false);
	    tf_contractChangeDate.setOpaque(false);
	    tf_contractChangeDate.setBorder(null);
	}
	if (rentManager.isGlobalEditable() && currRoom.isInitializing()) {
	    tf_waterRecord.setEditable(true);
	    tf_waterRecord.setOpaque(true);
	    tf_waterRecord.setBorder(UIManager.getBorder("TextField.border"));
	    tf_electricityRecord.setEditable(true);
	    tf_electricityRecord.setOpaque(true);
	    tf_electricityRecord.setBorder(UIManager
		    .getBorder("TextField.border"));
	} else {
	    tf_waterRecord.setEditable(false);
	    tf_waterRecord.setOpaque(false);
	    tf_waterRecord.setBorder(null);
	    tf_electricityRecord.setEditable(false);
	    tf_electricityRecord.setOpaque(false);
	    tf_electricityRecord.setBorder(null);
	}
	tf_waterRecord.setText(currRoom.getWaterRecord() + "");
	tf_electricityRecord.setText(currRoom.getElectricityRecord() + "");
	tf_rent.setText(currRoom.getRent() + "");
	tf_deposit.setText(currRoom.getDeposit() + "");
	cb_useInternet.setSelected(currRoom.isUseInternet());
	cb_useAc.setSelected(currRoom.isUseAC());
	ta_note.setText(currRoom.getNote());
	revalidate();
	isUpdatingInfo = false;
    }

    @Override
    public void updateInfo() {
	updateInfo(currRoom);
    }
}
