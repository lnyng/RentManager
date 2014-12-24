package rent;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.tree.DefaultMutableTreeNode;

public class RentData implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 7720763553031134027L;
    private DefaultMutableTreeNode root;
    private String dataMonth;
    private int version;

    public RentData(DefaultMutableTreeNode root) {
	this.root = root;
	setDataMonth(new SimpleDateFormat("MM-yyyy").format(new Date()));
	version = 0;
    }

    public DefaultMutableTreeNode getRoot() {
	return root;
    }

    public void setRoot(DefaultMutableTreeNode root) {
	this.root = root;
    }

    /**
     * return the month and year of this rent data in the format of MM-yyyy.
     */
    public String getDataMonth() {
	return dataMonth;
    }

    public void setDataMonth(String dataMonth) {
	this.dataMonth = dataMonth;
    }

    public void nextMonth() {
	int month = Integer.parseInt(dataMonth.substring(0, 2));
	int year = Integer.parseInt(dataMonth.substring(3));
	month = month % 12 + 1;
	year = (month == 1) ? year + 1 : year;
	dataMonth = month + "-" + year;
	if (dataMonth.length() == 6)
	    dataMonth = "0" + dataMonth;
    }

    public int getVersion() {
	return version;
    }

    public void setVersion(int version) {
	this.version = version;
    }

    public void increaseVersion() {
	version++;
    }

    public String getFilename() {
	return "[" + getDataMonth() + "]_" + getVersion() + ".ser";
    }
    
    public String getTextFilename() {
	return "[" + getDataMonth() + "]_" + getVersion() + ".txt";
    }
}
