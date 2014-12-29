package rent;

import java.util.ArrayList;
import java.util.Enumeration;

import javax.swing.table.AbstractTableModel;

@SuppressWarnings("serial")
public class BillTableModel extends AbstractTableModel {

    private RentManager rentManager;
    public static final int ROOM = 0;
    public static final int TENANT = 1;
    public static final int RENT = 2;
    public static final int WATER = 3;
    public static final int ELECTRICITY = 4;
    public static final int CLEANING = 5;
    public static final int INTERNET = 6;
    public static final int AC = 7;
    public static final int OTHER_FEE = 8;
    public static final int TOTAL = 9;
    private String[] columnIdentifiers;
    private ArrayList<ArrayList<Object[]>> stories = new ArrayList<ArrayList<Object[]>>();
    private ArrayList<int[]> indices = new ArrayList<int[]>();
    private static Class<?>[] columnClasses = new Class<?>[] { Room.class,
	    Tenant.class, Integer.class, Integer.class, Integer.class,
	    Integer.class, Boolean.class, Boolean.class, Integer.class,
	    Integer.class };

    public BillTableModel(Building bldg) {
	rentManager = RentManager.rm;
	columnIdentifiers = new String[] {
		rentManager.getString("table.header.room.num"),
		rentManager.getString("table.header.tenant"),
		rentManager.getString("table.header.rent"),
		rentManager.getString("table.header.water"),
		rentManager.getString("table.header.electricity"),
		rentManager.getString("table.header.cleaning"),
		rentManager.getString("table.header.internet"),
		rentManager.getString("table.header.ac"),
		rentManager.getString("table.header.other.fee"),
		rentManager.getString("table.header.total") };
	Enumeration<Building.Story> e_story = bldg.children();
	int storyIndex = 0;
	while (e_story.hasMoreElements()) {
	    Building.Story story = e_story.nextElement();
	    stories.add(new ArrayList<Object[]>());
	    Enumeration<Room> e_room = story.children();
	    int roomIndex = 0;
	    while (e_room.hasMoreElements()) {
		Room room = e_room.nextElement();
		stories.get(storyIndex).add(generateRow(room));
		indices.add(new int[] { storyIndex, roomIndex });
		roomIndex++;
	    }
	    storyIndex++;
	}
	fireTableRowsInserted(0, getRowCount() - 1);
    }

    public void addRoom(Room room, boolean isOnNewStory) {
	int storyIndex = room.getBuilding().getIndex(room.getParent());
	int roomIndex = room.getParent().getIndex(room);
	if (isOnNewStory)
	    stories.add(storyIndex, new ArrayList<Object[]>());
	stories.get(storyIndex).add(roomIndex, generateRow(room));
	int counter = 0;
	for (int i = 0; i < storyIndex; i++)
	    counter += stories.get(i).size();
	counter += roomIndex;
	indices.add(counter, new int[] { storyIndex, roomIndex });
	for (int i = counter + 1; i < indices.size()
		&& indices.get(i)[0] == storyIndex; i++)
	    indices.get(i)[1]++;
	fireTableRowsInserted(counter, counter);
    }

    public void updateRoom(Room room) {
	int roomRow = getRoomRow(room);
	int[] index = indices.get(roomRow);
	stories.get(index[0]).set(index[1], generateRow(room));
	fireTableRowsUpdated(roomRow, roomRow);
    }

    public void removeRoom(Room room) {
	int roomRow = getRoomRow(room);
	int[] index = indices.get(roomRow);
	indices.remove(roomRow);
	for (int i = roomRow; i < indices.size()
		&& indices.get(i)[0] == index[0]; i++)
	    indices.get(i)[1]--;
	stories.get(index[0]).remove(index[1]);
	fireTableRowsDeleted(roomRow, roomRow);
    }

    public void removeStory(Building.Story story) {
	int storyIndex = story.getParent().getIndex(story);
	int storySize = stories.get(storyIndex).size();
	int counter = 0;
	for (int i = 0; i < storyIndex; i++)
	    counter += stories.get(i).size();
	for (int i = 0; i < storySize; i++)
	    indices.remove(counter);
	for (int i = counter; i < indices.size(); i++)
	    indices.get(i)[0]--;
	stories.remove(storyIndex);
	fireTableRowsDeleted(counter, counter + storySize - 1);
    }

