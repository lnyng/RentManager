package rent;
import java.awt.CardLayout;
import java.awt.Dimension;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class InfoPanel extends JPanel {
    private RentManager rentManager;
    private ElementInfoPanel propertyInfo;
    private ElementInfoPanel buildingInfo;
    private ElementInfoPanel storyInfo;
    private ElementInfoPanel roomInfo;
    private ElementInfoPanel tenantInfo;
    private ElementInfoPanel currPanel = propertyInfo;
    private final static String PROPERTY = "property";
    private final static String BUILDING = "building";
    private final static String STORY = "story";
    private final static String ROOM = "room";
    private final static String TENANT = "tenant";

    public InfoPanel() {
	rentManager = RentManager.rm;
	this.removeAll();
	propertyInfo = new PropertyInfoPanel();
	buildingInfo = new BuildingInfoPanel();
	storyInfo = new StoryInfoPanel();
	roomInfo = new RoomInfoPanel();
	tenantInfo = new TenantInfoPanel();
	setLayout(new CardLayout());
	add(propertyInfo, PROPERTY);
	add(buildingInfo, BUILDING);
	add(storyInfo, STORY);
	add(roomInfo, ROOM);
	add(tenantInfo, TENANT);
	switchPanel(null);
	setPreferredSize(new Dimension(800, 180));
	setMinimumSize(new Dimension(500, 180));
    }

    public void updateCurrPanel() {
	currPanel.updateInfo();
    }

    public void switchPanel(Object element) {
	CardLayout c = (CardLayout) getLayout();
	if (element == null || element.equals(rentManager.getRoot())) {
	    propertyInfo.updateInfo(element);
	    c.show(this, PROPERTY);
	    currPanel = propertyInfo;
	} else if (element instanceof Building) {
	    buildingInfo.updateInfo(element);
	    c.show(this, BUILDING);
	    currPanel = buildingInfo;
	} else if (element instanceof Building.Story) {
	    storyInfo.updateInfo(element);
	    c.show(this, STORY);
	    currPanel = storyInfo;
	} else if (element instanceof Room) {
	    roomInfo.updateInfo(element);
	    c.show(this, ROOM);
	    currPanel = roomInfo;
	} else if (element instanceof Tenant) {
	    tenantInfo.updateInfo(element);
	    c.show(this, TENANT);
	    currPanel = tenantInfo;
	}
    }
}
