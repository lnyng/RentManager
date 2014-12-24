package rent;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.text.MessageFormat;
import java.util.Enumeration;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;
import javax.swing.filechooser.FileFilter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;

@SuppressWarnings("serial")
public class RentManager extends JFrame {

    public static final String version = "1.4.1";
    public static RentManager rm;
    private Class<?> cls;
    private RentHistory rentHistory;
    private RentData rentData;
    public static Tenant nullTenant;
    private TreePanel treePanel;
    private TablePanel tablePanel;
    private InfoPanel infoPanel;
    private RentManagerMenu rentMenu;
    public ImageIcon buildingIcon;
    public ImageIcon roomIcon;
    public ImageIcon tenantIcon;
    public ImageIcon buildingSmallIcon;
    public ImageIcon roomSmallIcon;
    public ImageIcon tenantSmallIcon;
    public ImageIcon buildingEmptyIcon;
    public ImageIcon floorIcon;
    public ImageIcon roomEmptyIcon;
    public ImageIcon searchIcon;
    public ImageIcon addIcon;
    public ImageIcon removeIcon;
    private boolean isGlobalEditable;
    private String path;
    private Locale locale;
    private ResourceBundle rentBundle;
    private boolean hasModified;

    RentManager() {
	rm = this;
	ToolTipManager.sharedInstance().setReshowDelay(0);
	cls = getClass();
	buildingIcon = new ImageIcon(cls.getResource("/images/building.png"));
	roomIcon = new ImageIcon(cls.getResource("/images/room.png"));
	tenantIcon = new ImageIcon(cls.getResource("/images/tenant.png"));
	buildingSmallIcon = new ImageIcon(
		cls.getResource("/images/building_small.png"));
	roomSmallIcon = new ImageIcon(cls.getResource("/images/room_small.png"));
	tenantSmallIcon = new ImageIcon(
		cls.getResource("/images/tenant_small.png"));
	buildingEmptyIcon = new ImageIcon(
		cls.getResource("/images/building_empty.png"));
	roomEmptyIcon = new ImageIcon(cls.getResource("/images/room_empty.png"));
	floorIcon = new ImageIcon(cls.getResource("/images/floor.png"));
	searchIcon = new ImageIcon(cls.getResource("/images/search.png"));
	addIcon = new ImageIcon(cls.getResource("/images/add.png"));
	removeIcon = new ImageIcon(cls.getResource("/images/remove.png"));
	try {
	    path = new File(RentManager.class.getProtectionDomain()
		    .getCodeSource().getLocation().toURI()).getParentFile()
		    .getAbsolutePath();
	} catch (URISyntaxException e1) {
	    e1.printStackTrace();
	}

	File history = new File(path + "/data/rent_history.ser");
	DefaultMutableTreeNode root = new DefaultMutableTreeNode();
	rentHistory = new RentHistory();
	if (!history.exists()) {
	    rentData = new RentData(root);
	    File dir = new File(path + "/data");
	    if (!dir.exists())
		dir.mkdir();
	} else {
	    try {
		InputStream file = new FileInputStream(history);
		InputStream buffer = new BufferedInputStream(file);
		ObjectInput input = new ObjectInputStream(buffer);
		rentHistory = (RentHistory) input.readObject();
		if (!rentHistory.isDataComplete()) {
		    JOptionPane.showMessageDialog(this,
			    getString("message.miss.history"));
		    System.exit(1);
		}
		rentData = rentHistory.getLatestRentData();
		if (rentData == null)
		    rentData = new RentData(root);
		input.close();
	    } catch (IOException e) {
		e.printStackTrace();
		JOptionPane.showMessageDialog(this,
			getString("exception.io.init"));
		rentData = new RentData(root);
	    } catch (ClassNotFoundException e) {
		e.printStackTrace();
		JOptionPane.showMessageDialog(this,
			getString("exception.class.not.found.init"));
		rentData = new RentData(root);
	    }
	}
	isGlobalEditable = true;
	locale = Locale.getDefault();
	reset();
	this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
	this.addWindowListener(new WindowAdapter() {
	    public void windowClosing(WindowEvent we) {
		if (hasModified) {
		    int result = JOptionPane
			    .showConfirmDialog(RentManager.this,
				    getString("message.save.changes"));
		    if (result == JOptionPane.CANCEL_OPTION)
			return;
		    else if (result == JOptionPane.OK_OPTION) {
			save();
		    }
		}
		System.gc();
		dispose();
		System.exit(0);
	    }
	});
	hasModified = false;
	this.setLocationRelativeTo(null);
	this.setVisible(true);
    }