    public void updateTenant(Tenant tenant) {
	int roomRow = getRoomRow((Room) tenant.getParent());
	Object[] row = getRow(roomRow);
	if (row[TENANT].equals(RentManager.nullTenant)
		|| !((Tenant) row[TENANT]).isContractSigner()) {
	    row[TENANT] = tenant;
	}
	fireTableCellUpdated(roomRow, TENANT);
    }

    public void removeTenant(Tenant tenant) {
	int roomRow = getRoomRow((Room) tenant.getParent());
	Object[] row = getRow(roomRow);
	if (row[TENANT].equals(tenant)) {
	    Room room = (Room) tenant.getParent();
	    if (room.getChildCount() > 1)
		row[TENANT] = room.getChildAt((room.getIndex(tenant) + 1)
			% room.getChildCount());
	    else
		row[1] = RentManager.nullTenant;
	}
	fireTableCellUpdated(roomRow, TENANT);
    }

    private static int getTotal(Object[] row) {
	if (row[1].equals(RentManager.nullTenant))
	    return 0;
	Room room = (Room) row[ROOM];
	double waterBill = ((Integer) row[WATER] - room.getWaterRecord())
		* room.getBuilding().getWaterPrice();
	double electricityBill = ((Integer) row[ELECTRICITY] - room
		.getElectricityRecord())
		* room.getBuilding().getElectricityPrice();
	int internetBill = ((Boolean) row[INTERNET]) ? room.getBuilding()
		.getInternetPrice() : 0;
	int acBill = ((Boolean) row[AC]) ? room.getBuilding().getAcPrice() : 0;
	int total = (int) ((Integer) row[RENT] + waterBill + electricityBill
		+ room.getCleaningPrice() + internetBill + acBill
		+ (Integer) row[OTHER_FEE] + 0.5);
	return total;
    }

    public void updateTotal(int r) {
	Object[] row = getRow(r);
	row[TOTAL] = getTotal(row);
	fireTableCellUpdated(r, TOTAL);
    }

    public Object[] getRow(int r) {
	int[] index = indices.get(r);
	return stories.get(index[0]).get(index[1]);
    }

    public static Class<?>[] getColumnClasses() {
	return columnClasses;
    }

    public ArrayList<ArrayList<Object[]>> getStories() {
	return stories;
    }

    public ArrayList<int[]> getIndices() {
	return indices;
    }

    @Override
    public int getRowCount() {
	return indices.size();
    }

    @Override
    public int getColumnCount() {
	return columnIdentifiers.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
	Object[] row = getRow(rowIndex);
	return row[columnIndex];
    }

    public String getColumnName(int c) {
	return columnIdentifiers[c];
    }

    public Class<?> getColumnClass(int c) {
	return columnClasses[c];
    }

    public boolean isCellEditable(int r, int c) {
	if (!rentManager.isGlobalEditable())
	    return false;
	if (c == CLEANING || c == WATER || c == ELECTRICITY || c == INTERNET
		|| c == AC || c == OTHER_FEE)
	    return true;
	return false;
    }

    public void setValueAt(Object aValue, int r, int c) {
	if (isCellEditable(r, c)) {
	    int[] index = indices.get(r);
	    stories.get(index[0]).get(index[1])[c] = (aValue != null) ? aValue
		    : 0;
	    fireTableCellUpdated(r, c);
	}
    }

    public int getRoomRow(Room room) {
	int storyIndex = room.getBuilding().getIndex(room.getParent());
	int roomIndex = room.getParent().getIndex(room);
	int counter = 0;
	for (int i = 0; i < storyIndex; i++)
	    counter += stories.get(i).size();
	counter += roomIndex;
	return counter;
    }

    private static Object[] generateRow(Room room) {
	Tenant tenant = (Tenant) room.getContractSigner();
	Tenant signer = (tenant == null) ? RentManager.nullTenant : tenant;
	Integer rent = new Integer(room.getRent());
	Integer water = room.getWaterRecord() + room.getWaterUsed();
	Integer electricity = room.getElectricityRecord()
		+ room.getElectricityUsed();
	Integer cleaning = room.getCleaningPrice();
	Boolean internet = room.isUseInternet();
	Boolean ac = room.isUseAC();
	Integer otherFee = room.getOtherFee();
	Integer total = 0;
	Object[] row = new Object[] { room, signer, rent, water, electricity,
		cleaning, internet, ac, otherFee, total };
	row[TOTAL] = getTotal(row);
	return row;
    }
}
