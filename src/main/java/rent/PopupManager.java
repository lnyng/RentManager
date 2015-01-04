package rent;

import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

import javax.swing.Timer;

public class PopupManager extends MouseAdapter {

    private static PopupManager manager;
    private Timer popupTimer;
    private String popupText;
    private Popuppable insidePopuppable;
    private Rectangle insideRectangle;
    private MouseEvent mouseEvent;

    private PopupFrame popup;

    boolean enabled = true;
    private boolean popupped = false;

    private WindowFocusListener focusListener = null;
    private AWTEventListener popupListener = null;

    PopupManager() {
	popupTimer = new Timer(750, new ActionListener() {
	    
	    public void actionPerformed(ActionEvent e) {
		insideRectangle = insidePopuppable
			.getActivatedRegion(mouseEvent);
		popupText = insidePopuppable.getPopupText(mouseEvent);
		if (insideRectangle != null)
		    showPopup();
	    }
	});
	popupTimer.setRepeats(false);
	focusListener = new WindowFocusListener() {
	    
	    public void windowGainedFocus(WindowEvent e) {
		unregisterComponent(insidePopuppable);
		Toolkit.getDefaultToolkit().removeAWTEventListener(
			popupListener);
	    }

	    
	    public void windowLostFocus(WindowEvent e) {
		String newText = popup.getTextArea().getText();
		insidePopuppable.feedBack(insideRectangle, newText);
		registerComponent(insidePopuppable);
		Toolkit.getDefaultToolkit().addAWTEventListener(popupListener,
			AWTEvent.MOUSE_EVENT_MASK);
		hidePopup();
	    }
	};
	popupListener = new AWTEventListener() {
	    private boolean isExitFromInside = false;

	    
	    public void eventDispatched(AWTEvent event) {
		if (event instanceof MouseEvent) {
		    MouseEvent mouseEvent = (MouseEvent) event;
		    if (mouseEvent.getID() == MouseEvent.MOUSE_EXITED) {
			Component source = mouseEvent.getComponent();
			isExitFromInside = popup.isAncestorOf(source);
		    } else if (mouseEvent.getID() == MouseEvent.MOUSE_ENTERED) {
			Component source = mouseEvent.getComponent();
			if (isExitFromInside && !popup.isAncestorOf(source)) {
			    Point p = mouseEvent.getLocationOnScreen();
			    Point compLoc = insidePopuppable.getComponent()
				    .getLocationOnScreen();
			    p.translate(-compLoc.x, -compLoc.y);
			    if (!insideRectangle.contains(p)) {
				hidePopup();
			    }
			}
		    }
		}
	    }
	};
    }

    public void setEnabled(boolean flag) {
	enabled = flag;
	if (!flag) {
	    hidePopup();
	}
    }

    public boolean isEnabled() {
	return enabled;
    }

    void showPopup() {
	if (insidePopuppable == null
		|| !insidePopuppable.getComponent().isShowing())
	    return;
	if (enabled) {
	    if (popup == null) {
		popup = PopupFrame.sharedInstance("", new Point(0, 0));
		Toolkit.getDefaultToolkit().addAWTEventListener(popupListener,
			AWTEvent.MOUSE_EVENT_MASK);
		popup.addWindowFocusListener(focusListener);
	    }
	    popup.hidePopup();
	    Point loc = insidePopuppable.getComponent().getLocationOnScreen();
	    loc.translate(insideRectangle.x, insideRectangle.y);
	    loc.translate(0, insideRectangle.height);
	    popup = PopupFrame.sharedInstance(popupText, loc);
	    popup.setVisible(true);
	    popupped = true;
	}
    }

    void hidePopup() {
	if (popup != null && popupped) {
	    popup.hidePopup();
	    popupped = false;
	}
    }

    public static PopupManager sharedInstance() {
	if (manager == null)
	    manager = new PopupManager();
	return manager;
    }

    public void registerComponent(Popuppable popuppable) {
	popuppable.getComponent().removeMouseListener(this);
	popuppable.getComponent().addMouseListener(this);
	popuppable.getComponent().removeMouseMotionListener(this);
	popuppable.getComponent().addMouseMotionListener(this);
    }

    public void unregisterComponent(Popuppable popuppable) {
	popuppable.getComponent().removeMouseListener(this);
	popuppable.getComponent().removeMouseMotionListener(this);
    }

    public void mouseEntered(MouseEvent event) {
	if (popupped && insideRectangle != null
		&& insideRectangle.contains(event.getPoint()))
	    return;
	else if (popupped) {
	    hidePopup();
	}
	initiatePopup(event);
    }

    private void initiatePopup(MouseEvent event) {
	Popuppable popuppable = (Popuppable) event.getSource();
	Component component = popuppable.getComponent();
	Point location = event.getPoint();
	// ensure tooltip shows only in proper place
	if (location.x < 0 || location.x >= component.getWidth()
		|| location.y < 0 || location.y >= component.getHeight()) {
	    return;
	}
	if (insidePopuppable != null) {
	    popupTimer.stop();
	}
	insidePopuppable = popuppable;
	mouseEvent = event;
	popupTimer.restart();
    }

    public void mouseExited(MouseEvent event) {
	if (popupped) {
	    Point p = event.getLocationOnScreen();
	    Point loc = popup.getLocationOnScreen();
	    p.translate(-loc.x, -loc.y);
	    if (popup.contains(p)) {
		return;
	    }
	}
	hidePopup();
	popupTimer.stop();
	insidePopuppable = null;
	mouseEvent = null;
    }

    public void mousePressed(MouseEvent event) {
	hidePopup();
	popupTimer.stop();
	insidePopuppable = null;
	mouseEvent = null;
    }

    public void mouseMoved(MouseEvent event) {
	if (popupped) {
	    checkForPopupChange(event);
	} else {
	    insidePopuppable = (Popuppable) event.getSource();
	    mouseEvent = event;
	    popupTimer.restart();
	}
    }

    private void checkForPopupChange(MouseEvent event) {
	if (insideRectangle.contains(event.getPoint()))
	    return;
	else {
	    Point p = event.getLocationOnScreen();
	    Point loc = popup.getLocationOnScreen();
	    p.translate(-loc.x, -loc.y);
	    if (popup.contains(p))
		return;
	}
	hidePopup();
	insidePopuppable = (Popuppable) event.getSource();
	mouseEvent = event;
	popupTimer.stop();
    }
}
