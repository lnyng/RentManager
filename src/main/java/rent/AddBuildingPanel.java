package rent;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.text.JTextComponent;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;

@SuppressWarnings("serial")
public class AddBuildingPanel extends JPanel implements ElementsAdder {

    private RentManager rentManager;
    private JTextField tf_name = new JTextField();
    private JTextField tf_address = new JTextField(15);
    private JTextField tf_numberStories = new JTextField("6", 3);
    private JTextField tf_electricityPrice = new JTextField("1.0", 3);
    private JTextField tf_waterPrice = new JTextField("1.0", 3);
    private JTextField tf_cleaningPrice = new JTextField("50", 3);
    private JTextField tf_internetPrice = new JTextField("50", 3);
    private JTextField tf_acPrice = new JTextField("50", 3);
    private Building newBuilding;

    public AddBuildingPanel() {
	rentManager = RentManager.rm;
	tf_name.setText(RentManagerMain.getString("default.bldg.name"));
	tf_address.setText(RentManagerMain.getString("default.bldg.address"));

	FocusListener fl = new FocusListener() {
	    public void focusGained(FocusEvent e) {
		((JTextComponent) e.getSource()).selectAll();
	    }

	    public void focusLost(FocusEvent e) {
	    }

	};
	tf_name.addFocusListener(fl);
	tf_numberStories.addFocusListener(fl);
	tf_address.addFocusListener(fl);
	tf_electricityPrice.addFocusListener(fl);
	tf_waterPrice.addFocusListener(fl);
	tf_cleaningPrice.addFocusListener(fl);
	tf_internetPrice.addFocusListener(fl);
	tf_acPrice.addFocusListener(fl);

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
	c.gridheight = 2;
	c.insets = new Insets(15, 15, 0, 15);
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
	c.insets = new Insets(15, 0, 0, 15);
	c.weightx = 0.7;
	c.anchor = GridBagConstraints.LINE_START;
	c.fill = GridBagConstraints.HORIZONTAL;
	this.add(tf_name, c);

	c = new GridBagConstraints();
	c.gridx = 3;
	c.gridy = 0;
	c.insets = new Insets(15, 0, 0, 5);
	c.anchor = GridBagConstraints.LINE_END;
	this.add(l_numberStories, c);

	c = new GridBagConstraints();
	c.gridx = 4;
	c.gridy = 0;
	c.insets = new Insets(15, 0, 0, 15);
	c.weightx = 0.3;
	c.fill = GridBagConstraints.HORIZONTAL;
	c.anchor = GridBagConstraints.LINE_START;
	this.add(tf_numberStories, c);

	c = new GridBagConstraints();
	c.gridx = 1;
	c.gridy = 1;
	c.insets = new Insets(10, 0, 0, 5);
	c.anchor = GridBagConstraints.LINE_END;
	this.add(l_address, c);

	c = new GridBagConstraints();
	c.gridx = 2;
	c.gridy = 1;
	c.gridwidth = 3;
	c.insets = new Insets(10, 0, 0, 15);
	c.fill = GridBagConstraints.HORIZONTAL;
	c.anchor = GridBagConstraints.LINE_START;
	this.add(tf_address, c);

	JPanel p_price = new JPanel(new GridBagLayout());
	c = new GridBagConstraints();
	c.gridx = 0;
	c.gridy = 0;
	c.insets = new Insets(0, 5, 0, 5);
	c.anchor = GridBagConstraints.LINE_END;
	p_price.add(l_electricityPrice, c);

	c = new GridBagConstraints();
	c.gridx = 1;
	c.gridy = 0;
	c.insets = new Insets(0, 0, 0, 15);
	c.anchor = GridBagConstraints.LINE_START;
	p_price.add(tf_electricityPrice, c);

	c = new GridBagConstraints();
	c.gridx = 2;
	c.gridy = 0;
	c.insets = new Insets(0, 0, 0, 5);
	c.anchor = GridBagConstraints.LINE_END;
	p_price.add(l_waterPrice, c);

	c = new GridBagConstraints();
	c.gridx = 3;
	c.gridy = 0;
	c.insets = new Insets(0, 0, 0, 5);
	c.anchor = GridBagConstraints.LINE_START;
	p_price.add(tf_waterPrice, c);

	c = new GridBagConstraints();
	c.gridx = 0;
	c.gridy = 1;
	c.insets = new Insets(10, 5, 0, 5);
	c.anchor = GridBagConstraints.LINE_END;
	p_price.add(l_cleaningPrice, c);

	c = new GridBagConstraints();
	c.gridx = 1;
	c.gridy = 1;
	c.insets = new Insets(10, 0, 0, 15);
	c.anchor = GridBagConstraints.LINE_START;
	p_price.add(tf_cleaningPrice, c);

	c = new GridBagConstraints();
	c.gridx = 2;
	c.gridy = 1;
	c.insets = new Insets(10, 0, 0, 5);
	c.anchor = GridBagConstraints.LINE_END;
	p_price.add(l_internetPrice, c);

	c = new GridBagConstraints();
	c.gridx = 3;
	c.gridy = 1;
	c.insets = new Insets(10, 0, 0, 5);
	c.anchor = GridBagConstraints.LINE_START;
	p_price.add(tf_internetPrice, c);

	c = new GridBagConstraints();
	c.gridx = 0;
	c.gridy = 2;
	c.insets = new Insets(10, 0, 0, 5);
	c.anchor = GridBagConstraints.LINE_END;
	p_price.add(l_acPrice, c);

	c = new GridBagConstraints();
	c.gridx = 1;
	c.gridy = 2;
	c.insets = new Insets(10, 0, 0, 5);
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
	c.gridheight = 3;
	c.insets = new Insets(10, 0, 15, 15);
	this.add(p_price, c);
    }

