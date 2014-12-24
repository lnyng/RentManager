package rent;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;

import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;

public class Room implements MutableTreeNode, Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -8856441996006593625L;
    private Building.Story story;
    private int roomNumber;
    private int storyNumber;
    private int rent;
    private int deposit;
    private ArrayList<Tenant> currTenants = new ArrayList<Tenant>();
    private Tenant contractSigner;
    private boolean useInternet;
    private boolean useAC;
    private int waterUsed;
    private int electricityUsed;
    private int waterRecord;
    private int electricityRecord;
    private int otherFee;
    private Date contractChangeDate;
    private String note;
    private String otherFeeNote;
    private boolean isInitializing;

    public Room(int roomNumber, int rent, int deposit) {
	this.roomNumber = roomNumber;
	storyNumber = roomNumber / 100;
	this.rent = rent;
	this.deposit = deposit;
	useInternet = false;
	isInitializing = true;
	contractChangeDate = null;
	note = "";
	otherFeeNote = "";
	otherFee = 0;
    }

    public Building getBuilding() {
	return story.getBuilding();
    }

    public int getStoryNumber() {
	return storyNumber;
    }

    public int getRoomNumber() {
	return roomNumber;
    }

    public boolean isOccupied() {
	return !currTenants.isEmpty();
    }

    public void setRoomNumber(int newRoomNumber) {
	roomNumber = newRoomNumber;
    }

    public Tenant getContractSigner() {
	return contractSigner;
    }

    public void setContractSigner(Tenant signer) {
	contractSigner = signer;
    }

    public int getRent() {
	return rent;
    }

    public void setRent(int rent) {
	this.rent = rent;
    }

    public int getDeposit() {
	return deposit;
    }

    public void setDeposit(int deposit) {
	this.deposit = deposit;
    }

    public boolean isUseInternet() {
	return useInternet;
    }

    public void setUseInternet(boolean useInternet) {
	this.useInternet = useInternet;
    }

    public boolean isUseAC() {
	return useAC;
    }

    public void setUseAC(boolean useAC) {
	this.useAC = useAC;
    }

    public int getWaterUsed() {
	return waterUsed;
    }

    public int getElectricityUsed() {
	return electricityUsed;
    }

    public int getWaterRecord() {
	return waterRecord;
    }

    public void setWaterRecord(int waterRecord) {
	this.waterRecord = waterRecord;
    }

    public int getElectricityRecord() {
	return electricityRecord;
    }

    public void setElectricityRecord(int electricityRecord) {
	this.electricityRecord = electricityRecord;
    }

    public void setWaterUsed(int waterUsed) {
	this.waterUsed = waterUsed;
    }

    public void setElectricityUsed(int electricityUsed) {
	this.electricityUsed = electricityUsed;
    }

    public int getOtherFee() {
	return otherFee;
    }

    public void setOtherFee(int otherFee) {
	this.otherFee = otherFee;
    }

    public Date getContractChangeDate() {
	return contractChangeDate;
    }

    public void setContractChangeDate(Date contractChangeDate) {
	this.contractChangeDate = contractChangeDate;
	if (getChildCount() != 0) {
	    for (Tenant t : currTenants) {
		t.setContractStartDate(contractChangeDate);
	    }
	}
    }

    public String getNote() {
	return note;
    }

    public void setNote(String note) {
	this.note = note;
    }

    public String getOtherFeeNote() {
	return otherFeeNote;
    }

    public void setOtherFeeNote(String otherFeeNote) {
	this.otherFeeNote = otherFeeNote;
    }

    public boolean isInitializing() {
	return isInitializing;
    }

    public void setInitializing(boolean isInitializing) {
	this.isInitializing = isInitializing;
    }

    public String toString() {
	return roomNumber + "";
    }

    @Override
    public TreeNode getChildAt(int childIndex) {
	if (childIndex > currTenants.size() - 1)
	    return null;
	return currTenants.get(childIndex);
    }

    @Override
    public int getChildCount() {
	return currTenants.size();
    }

    @Override
    public TreeNode getParent() {
	return story;
    }

    @Override
    public int getIndex(TreeNode node) {
	return currTenants.indexOf(node);
    }

    @Override
    public boolean getAllowsChildren() {
	return true;
    }

    @Override
    public boolean isLeaf() {
	return currTenants.size() == 0;
    }

    @Override
    public Enumeration<Tenant> children() {
	return Collections.enumeration(currTenants);
    }

    @Override
    public void insert(MutableTreeNode child, int index) {
	currTenants.add(index, (Tenant) child);
	child.setParent(this);
    }

    @Override
    public void remove(int index) {
	Tenant removed = currTenants.remove(index);
	if (getChildCount() == 0)
	    setContractSigner(null);
	else if (removed.isContractSigner()) {
	    setContractSigner((Tenant) getChildAt(0));
	}
    }

    @Override
    public void remove(MutableTreeNode node) {
	currTenants.remove(node);
    }

    @Override
    public void setUserObject(Object object) {
	return;
    }

    @Override
    public void removeFromParent() {
	story.remove(this);
	story = null;
    }

    @Override
    public void setParent(MutableTreeNode newParent) {
	story = (Building.Story) newParent;
	storyNumber = story.getStoryNumber();
    }

    private void readObject(java.io.ObjectInputStream in) throws IOException,
	    ClassNotFoundException {
	in.defaultReadObject();
	if (otherFeeNote == null)
	    otherFeeNote = "";
    }
}
