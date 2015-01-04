package rent;

import java.io.Serializable;
import java.util.Date;
import java.util.Enumeration;

import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;

public class Tenant implements MutableTreeNode, Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 560358419325036567L;
    private String name;
    private String id;
    private String phoneNumber;
    private Room room;
    private String note;
    private boolean isContractSigner;
    private Date contractStartDate;
    private boolean isInitializing;

    public Tenant(String name, String id, String phoneNumber, String note,
	    boolean isContractSigner, Date contractStartDate) {
	this.name = name;
	this.id = id;
	this.phoneNumber = phoneNumber;
	this.note = note;
	this.isContractSigner = isContractSigner;
	this.contractStartDate = contractStartDate;
	setInitializing(true);
    }

    public String getName() {
	return name;
    }

    public String getId() {
	return id;
    }

    public String getPhoneNumber() {
	return phoneNumber;
    }

    public String getNote() {
	return note;
    }

    public boolean isContractSigner() {
	return isContractSigner;
    }

    public Date getContractStartDate() {
	return contractStartDate;
    }

    public Building getBuilding() {
	if (room == null)
	    return null;
	return room.getBuilding();
    }

    public void setName(String newName) {
	name = newName;
    }

    public void setId(String newId) {
	id = newId;
    }

    public void setPhoneNumber(String newPhoneNumber) {
	phoneNumber = newPhoneNumber;
    }

    public void setNote(String newNote) {
	note = newNote;
    }

    public void setIsContractSigner(boolean isContractSigner) {
	this.isContractSigner = isContractSigner;
    }

    public void setContractStartDate(Date newDate) {
	this.contractStartDate = newDate;
    }

    public boolean isInitializing() {
	return isInitializing;
    }

    public void setInitializing(boolean isInitializing) {
	this.isInitializing = isInitializing;
    }

    public String toString() {
	return name;
    }

    
    public TreeNode getChildAt(int childIndex) {
	return null;
    }

    
    public int getChildCount() {
	return 0;
    }

    
    public TreeNode getParent() {
	return room;
    }

    
    public int getIndex(TreeNode node) {
	return -1;
    }

    
    public boolean getAllowsChildren() {
	return false;
    }

    
    public boolean isLeaf() {
	return true;
    }

    
    public Enumeration<Object> children() {
	return null;
    }

    
    public void insert(MutableTreeNode child, int index) {
	return;
    }

    
    public void remove(int index) {
	return;
    }

    
    public void remove(MutableTreeNode node) {
	return;
    }

    
    public void setUserObject(Object object) {
	return;
    }

    
    public void removeFromParent() {
	room.remove(this);
	room = null;
    }

    
    public void setParent(MutableTreeNode newParent) {
	room = (Room) newParent;
    }
}
