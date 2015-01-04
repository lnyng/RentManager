package rent;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.tree.TreePath;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

@SuppressWarnings("serial")
public class SearchField extends JTextField {
    private static final int MAX_STANCES_SHOWN = 5;
    private TreeMap<Difference, Tenant> sortedTenants;
    private JButton b_search;

    public SearchField(JButton button) {
	b_search = button;
	sortedTenants = new TreeMap<Difference, Tenant>(
		new Comparator<Difference>() {
		    
		    public int compare(Difference o1, Difference o2) {
			return o1.compareTo(o2);
		    }
		});
	this.addFocusListener(new FocusListener() {
	    
	    public void focusGained(FocusEvent e) {
		SearchField.this.selectAll();
	    }

	    
	    public void focusLost(FocusEvent e) {
	    }
	});
	this.addKeyListener(new KeyListener() {
	    
	    public void keyTyped(KeyEvent e) {
	    }

	    
	    public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
		    b_search.doClick();
		}
	    }

	    
	    public void keyReleased(KeyEvent e) {

	    }
	});
    }

    class Difference implements Comparable<Difference> {
	private int difference;
	private String name;

	public Difference(int difference, String name) {
	    this.difference = difference;
	    this.name = name;
	}

	public int getDifference() {
	    return difference;
	}

	public String getName() {
	    return name;
	}

	
	public int compareTo(Difference o) {
	    if (difference == o.getDifference()) {
		return name.compareTo(o.getName());
	    }
	    return difference > o.getDifference() ? 1 : -1;
	}

    }

    @SuppressWarnings("unchecked")
    public void search() {
	String key = this.getText().trim()
		.toLowerCase(RentManager.rm.getLocale());
	Enumeration<Building> bldgs = RentManager.rm.getRoot().children();
	while (bldgs.hasMoreElements()) {
	    Building bldg = bldgs.nextElement();
	    Enumeration<Building.Story> stories = bldg.children();
	    while (stories.hasMoreElements()) {
		Building.Story story = stories.nextElement();
		Enumeration<Room> rooms = story.children();
		while (rooms.hasMoreElements()) {
		    Room room = rooms.nextElement();
		    Enumeration<Tenant> tenants = room.children();
		    while (tenants.hasMoreElements()) {
			Tenant tenant = tenants.nextElement();
			compare(key, tenant);
		    }
		}
	    }
	}
    }

    private void compare(String key, Tenant tenant) {
	String name = tenant.getName().trim().toLowerCase();
	Difference r = null;
	if (RentManager.rm.getLocale().equals(Locale.SIMPLIFIED_CHINESE)) {
	    HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
	    format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
	    format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
	    boolean match = true;
	    for (int i = 0; i < name.length() && i < key.length() && match; i++) {
		String[] pinyin = null;
		try {
		    pinyin = PinyinHelper.toHanyuPinyinStringArray(
			    name.charAt(i), format);
		} catch (BadHanyuPinyinOutputFormatCombination e) {
		    e.printStackTrace();
		}
		if (pinyin == null) {
		    match = false;
		    break;
		}
		boolean matchChar = false;
		for (int j = 0; j < pinyin.length && !matchChar; j++) {
		    matchChar |= (pinyin[j].charAt(0) == key.charAt(i));
		}
		match &= matchChar;
	    }
	    if (match) {
		r = new Difference(-1, name);
		sortedTenants.put(r, tenant);
		return;
	    }
	}
	if (key.length() > name.length())
	    return;
	int distance = getContainDistance(key, name)[0];
	if (distance >= key.length())
	    return;
	r = new Difference(distance, name);
	sortedTenants.put(r, tenant);
    }

    /**
     * calculate the minimum operations (transposition, substitution) needed to
     * make s2 contains s1
     * 
     * @param s1
     *            the shorter string (key)
     * @param s2
     *            the longer string (instance)
     * @return a two digit integer array containing the minimum number of
     *         operations needed and the alignment index, respectively.
     */
    private int[] getContainDistance(String s1, String s2) {
	int l1 = s1.length();
	int l2 = s2.length();

	// If the key is longer than the instance, the instance will never
	// contains the key.
	if (l1 > l2)
	    return new int[] { Integer.MAX_VALUE, -1 };

	int alignIndex = s2.indexOf(s1);
	// Base case: instance contains the key.
	if (alignIndex != -1)
	    return new int[] { 0, alignIndex };

	// set the contain distance of using substitution or transposition at
	// the last step to their maximum value. Notice that substitution at the
	// front and rear are different.
	int dSubAtFront = l1;
	int dSubAtRear = l1;
	int dTranspos = Integer.MAX_VALUE;

	// dynamic programming

	// align the first (l1 - 1) and last (l1 - 1) character of the key
	// respectively.
	int[] cdSubAtFront = getContainDistance(s1.substring(1, l1), s2);
	int[] cdSubAtRear = getContainDistance(s1.substring(0, l1 - 1), s2);

	// check whether the instance have more characters before the alignment
	// index when substituting at front.
	if (cdSubAtFront[1] > 0) {
	    // if the "missing" characters are the same in both the key and the
	    // instance, no substitution is needed.
	    boolean s = s1.charAt(0) == s2.charAt(cdSubAtFront[1] - 1);
	    dSubAtFront = cdSubAtFront[0] + (s ? 0 : 1);
	}

	// check whether the instance have more characters at the rear after
	// aligning the first (l1 - 1) characters of the key.
	if (s2.length() > cdSubAtRear[1] + l1 - 1) {
	    boolean s = s1.charAt(l1 - 1) == s2.charAt(cdSubAtRear[1] + l1 - 1);
	    dSubAtRear = cdSubAtRear[0] + (s ? 0 : 1);
	}

	int[] cdTranspos = null;
	// check whether the key has 2 or more characters for transposition.
	if (l1 > 1) {
	    cdTranspos = getContainDistance(s1.substring(0, l1 - 2), s2);
	    // check whether the instance has enough characters to contain the
	    // key in this alignment; if so, check whether a transposition can
	    // be done.
	    if (s2.length() > cdTranspos[1] + l1 - 1
		    && (s1.charAt(l1 - 1) == s2.charAt(cdTranspos[1] + l1 - 2))
		    && (s1.charAt(l1 - 2) == s2.charAt(cdTranspos[1] + l1 - 1))) {
		dTranspos = cdTranspos[0] + 1;
	    }
	}

	// compare and find the minimum number of total operations to get to the
	// current state among the three operations. return the corresponding
	// contain distance and alignment index.
	int dMin = Math.min(Math.min(dSubAtFront, dSubAtRear), dTranspos);
	if (dMin == dSubAtFront)
	    return new int[] { dMin, cdSubAtFront[1] - 1 };
	if (dMin == dSubAtRear)
	    return new int[] { dMin, cdSubAtRear[1] };
	return new int[] { dMin, cdTranspos[1] };
    }

    public void showMenu() {
	JPopupMenu pm_search = new JPopupMenu();
	if (sortedTenants.isEmpty()) {
	    JMenuItem noResult = new JMenuItem(
		    RentManagerMain.getString("message.no.relevant.result"));
	    noResult.setEnabled(false);
	    pm_search.add(noResult);
	}
	int count = 0;
	while (!sortedTenants.isEmpty()) {
	    if (count <= MAX_STANCES_SHOWN) {
		final Entry<Difference, Tenant> entry = sortedTenants
			.pollFirstEntry();
		JMenuItem mi_tenant = new JMenuItem(entry.getValue().toString());
		mi_tenant.addActionListener(new ActionListener() {
		    
		    public void actionPerformed(ActionEvent e) {
			TreePath path = TreePanel.getPathToRoot(entry
				.getValue());
			RentManager.rm.getTreePanel().getTree()
				.setSelectionPath(path);
		    }
		});
		pm_search.add(mi_tenant);
		count++;
	    } else
		sortedTenants.pollFirstEntry();
	}
	pm_search.show(this, 0, this.getHeight());
    }
}