    public void loadBundle(Locale locale) {
	this.locale = locale;
	rentBundle = ResourceBundle.getBundle("resource/RentBundle",
		this.locale, new ResourceBundle.Control() {
		    public ResourceBundle newBundle(String baseName,
			    Locale locale, String format, ClassLoader loader,
			    boolean reload) throws IllegalAccessException,
			    InstantiationException, IOException {
			String bundleName = toBundleName(baseName, locale);
			String resourceName = toResourceName(bundleName,
				"properties");
			ResourceBundle bundle = null;
			InputStream stream = null;
			if (reload) {
			    URL url = loader.getResource(resourceName);
			    if (url != null) {
				URLConnection connection = url.openConnection();
				if (connection != null) {
				    connection.setUseCaches(false);
				    stream = connection.getInputStream();
				}
			    }
			} else {
			    stream = loader.getResourceAsStream(resourceName);
			}
			if (stream != null) {
			    try {
				if (!locale.equals(Locale.US)) {
				    bundle = new PropertyResourceBundle(
					    new InputStreamReader(stream,
						    "UTF-8"));
				} else {
				    bundle = new PropertyResourceBundle(
					    new InputStreamReader(stream));
				}
			    } finally {
				stream.close();
			    }
			}
			return bundle;
		    }
		});
    }

    public void reset() {
	reset(locale);
    }

    public void reset(Locale locale) {
	loadBundle(locale);
	this.getContentPane().removeAll();
	rentData.getRoot().setUserObject(getString("root"));
	nullTenant = new Tenant(getString("default.none"), "", "", "", false,
		null);
	setLayout(new GridBagLayout());
	treePanel = new TreePanel();
	GridBagConstraints c = new GridBagConstraints();
	c.gridx = 0;
	c.gridy = 0;
	c.gridheight = 2;
	c.weightx = 0.2;
	c.weighty = 1;
	c.fill = GridBagConstraints.BOTH;
	this.add(treePanel, c);

	tablePanel = new TablePanel();
	c = new GridBagConstraints();
	c.gridx = 1;
	c.gridy = 0;
	c.weightx = 0.8;
	c.weighty = 1;
	c.fill = GridBagConstraints.BOTH;
	this.add(tablePanel, c);

	infoPanel = new InfoPanel();
	c = new GridBagConstraints();
	c.gridx = 1;
	c.gridy = 1;
	c.weightx = 0.8;
	c.weighty = 0;
	c.fill = GridBagConstraints.BOTH;
	this.add(infoPanel, c);

	rentMenu = new RentManagerMenu();
	this.setJMenuBar(rentMenu);
	String title = getString("title") + " - [" + rentData.getDataMonth()
		+ "]";
	if (!isGlobalEditable)
	    title += " - " + getString("not.editable");
	this.setTitle(title);
	this.setResizable(true);
	this.setMinimumSize(new Dimension(683, 450));
	this.setPreferredSize(new Dimension(1000, 750));
	this.pack();
    }

    public String getString(String key) {
	return rentBundle.getString(key);
    }

    class RentManagerMenu extends JMenuBar implements ActionListener {

