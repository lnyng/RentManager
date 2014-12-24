package rent;
import java.awt.BorderLayout;
import java.text.MessageFormat;

import javax.swing.JTextArea;
import javax.swing.tree.DefaultMutableTreeNode;

@SuppressWarnings("serial")
public class PropertyInfoPanel extends ElementInfoPanel {
    private RentManager rentManager;
    private JTextArea ta_property = new JTextArea();

    public PropertyInfoPanel() {
	rentManager = RentManager.rm;
	setLayout(new BorderLayout());
	ta_property.setFont(getFont().deriveFont(16f));
	ta_property.setEditable(false);
	ta_property.setOpaque(false);
	ta_property.setWrapStyleWord(true);
	ta_property.setLineWrap(true);
	ta_property.setFont(ta_property.getFont().deriveFont(16f));
	add(ta_property, BorderLayout.CENTER);
    }

    @Override
    public void updateInfo(Object element) {
	DefaultMutableTreeNode root = rentManager.getRoot();
	int numBldgs = RentManager.getNumElements(1, root);
	int numStories = RentManager.getNumElements(2, root);
	int numRooms = RentManager.getNumElements(3, root);
	int numTenants = RentManager.getNumElements(4, root);

	String message = MessageFormat.format(
		rentManager.getString("message.property.info"), numBldgs,
		numStories, numRooms, numTenants);
	ta_property.setText(message);
    }

    @Override
    public void updateInfo() {
	updateInfo(null);
    }
}