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

	this.addTab(RentManagerMain.getString("class.tenant"),
		rentManager.tenantSmallIcon, addTenant,
		RentManagerMain.getString("tip.add.tenant"));
	this.addTab(RentManagerMain.getString("class.room"),
		rentManager.roomSmallIcon, addRoom,
		RentManagerMain.getString("tip.add.room"));
	this.addTab(RentManagerMain.getString("class.bldg"),
		rentManager.buildingSmallIcon, addBuilding,
		RentManagerMain.getString("tip.add.bldg"));
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
