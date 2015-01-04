package rent;

import java.awt.Rectangle;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;

public interface Popuppable {
    public JComponent getComponent();
    public String getPopupText(MouseEvent event);    
    public Rectangle getActivatedRegion(MouseEvent event);
    public void feedBack(Rectangle rect, Object feedBack);
}
