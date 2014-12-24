package rent;

import java.awt.Point;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

@SuppressWarnings("serial")
public class PopupFrame extends JFrame {

    private static PopupFrame frame;
    private JTextArea ta_popup;

    PopupFrame() {
	ta_popup = new JTextArea(5, 15);
	ta_popup.setLineWrap(true);
	ta_popup.setWrapStyleWord(true);
	this.getContentPane().add(new JScrollPane(ta_popup));
	this.setUndecorated(true);
	this.setResizable(false);
	this.setAlwaysOnTop(true);
	this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	this.pack();
	this.setVisible(false);
    }

    public static PopupFrame sharedInstance(String text, Point loc) {
	if (frame == null)
	    frame = new PopupFrame();
	frame.getTextArea().setText(text);
	frame.setLocation(loc);
	return frame;
    }

    public void setVisible(boolean flag) {
	setFocusableWindowState(false);
	super.setVisible(flag);
	setFocusableWindowState(true);
    }

    public void hidePopup() {
	setVisible(false);
	dispose();
    }

    public JTextArea getTextArea() {
	return ta_popup;
    }
}