	private JMenu m_file = new JMenu(getString("menu.file"));
	private JMenu m_language = new JMenu(getString("menu.lang"));
	private JMenu m_help = new JMenu(getString("menu.help"));
	private JMenuItem mi_new = new JMenuItem(getString("menu.new.bill"));
	private JMenuItem mi_open = new JMenuItem(getString("menu.open.file"));
	private JMenuItem mi_save = new JMenuItem(getString("menu.save"));
	private JMenuItem mi_saveAsNew = new JMenuItem(
		getString("menu.save.as.new"));
	private JMenuItem mi_export = new JMenuItem(getString("menu.export"));
	private JMenuItem mi_delete = new JMenuItem(
		getString("menu.delete.current.bill"));
	private JMenuItem mi_exit = new JMenuItem(getString("menu.exit"));
	private JMenuItem mi_help = new JMenuItem(getString("menu.help"));
	private JMenuItem mi_about = new JMenuItem(getString("menu.about"));
	private JMenuItem mi_updateHistory = new JMenuItem(
		getString("menu.update.history"));
	private JMenuItem mi_chinese = new JMenuItem(getString("menu.lang.chs"));
	private JMenuItem mi_english = new JMenuItem(getString("menu.lang.eng"));
	private JFileChooser fc;

	public RentManagerMenu() {
	    m_file.setMnemonic(KeyEvent.VK_F);
	    m_language.setMnemonic(KeyEvent.VK_L);
	    m_help.setMnemonic(KeyEvent.VK_H);
	    
	    mi_new.addActionListener(this);
	    mi_open.addActionListener(this);
	    mi_save.addActionListener(this);
	    mi_saveAsNew.addActionListener(this);
	    mi_export.addActionListener(this);
	    mi_exit.addActionListener(this);
	    mi_help.addActionListener(this);
	    mi_about.addActionListener(this);
	    mi_updateHistory.addActionListener(this);
	    mi_chinese.addActionListener(this);
	    mi_english.addActionListener(this);
	    mi_delete.addActionListener(this);
	    
	    mi_new.setMnemonic(KeyEvent.VK_N);
	    mi_open.setMnemonic(KeyEvent.VK_O);
	    mi_save.setMnemonic(KeyEvent.VK_S);
	    mi_saveAsNew.setMnemonic(KeyEvent.VK_A);
	    mi_export.setMnemonic(KeyEvent.VK_E);
	    mi_exit.setMnemonic(KeyEvent.VK_X);
	    mi_help.setMnemonic(KeyEvent.VK_E);
	    mi_about.setMnemonic(KeyEvent.VK_A);
	    mi_updateHistory.setMnemonic(KeyEvent.VK_U);
	    mi_chinese.setMnemonic(KeyEvent.VK_C);
	    mi_english.setMnemonic(KeyEvent.VK_E);
	    mi_delete.setMnemonic(KeyEvent.VK_D);

	    m_file.add(mi_new);
	    m_file.add(mi_open);
	    m_file.addSeparator();
	    m_file.add(mi_save);
	    m_file.add(mi_saveAsNew);
	    m_file.add(mi_export);
	    m_file.addSeparator();
	    m_file.add(mi_delete);
	    m_file.addSeparator();
	    m_file.add(mi_exit);
	    m_language.add(mi_chinese);
	    m_language.add(mi_english);
	    m_help.add(mi_help);
	    m_help.add(mi_about);
	    m_help.add(mi_updateHistory);
	    add(m_file);
	    add(m_language);
	    add(m_help);
	    fc = new JFileChooser();
	    fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
	    fc.setMultiSelectionEnabled(false);
	    fc.setFileFilter(new FileFilter() {
		@Override
		public boolean accept(File f) {
		    if (f.isDirectory())
			return true;
		    String name = f.getName();
		    if (!name.endsWith(".ser"))
			return false;
		    if (name.indexOf("[") != 0)
			return false;
		    if (name.indexOf("-") != 3)
			return false;
		    if (name.indexOf("]") != 8)
			return false;
		    if (name.indexOf("_") != 9)
			return false;
		    try {
			Integer.parseInt(name.substring(1, 3));
			Integer.parseInt(name.substring(4, 8));
			Integer.parseInt(name.substring(10, name.indexOf(".")));
		    } catch (NumberFormatException ext) {
			return false;
		    }
		    return true;
		}

		@Override
		public String getDescription() {
		    return getString("file.filter.description");
		}

	    });

	    if (rentHistory.getRentDataNames().size() == 0)
		mi_delete.setEnabled(false);

	    if (rentHistory.isInNewMonth(rentData))
		mi_saveAsNew.setEnabled(false);

	    if (!isGlobalEditable) {
		mi_new.setEnabled(false);
		mi_save.setEnabled(false);
		mi_saveAsNew.setEnabled(false);
		mi_delete.setEnabled(false);
	    }
	}

