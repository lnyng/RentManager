package rent;
import java.util.Enumeration;

import javax.swing.tree.MutableTreeNode;

public class ElementComparator {

    @SuppressWarnings("unchecked")
    public static int getInsertIndex(MutableTreeNode parent,
	    MutableTreeNode child) {
	Enumeration<Object> e = parent.children();
	int index = 0;
	int childValue = 0;
	boolean isNumeric = true;
	try {
	    childValue = Integer.parseInt(child.toString());
	} catch (NumberFormatException ext) {
	    isNumeric = false;
	}
	while (e.hasMoreElements()) {
	    Object next = e.nextElement();
	    int compare = -1;
	    if (isNumeric) {
		int nextValue = 0;
		try {
		    nextValue = Integer.parseInt(next.toString());
		    if (childValue == nextValue)
			return -1;
		    else if (childValue < nextValue)
			return index;
		} catch (NumberFormatException ext) {
		    isNumeric = false;
		    compare = next.toString().compareTo(child.toString());
		    if (compare == 0)
			return -1;
		    else if (compare > 0)
			return index;
		}
	    } else {
		compare = next.toString().compareTo(child.toString());
		if (compare == 0)
		    return -1;
		else if (compare > 0)
		    return index;
	    }
	    index++;
	}
	return index;
    }

}
