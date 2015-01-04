package rent;

import java.awt.CardLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Enumeration;

import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.text.JTextComponent;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;

@SuppressWarnings("serial")
public class AddRoomPanel extends JPanel implements ElementsAdder {

    private RentManager rentManager;
    private JRadioButton rb_addMultiRooms = new JRadioButton();
    private JRadioButton rb_addOneRoom = new JRadioButton();
    private JTextField tf_startStory = new JTextField("2", 3);
    private JTextField tf_endStory = new JTextField("6", 3);
    private JTextField tf_roomsEachStory = new JTextField("4", 3);
    private JTextField tf_roomNumber = new JTextField("201", 5);
    private JComboBox<Building> cb_building = new JComboBox<Building>();
    private JTextField tf_rent = new JTextField("500", 5);
    private JTextField tf_deposit = new JTextField("500", 5);
    private Room newRoom;

    public AddRoomPanel() {
	this.rentManager = RentManager.rm;
	rb_addMultiRooms.setText(RentManagerMain
		.getString("button.add.multi.rooms"));
	rb_addOneRoom.setText(RentManagerMain.getString("button.add.one.room"));
	FocusListener fl = new FocusListener() {
	    public void focusGained(FocusEvent e) {
		((JTextComponent) e.getSource()).selectAll();
	    }

	    public void focusLost(FocusEvent e) {
	    }

	};
	tf_startStory.addFocusListener(fl);
	tf_endStory.addFocusListener(fl);
	tf_roomsEachStory.addFocusListener(fl);
	tf_roomNumber.addFocusListener(fl);
	tf_rent.addFocusListener(fl);
	tf_deposit.addFocusListener(fl);

	@SuppressWarnings("unchecked")
	Enumeration<Building> e = rentManager.getRoot().children();
	while (e.hasMoreElements())
	    cb_building.addItem(e.nextElement());

	JLabel l_icon = new JLabel(rentManager.roomIcon);
	ButtonGroup group = new ButtonGroup();
	group.add(rb_addOneRoom);
	group.add(rb_addMultiRooms);
	JLabel l_building = new JLabel(
		RentManagerMain.getString("label.room.bldg"));
	JLabel l_startStory = new JLabel(
		RentManagerMain.getString("label.room.start.story"));
	JLabel l_endStory = new JLabel(
		RentManagerMain.getString("label.room.end.story"));
	JLabel l_roomsEachStory = new JLabel(
		RentManagerMain.getString("label.room.rooms.each.story"));
	JLabel l_roomNumber = new JLabel(
		RentManagerMain.getString("label.room.num"));
	JLabel l_rent = new JLabel(RentManagerMain.getString("label.room.rent"));
	JLabel l_deposit = new JLabel(
		RentManagerMain.getString("label.room.deposit"));

	this.setLayout(new GridBagLayout());
	GridBagConstraints c = new GridBagConstraints();
	c.gridx = 0;
	c.gridy = 0;
	c.gridheight = 3;
	c.insets = new Insets(0, 15, 0, 15);
	this.add(l_icon, c);

	c = new GridBagConstraints();
	c.gridx = 1;
	c.gridy = 0;
	c.gridwidth = 2;
	c.insets = new Insets(15, 0, 0, 0);
	c.anchor = GridBagConstraints.LINE_START;
	this.add(rb_addOneRoom, c);

	c = new GridBagConstraints();
	c.gridx = 3;
	c.gridy = 0;
	c.gridwidth = 2;
	c.insets = new Insets(15, 0, 0, 0);
	c.anchor = GridBagConstraints.LINE_START;
	this.add(rb_addMultiRooms, c);

	c = new GridBagConstraints();
	c.gridx = 1;
	c.gridy = 1;
	c.insets = new Insets(10, 0, 0, 5);
	c.anchor = GridBagConstraints.LINE_END;
	this.add(l_building, c);

	c = new GridBagConstraints();
	c.gridx = 2;
	c.gridy = 1;
	c.insets = new Insets(10, 0, 0, 0);
	c.anchor = GridBagConstraints.LINE_START;
	this.add(cb_building, c);

	c = new GridBagConstraints();
	c.gridx = 1;
	c.gridy = 2;
	c.insets = new Insets(10, 0, 0, 5);
	c.anchor = GridBagConstraints.LINE_END;
	this.add(l_rent, c);

	c = new GridBagConstraints();
	c.gridx = 2;
	c.gridy = 2;
	c.insets = new Insets(10, 0, 0, 10);
	c.anchor = GridBagConstraints.LINE_START;
	this.add(tf_rent, c);

	c = new GridBagConstraints();
	c.gridx = 3;
	c.gridy = 2;
	c.insets = new Insets(10, 0, 0, 5);
	c.anchor = GridBagConstraints.LINE_END;
	this.add(l_deposit, c);

	c = new GridBagConstraints();
	c.gridx = 4;
	c.gridy = 2;
	c.insets = new Insets(10, 0, 0, 0);
	c.anchor = GridBagConstraints.LINE_START;
	this.add(tf_deposit, c);

	JPanel p_oneRoom = new JPanel(new GridBagLayout());
	c = new GridBagConstraints();
	c.gridx = 0;
	c.gridy = 0;
	c.insets = new Insets(0, 0, 0, 5);
	c.anchor = GridBagConstraints.LINE_END;
	p_oneRoom.add(l_roomNumber, c);

	c = new GridBagConstraints();
	c.gridx = 1;
	c.gridy = 0;
	c.insets = new Insets(0, 0, 0, 0);
	c.anchor = GridBagConstraints.LINE_START;
	p_oneRoom.add(tf_roomNumber, c);

	JPanel p_multiRooms = new JPanel(new GridBagLayout());
	c = new GridBagConstraints();
	c.gridx = 0;
	c.gridy = 0;
	c.insets = new Insets(0, 0, 0, 5);
	c.anchor = GridBagConstraints.LINE_END;
	p_multiRooms.add(l_startStory, c);

	c = new GridBagConstraints();
	c.gridx = 1;
	c.gridy = 0;
	c.insets = new Insets(0, 0, 0, 10);
	c.anchor = GridBagConstraints.LINE_START;
	p_multiRooms.add(tf_startStory, c);

	c = new GridBagConstraints();
	c.gridx = 2;
	c.gridy = 0;
	c.insets = new Insets(0, 0, 0, 5);
	c.anchor = GridBagConstraints.LINE_END;
	p_multiRooms.add(l_endStory, c);

	c = new GridBagConstraints();
	c.gridx = 3;
	c.gridy = 0;
	c.insets = new Insets(0, 0, 0, 0);
	c.anchor = GridBagConstraints.LINE_START;
	p_multiRooms.add(tf_endStory, c);

	c = new GridBagConstraints();
	c.gridx = 0;
	c.gridy = 1;
	c.insets = new Insets(10, 0, 0, 5);
	c.anchor = GridBagConstraints.LINE_END;
	p_multiRooms.add(l_roomsEachStory, c);

	c = new GridBagConstraints();
	c.gridx = 1;
	c.gridy = 1;
	c.insets = new Insets(10, 0, 0, 0);
	c.anchor = GridBagConstraints.LINE_START;
	p_multiRooms.add(tf_roomsEachStory, c);

	final JPanel p_cards = new JPanel(new CardLayout());
	p_cards.add(p_oneRoom, rb_addOneRoom.getText());
	p_cards.add(p_multiRooms, rb_addMultiRooms.getText());

	ActionListener al = new ActionListener() {

	    public void actionPerformed(ActionEvent e) {
		CardLayout cl = (CardLayout) p_cards.getLayout();
		cl.show(p_cards, ((JRadioButton) e.getSource()).getText());
	    }

	};

	rb_addOneRoom.addActionListener(al);
	rb_addMultiRooms.addActionListener(al);
	rb_addOneRoom.setSelected(true);

	c = new GridBagConstraints();
	c.gridx = 1;
	c.gridy = 3;
	c.gridwidth = 4;
	c.insets = new Insets(10, 0, 15, 15);
	this.add(p_cards, c);
	updateData(null);
    }

