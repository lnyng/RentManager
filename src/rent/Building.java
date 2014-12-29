package rent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;

public class Building implements MutableTreeNode, Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 3793787939870140025L;
    private DefaultMutableTreeNode root;
    private String address;
    private String name;
    private int numberStories;
    private double electricityPrice;
    private double waterPrice;
    private int internetPrice;
    private int cleaningPrice;
    private int acPrice;
    private ArrayList<Story> stories;

    public Building(String address, String name, int numberStories,
	    double electricityPrice, double waterPrice, int internetPrice,
	    int cleaningPrice, int acPrice) {
	this.address = address;
	this.name = name;
	this.numberStories = numberStories;
	stories = new ArrayList<Story>();
	this.electricityPrice = electricityPrice;
	this.waterPrice = waterPrice;
	this.internetPrice = internetPrice;
	this.cleaningPrice = cleaningPrice;
	this.acPrice = acPrice;
    }

    public String getAddress() {
	return address;
    }

    public String getName() {
	return name;
    }

    public ArrayList<Story> getStories() {
	return stories;
    }

    public double getElectricityPrice() {
	return electricityPrice;
    }

    public double getWaterPrice() {
	return waterPrice;
    }

    public int getInternetPrice() {
	return internetPrice;
    }

    public int getCleaningPrice() {
	return cleaningPrice;
    }

    public int getNumberStories() {
	return numberStories;
    }

    public void setNumberStories(int newNumStories) {
	numberStories = newNumStories;
    }

    public void setAddress(String newAddress) {
	address = newAddress;
    }

    public void setName(String newName) {
	name = newName;
    }

    public void setElectricityPrice(double newElectricityPrice) {
	electricityPrice = newElectricityPrice;
    }

    public void setWaterPrice(double newWaterPrice) {
	waterPrice = newWaterPrice;
    }

    public void setInternetPrice(int newInternetPrice) {
	internetPrice = newInternetPrice;
    }

    public void setCleaningPrice(int newCleaningPrice) {
	cleaningPrice = newCleaningPrice;
    }

    public int getAcPrice() {
	return acPrice;
    }

    public void setAcPrice(int acPrice) {
	this.acPrice = acPrice;
    }

    public Story getStory(int storyNumber) {
	for (Story s : stories) {
	    if (s.getStoryNumber() == storyNumber)
		return s;
	}
	return null;
    }

    public String toString() {
	return name;
    }

    public class Story implements MutableTreeNode, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2391104149375083293L;
	private ArrayList<Room> rooms;
	private int storyNumber;

	public Story(int storyNumber) {
	    this.storyNumber = storyNumber;
	    rooms = new ArrayList<Room>();
	}

	public Building getBuilding() {
	    return Building.this;
	}

	public int getStoryNumber() {
	    return storyNumber;
	}

	public String toString() {
	    return storyNumber + "";
	}

	@Override
	public TreeNode getChildAt(int childIndex) {
	    return rooms.get(childIndex);
	}

	@Override
	public int getChildCount() {
	    return rooms.size();
	}

	@Override
	public TreeNode getParent() {
	    return getBuilding();
	}

	@Override
	public int getIndex(TreeNode node) {
	    return rooms.indexOf(node);
	}

	@Override
	public boolean getAllowsChildren() {
	    return true;
	}

	@Override
	public boolean isLeaf() {
	    return rooms.size() == 0;
	}

	@Override
	public Enumeration<Room> children() {
	    return Collections.enumeration(rooms);
	}

	@Override
	public void insert(MutableTreeNode child, int index) {
	    rooms.add(index, (Room) child);
	    child.setParent(this);
	}

	@Override
	public void remove(int index) {
	    rooms.remove(index);
	}

	@Override
	public void remove(MutableTreeNode node) {
	    rooms.remove(node);
	}

	@Override
	public void setUserObject(Object object) {
	}

	@Override
	public void removeFromParent() {
	    getBuilding().remove(this);
	}

	@Override
	public void setParent(MutableTreeNode newParent) {
	}
    }

    @Override
    public TreeNode getChildAt(int childIndex) {
	return stories.get(childIndex);
    }

    @Override
    public int getChildCount() {
	return stories.size();
    }

    @Override
    public TreeNode getParent() {
	return root;
    }

    @Override
    public int getIndex(TreeNode node) {
	return stories.indexOf(node);
    }

    @Override
    public boolean getAllowsChildren() {
	return true;
    }

    @Override
    public boolean isLeaf() {
	return stories.size() == 0;
    }

    @Override
    public Enumeration<Story> children() {
	return Collections.enumeration(stories);
    }

    @Override
    public void insert(MutableTreeNode child, int index) {
	stories.add(index, (Story) child);
	child.setParent(this);
    }

    @Override
    public void remove(int index) {
	stories.remove(index);
    }

    @Override
    public void remove(MutableTreeNode node) {
	stories.remove(node);
    }

    @Override
    public void setUserObject(Object object) {
	return;
    }

    @Override
    public void removeFromParent() {
	root.remove(this);
    }

    @Override
    public void setParent(MutableTreeNode newParent) {
	root = (DefaultMutableTreeNode) newParent;
    }
}
