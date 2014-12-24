package rent;
import javax.swing.JTabbedPane;

@SuppressWarnings("serial")
public class AddElementTabbedPane extends JTabbedPane {

    private AddTenantPanel addTenant;
    private AddRoomPanel addRoom;
    private AddBuildingPanel addBuilding;
    private RentManager rentManager;
    public final static int TENANT_PANEL = 0;
    public final static int ROOM_PANEL = 1;
    public final static int BLDG_PANEL = 2;

    public AddElementTabbedPane() {
	this.rentManager = RentManager.rm;
	addTenant = new AddTenantPanel();
	addRoom = new AddRoomPanel();
	addBuilding = new AddBuildingPanel();

	this.addTab(rentManager.getString("class.tenant"),
		rentManager.tenantSmallIcon, addTenant,
		rentManager.getString("tip.add.tenant"));
	this.addTab(rentManager.getString("class.room"),
		rentManager.roomSmallIcon, addRoom,
		rentManager.getString("tip.add.room"));
	this.addTab(rentManager.getString("class.bldg"),
		rentManager.buildingSmallIcon, addBuilding,
		rentManager.getString("tip.add.bldg"));
    }

    public AddTenantPanel getTenantPanel() {
	return addTenant;
    }

    public AddRoomPanel getRoomPanel() {
	return addRoom;
    }

    public AddBuildingPanel getBuildingPanel() {
	return addBuilding;
    }
}