	@SuppressWarnings("unchecked")
	@Override
	public void actionPerformed(ActionEvent e) {
	    Object source = e.getSource();
	    if (source.equals(mi_new)) {
		JLabel message = new JLabel("<html><p style='width:200px'>"
			+ getString("message.new.bill") + "</html>");
		int result = JOptionPane.showConfirmDialog(RentManager.this,
			message, getString("title.message"),
			JOptionPane.OK_CANCEL_OPTION);
		if (result == JOptionPane.OK_OPTION) {
		    File save = null;
		    boolean isInNewMonth = rentHistory.isInNewMonth(rentData);
		    if (isInNewMonth) {
			save = new File(path + "/data/"
				+ rentData.getFilename());
			rentHistory.getRentDataNames().add(
				rentData.getFilename());
		    } else {
			String name = rentHistory.getRentDataNames().getLast();
			save = new File(path + "/data/" + name);
		    }
		    try {
			OutputStream file = new FileOutputStream(save);
			OutputStream buffer = new BufferedOutputStream(file);
			ObjectOutputStream out = new ObjectOutputStream(buffer);
			out.writeObject(rentData);
			out.flush();
			if (isInNewMonth) {
			    file = new FileOutputStream(path
				    + "/data/rent_history.ser");
			    buffer = new BufferedOutputStream(file);
			    out = new ObjectOutputStream(buffer);
			    out.writeObject(rentHistory);
			}
			out.close();
			buffer.close();
			file.close();
		    } catch (FileNotFoundException ext) {
			ext.printStackTrace();
		    } catch (IOException ext) {
			ext.printStackTrace();
		    }

		    Enumeration<Building> bldgs = getRoot().children();
		    while (bldgs.hasMoreElements()) {
			Building bldg = bldgs.nextElement();
			Enumeration<Building.Story> stories = bldg.children();
			while (stories.hasMoreElements()) {
			    Building.Story story = stories.nextElement();
			    Enumeration<Room> rooms = story.children();
			    while (rooms.hasMoreElements()) {
				Room room = rooms.nextElement();
				room.setWaterRecord(room.getWaterRecord()
					+ room.getWaterUsed());
				room.setWaterUsed(0);
				room.setElectricityRecord(room
					.getElectricityRecord()
					+ room.getElectricityUsed());
				room.setElectricityUsed(0);
				room.setInitializing(false);
				Enumeration<Tenant> tenants = room.children();
				while (tenants.hasMoreElements())
				    tenants.nextElement()
					    .setInitializing(false);
			    }
			}
		    }
		    rentData.nextMonth();
		    rentData.setVersion(0);
		    setTitle(getString("title") + " - ["
			    + rentData.getDataMonth() + "]");
		    tablePanel.refresh();
		    setHasModified(true);
		    if (rentHistory.getRentDataNames().size() != 0)
			mi_delete.setEnabled(true);
		    mi_saveAsNew.setEnabled(false);
		}
	    } else if (source.equals(mi_open)) {
		if (hasModified) {
		    int result = JOptionPane
			    .showConfirmDialog(RentManager.this,
				    getString("message.save.changes"));
		    if (result == JOptionPane.CANCEL_OPTION)
			return;
		    else if (result == JOptionPane.OK_OPTION) {
			save();
		    }
		}
		fc.setCurrentDirectory(new File(path + "/data"));
		if (fc.showOpenDialog(RentManager.this) == JFileChooser.APPROVE_OPTION) {
		    File selected = fc.getSelectedFile();
		    RentData newData = null;
		    try {
			InputStream file = new FileInputStream(selected);
			InputStream buffer = new BufferedInputStream(file);
			ObjectInput input = new ObjectInputStream(buffer);
			newData = (RentData) input.readObject();
			input.close();
		    } catch (IOException ext) {
			ext.printStackTrace();
			JOptionPane.showMessageDialog(RentManager.this,
				getString("exception.io"));
			return;
		    } catch (ClassNotFoundException ext) {
			ext.printStackTrace();
			JOptionPane.showMessageDialog(RentManager.this,
				getString("exception.class.not.found"));
			return;
		    }
		    if (newData.getFilename().equals(
			    rentHistory.getRentDataNames().getLast())) {
			isGlobalEditable = true;
		    } else {
			isGlobalEditable = false;
		    }
		    rentData = newData;
		    setHasModified(false);
		    reset();
		}
	    } else if (source.equals(mi_save)) {
		save();
		if (rentHistory.getRentDataNames().size() != 0)
		    mi_delete.setEnabled(true);
		mi_saveAsNew.setEnabled(true);
	    } else if (source.equals(mi_saveAsNew)) {
		rentData.increaseVersion();
		String name = path + "/data/" + rentData.getFilename();
		File save = new File(name);
		rentHistory.getRentDataNames().add(rentData.getFilename());
		try {
		    OutputStream file = new FileOutputStream(save);
		    OutputStream buffer = new BufferedOutputStream(file);
		    ObjectOutputStream out = new ObjectOutputStream(buffer);
		    out.writeObject(rentData);
		    out.flush();
		    file = new FileOutputStream(path + "/data/rent_history.ser");
		    buffer = new BufferedOutputStream(file);
		    out = new ObjectOutputStream(buffer);
		    out.writeObject(rentHistory);
		    out.close();
		    setHasModified(false);
		    JOptionPane.showMessageDialog(RentManager.this,
			    getString("message.save.success"));
		} catch (IOException ext) {
		    ext.printStackTrace();
		    JOptionPane.showMessageDialog(RentManager.this,
			    getString("exception.io"));
		    return;
		}
		if (rentHistory.getRentDataNames().size() != 0)
		    mi_delete.setEnabled(true);
	    }
	    if (source.equals(mi_export)) {
		JFileChooser exporter = new JFileChooser();
		exporter.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		exporter.setMultiSelectionEnabled(false);
		exporter.setCurrentDirectory(new File(path));
		int result = exporter.showSaveDialog(RentManager.this);
		if (result == JFileChooser.APPROVE_OPTION) {
		    export(exporter.getSelectedFile());
		}
	    } else if (source.equals(mi_exit)) {
		if (hasModified) {
		    int result = JOptionPane
			    .showConfirmDialog(RentManager.this,
				    getString("message.save.changes"));
		    if (result == JOptionPane.CANCEL_OPTION)
			return;
		    else if (result == JOptionPane.OK_OPTION) {
			save();
		    }
		}
		RentManager.this.dispose();
	    } else if (source.equals(mi_chinese)) {
		reset(Locale.SIMPLIFIED_CHINESE);
	    } else if (source.equals(mi_english)) {
		reset(Locale.US);
	    } else if (source.equals(mi_help)) {

	    } else if (source.equals(mi_about)) {
		String message = MessageFormat.format(
			getString("message.credits"), version);
		JOptionPane.showMessageDialog(RentManager.this, message);
	    } else if (source.equals(mi_updateHistory)) {
		JTextArea ta_updateHistory = new JTextArea();
		String updateHistory = null;
		BufferedReader br = null;
		try {
		    br = new BufferedReader(new InputStreamReader(
			    cls.getResourceAsStream("/history.txt")));
		    StringBuilder sb = new StringBuilder();
		    String line = br.readLine();
		    while (line != null) {
			sb.append(line);
			sb.append(System.lineSeparator());
			line = br.readLine();
		    }
		    updateHistory = sb.toString();
		    br.close();
		} catch (IOException ext) {
		    String message = getString("message.fail.update.history");
		    JOptionPane.showMessageDialog(RentManager.this, message);
		    try {
			if (br != null)
			    br.close();
		    } catch (IOException e1) {
			e1.printStackTrace();
		    }
		    return;
		}
		ta_updateHistory.setText(updateHistory);
		ta_updateHistory.setTabSize(2);
		ta_updateHistory.setWrapStyleWord(true);
		ta_updateHistory.setLineWrap(true);
		ta_updateHistory.setEditable(false);
		ta_updateHistory.setCaretPosition(0);
		JScrollPane sp_updateHistory = new JScrollPane(ta_updateHistory);
		sp_updateHistory.setPreferredSize(new Dimension(450, 600));
		JOptionPane.showMessageDialog(RentManager.this,
			sp_updateHistory, "Update History",
			JOptionPane.PLAIN_MESSAGE);
	    } else if (source.equals(mi_delete)) {
		JLabel message = new JLabel("<html><p style='width:200px'>"
			+ MessageFormat.format(
				getString("message.delete.current.bill"),
				rentData.getFilename(), "<br>")
			+ "</html>");
		int result = JOptionPane.showConfirmDialog(RentManager.this,
			message, "Confirm: Delete Current Bill",
			JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
		if (result == JOptionPane.YES_OPTION) {
		    message.setText("<html><p style='width:200px'>"
			    + getString("message.delete.reconfirm") + "</html>");
		    int reconfirm = JOptionPane.showConfirmDialog(
			    RentManager.this, message, "Reconfirm",
			    JOptionPane.YES_NO_OPTION,
			    JOptionPane.WARNING_MESSAGE);
		    if (reconfirm == JOptionPane.YES_OPTION) {
			if (rentHistory.isInNewMonth(rentData)) {
			    try {
				rentData = rentHistory.getLatestRentData();
			    } catch (ClassNotFoundException | IOException e1) {
				e1.printStackTrace();
			    }
			    hasModified = false;
			    reset();
			    return;
			}
			File toDelete = new File(path + "/data/"
				+ rentHistory.getRentDataNames().getLast());
			System.gc();
			try {
			    Files.delete(toDelete.toPath());
			} catch (IOException e2) {
			    e2.printStackTrace();
			}
			rentHistory.getRentDataNames().removeLast();
			try {
			    rentData = rentHistory.getLatestRentData();
			    OutputStream file = new FileOutputStream(path
				    + "/data/rent_history.ser");
			    OutputStream buffer = new BufferedOutputStream(file);
			    ObjectOutputStream out = new ObjectOutputStream(
				    buffer);
			    out.writeObject(rentHistory);
			    out.close();
			    buffer.close();
			    file.close();
			} catch (ClassNotFoundException | IOException e1) {
			    e1.printStackTrace();
			}
			if (rentData == null) {
			    DefaultMutableTreeNode root = new DefaultMutableTreeNode();
			    rentData = new RentData(root);
			}
			reset();
		    }
		}
	    }
	}
    }

