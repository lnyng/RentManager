package rent;
import java.awt.BorderLayout;
import java.text.MessageFormat;
import java.util.Enumeration;

import javax.swing.JTextArea;

@SuppressWarnings("serial")
public class StoryInfoPanel extends ElementInfoPanel {
    private RentManager rentManager;
    private Building.Story currStory;
    private JTextArea ta_story = new JTextArea();

    public StoryInfoPanel() {
	setLayout(new BorderLayout());
	rentManager = RentManager.rm;
	ta_story.setFont(getFont().deriveFont(16f));
	ta_story.setEditable(false);
	ta_story.setOpaque(false);
	ta_story.setWrapStyleWord(true);
	ta_story.setLineWrap(true);
	add(ta_story, BorderLayout.CENTER);
    }

    @Override
    public void updateInfo(Object element) {
	currStory = (Building.Story) element;
	int storyNumber = currStory.getStoryNumber();
	int numRooms = currStory.getChildCount();
	int numTenants = RentManager.getNumElements(2, currStory);
	int numOccupied = 0;
	int waterUsed = 0;
	int electricityUsed = 0;
	Enumeration<Room> e = currStory.children();
	while (e.hasMoreElements()) {
	    Room room = e.nextElement();
	    waterUsed += room.getWaterUsed();
	    electricityUsed += room.getElectricityUsed();
	    if (room.isOccupied())
		numOccupied++;
	}
	String text = MessageFormat.format(
		rentManager.getString("message.story.info"), storyNumber,
		numRooms, numTenants, numOccupied, waterUsed, electricityUsed);
	ta_story.setText(text);
    }

    @Override
    public void updateInfo() {
	updateInfo(currStory);
    }

}