    public boolean addElement() {
	boolean success = false;
	if (cb_building.getSelectedItem() == null)
	    return success;
	Building building = (Building) cb_building.getSelectedItem();
	if (rb_addOneRoom.isSelected()) {
	    int roomNumber = 0;
	    int rent = 0;
	    int deposit = 0;
	    if (tf_rent.getText() == null || tf_rent.getText().equals("")) {
		tf_rent.setText("0");
	    }
	    if (tf_deposit.getText() == null || tf_deposit.getText().equals("")) {
		tf_deposit.setText("0");
	    }
	    try {
		roomNumber = Integer.parseInt(tf_roomNumber.getText());
		rent = Integer.parseInt(tf_rent.getText());
		deposit = Integer.parseInt(tf_deposit.getText());
	    } catch (NumberFormatException e) {
		tf_roomNumber.setText(201 + "");
		tf_rent.setText("500");
		tf_deposit.setText("500");
		return false;
	    }
	    int storyNumber = roomNumber / 100;
	    if (storyNumber > building.getNumberStories()) {
		String message = RentManagerMain
			.getString("message.not.enough.stories");
		int result = JOptionPane.showConfirmDialog(this,
			"<html><body><p style='width:200px'>" + message
				+ "</p></body></html>",
			RentManagerMain.getString("title.message"),
			JOptionPane.YES_NO_OPTION);
		if (result == JOptionPane.OK_OPTION)
		    building.setNumberStories(storyNumber);
		else
		    return false;
	    }
	    success = addOneRoom(building, roomNumber, rent, deposit);
	    return success;
	} else if (rb_addMultiRooms.isSelected()) {
	    int startStory = 0;
	    int endStory = 0;
	    int roomsEachStory = 0;
	    int rent = 0;
	    int deposit = 0;
	    if (tf_rent.getText() == null || tf_rent.getText().equals("")) {
		tf_rent.setText("0");
	    }
	    if (tf_deposit.getText() == null || tf_deposit.getText().equals("")) {
		tf_deposit.setText("0");
	    }
	    try {
		startStory = Integer.parseInt(tf_startStory.getText());
		endStory = Integer.parseInt(tf_endStory.getText());
		roomsEachStory = Integer.parseInt(tf_roomsEachStory.getText());
		rent = Integer.parseInt(tf_rent.getText());
		deposit = Integer.parseInt(tf_deposit.getText());
	    } catch (NumberFormatException e) {
		tf_startStory.setText("2");
		tf_endStory.setText("6");
		tf_roomsEachStory.setText("4");
		tf_rent.setText("500");
		tf_deposit.setText("500");
		return false;
	    }
	    if (endStory > building.getNumberStories()) {
		String message = RentManagerMain
			.getString("message.not.enough.stories.multi.rooms");
		int result = JOptionPane.showConfirmDialog(this,
			"<html><body><p style='width:200px'>" + message
				+ "</p></body></html>",
			RentManagerMain.getString("title.message"),
			JOptionPane.YES_NO_OPTION);
		if (result == JOptionPane.OK_OPTION)
		    building.setNumberStories(endStory);
		else
		    return false;
	    }
	    for (int s = startStory; s <= endStory; s++)
		for (int r = 1; r <= roomsEachStory; r++) {
		    success |= addOneRoom(building, 100 * s + r, rent, deposit);
		}
	    return success;
	}
	return success;
    }