    public boolean addElement() {
	String address = tf_address.getText();
	String name = tf_name.getText();
	int numberStories = 0;
	double electricityPrice = 0.0;
	double waterPrice = 0.0;
	int cleaningPrice = 0;
	int internetPrice = 0;
	int acPrice = 0;
	if (tf_electricityPrice.getText() == null
		|| tf_electricityPrice.getText().equals("")) {
	    tf_electricityPrice.setText("0");
	}
	if (tf_waterPrice.getText() == null
		|| tf_waterPrice.getText().equals("")) {
	    tf_waterPrice.setText("0");
	}
	if (tf_cleaningPrice.getText() == null
		|| tf_cleaningPrice.getText().equals("")) {
	    tf_cleaningPrice.setText("0");
	}
	if (tf_internetPrice.getText() == null
		|| tf_internetPrice.getText().equals("")) {
	    tf_internetPrice.setText("0");
	}
	if (tf_acPrice.getText() == null || tf_acPrice.getText().equals("")) {
	    tf_acPrice.setText("0");
	}
	try {
	    numberStories = Integer.parseInt(tf_numberStories.getText());
	    electricityPrice = Double
		    .parseDouble(tf_electricityPrice.getText());
	    waterPrice = Double.parseDouble(tf_waterPrice.getText());
	    cleaningPrice = Integer.parseInt(tf_cleaningPrice.getText());
	    internetPrice = Integer.parseInt(tf_internetPrice.getText());
	    acPrice = Integer.parseInt(tf_acPrice.getText());
	} catch (NumberFormatException e) {
	    return false;
	}
	newBuilding = new Building(address, name, numberStories,
		electricityPrice, waterPrice, cleaningPrice, internetPrice,
		acPrice);
	int index = ElementComparator.getInsertIndex(rentManager.getRoot(),
		newBuilding);
	if (index == -1)
	    return false;
	((DefaultTreeModel) rentManager.getTreePanel().getTree().getModel())
		.insertNodeInto(newBuilding, rentManager.getRoot(), index);
	rentManager.getTablePanel().addTable(newBuilding);
	return true;
    }

    public MutableTreeNode getNewElement() {
	return newBuilding;
    }

}
