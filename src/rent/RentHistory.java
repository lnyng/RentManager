package rent;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;

public class RentHistory implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 3097630419327196359L;
    private LinkedList<String> rentDataNames;

    public RentHistory() {
	setRentDataNames(new LinkedList<String>());
    }

    public LinkedList<String> getRentDataNames() {
	return rentDataNames;
    }

    public void setRentDataNames(LinkedList<String> rentDataNames) {
	this.rentDataNames = rentDataNames;
    }

    public RentData getLatestRentData() throws ClassNotFoundException,
	    IOException {
	return getRentData(rentDataNames.size() - 1);
    }

    public RentData getRentData(int index) throws IOException,
	    ClassNotFoundException {
	if (rentDataNames.size() == 0)
	    return null;
	File f = new File("data/" + rentDataNames.get(index));
	InputStream file = new FileInputStream(f);
	InputStream buffer = new BufferedInputStream(file);
	ObjectInput input = new ObjectInputStream(buffer);
	RentData data = (RentData) input.readObject();
	input.close();
	return data;
    }

    public boolean isInNewMonth(RentData rentData) {
	if (getRentDataNames().size() == 0)
	    return true;
	return !getRentDataNames().getLast().substring(1, 8)
		.equals(rentData.getDataMonth());
    }

    public ArrayList<String> getMissingFiles() {
	ArrayList<String> missing = new ArrayList<String>();
	for (String name : rentDataNames) {
	    File file = new File("data/" + name);
	    if (!file.exists())
		missing.add(name);
	}
	return missing;
    }
}