    private boolean addOneRoom(Building building, int roomNumber, int rent,
	    int deposit) {
	Room newRoomTry = new Room(roomNumber, rent, deposit);
	int storyNumber = newRoomTry.getStoryNumber();
	DefaultTreeModel tm = ((DefaultTreeModel) rentManager.getTreePanel()
		.getTree().getModel());
	Building.Story story;
	boolean isOnNewStory = ((story = building.getStory(storyNumber)) == null);
	if (isOnNewStory) {
	    story = building.new Story(storyNumber);
	    int index = ElementComparator.getInsertIndex(building, story);
	    tm.insertNodeInto(story, building, index);
	}
	int index = ElementComparator.getInsertIndex(story, newRoomTry);
	if (index == -1)
	    return false;
	tm.insertNodeInto(newRoomTry, story, index);
	rentManager.getTablePanel().addRoom(newRoomTry, isOnNewStory);
	newRoom = newRoomTry;
	return true;
    }

    public MutableTreeNode getNewElement() {
	return newRoom;
    }

    @SuppressWarnings("unchecked")
    public void updateData(Object newElement) {
	if (newElement == null) {
	    cb_building.removeAllItems();
	    Enumeration<Building> e = rentManager.getRoot().children();
	    while (e.hasMoreElements())
		cb_building.addItem(e.nextElement());
	    rb_addOneRoom.setSelected(true);
	} else if (newElement instanceof Building) {
	    updateData(null);
	    cb_building.setSelectedItem(newElement);
	} else if (newElement instanceof Building.Story) {
	    Building.Story story = (Building.Story) newElement;
	    int rooms = story.getChildCount();
	    rb_addOneRoom.setSelected(true);
	    tf_roomNumber.setText((100 * story.getStoryNumber() + rooms) + "");
	}
    }
}