    public void save() {
	String name = path + "/data/" + rentData.getFilename();
	File save = new File(name);
	boolean isInNewMonth = rentHistory.isInNewMonth(rentData);
	if (isInNewMonth)
	    rentHistory.getRentDataNames().add(rentData.getFilename());
	try {
	    OutputStream file = new FileOutputStream(save);
	    OutputStream buffer = new BufferedOutputStream(file);
	    ObjectOutputStream out = new ObjectOutputStream(buffer);
	    out.writeObject(rentData);
	    out.flush();
	    if (isInNewMonth) {
		file = new FileOutputStream(path + "/data/rent_history.ser");
		buffer = new BufferedOutputStream(file);
		out = new ObjectOutputStream(buffer);
		out.writeObject(rentHistory);
	    }
	    out.close();
	    setHasModified(false);
	    JOptionPane.showMessageDialog(RentManager.this,
		    getString("message.save.success"));
	} catch (IOException ext) {
	    ext.printStackTrace();
	    JOptionPane.showMessageDialog(RentManager.this,
		    getString("exception.io"));
	    return;
	}
    }

    public boolean export(File path) {
	File export = new File(path.getAbsolutePath() + "/"
		+ rentData.getTextFilename());
	String month = rentData.getDataMonth();
	try {
	    PrintStream ps = new PrintStream(export);
	    int childCount = getRoot().getChildCount();
	    for (int i = 0; i < childCount; i++) {
		BillTableModel btm = tablePanel.getTable(i).getModel();
		Building bldg = (Building) getRoot().getChildAt(i);
		String buildingAdd = bldg.getAddress();
		double waterPrice = bldg.getWaterPrice();
		double electPrice = bldg.getElectricityPrice();
		String cleanPrice = bldg.getCleaningPrice() + "";
		String internetPrice = bldg.getInternetPrice() + "";
		String acPrice = bldg.getAcPrice() + "";
		int rowCount = btm.getRowCount();
		for (int r = 0; r < rowCount; r++) {
		    Object[] row = btm.getRow(r);
		    if (row[BillTableModel.TENANT].equals(nullTenant))
			continue;
		    String tenant = row[BillTableModel.TENANT].toString();
		    Room room = (Room) row[BillTableModel.ROOM];
		    String roomNum = room.getRoomNumber() + "";
		    String rent = room.getRent() + "";
		    String lastWater = room.getWaterRecord() + "";
		    String currWater = row[BillTableModel.WATER].toString();
		    String usedWater = room.getWaterUsed() + "";
		    String water = (room.getWaterUsed() * waterPrice) + "";
		    String lastElect = room.getElectricityRecord() + "";
		    String currElect = row[BillTableModel.ELECTRICITY]
			    .toString();
		    String usedElect = room.getElectricityUsed() + "";
		    String elect = (room.getElectricityUsed() * electPrice)
			    + "";
		    String internet = (room.isUseInternet()) ? internetPrice
			    : "0";
		    String ac = (room.isUseAC()) ? acPrice : "0";
		    String otherFee = row[BillTableModel.OTHER_FEE].toString();
		    String total = row[BillTableModel.TOTAL].toString();
		    String segment = MessageFormat.format(
			    getString("export.text"), System.lineSeparator(),
			    month, tenant, buildingAdd, roomNum, rent,
			    lastWater, currWater, usedWater, waterPrice, water,
			    lastElect, currElect, usedElect, electPrice, elect,
			    cleanPrice, internet, ac, otherFee, total);
		    ps.println(segment);
		}
		ps.close();
	    }
	} catch (IOException ext) {
	    JOptionPane.showMessageDialog(this,
		    getString("message.cannot.export"));
	    return false;
	}
	return true;
    }

    public boolean isGlobalEditable() {
	return isGlobalEditable;
    }

    public boolean isHasModified() {
	return hasModified;
    }

    public void setHasModified(boolean hasModified) {
	this.hasModified = hasModified;
    }

    public DefaultMutableTreeNode getRoot() {
	return rentData.getRoot();
    }

    public TreePanel getTreePanel() {
	return treePanel;
    }

    public TablePanel getTablePanel() {
	return tablePanel;
    }

    public InfoPanel getInfoPanel() {
	return infoPanel;
    }
    
    public Locale getLocale() {
	return locale;
    }

    @SuppressWarnings("unchecked")
    public static int getNumElements(int depth, MutableTreeNode node) {
	if (depth == 0)
	    return 1;
	if (!node.getAllowsChildren())
	    return 0;
	Enumeration<MutableTreeNode> children = node.children();
	int count = 0;
	while (children.hasMoreElements())
	    count += getNumElements(depth - 1, children.nextElement());
	return count;
    }

    public static void main(String[] args) {
	SwingUtilities.invokeLater(new Runnable() {
	    public void run() {
		new RentManager();
	    }
	});
    }

}