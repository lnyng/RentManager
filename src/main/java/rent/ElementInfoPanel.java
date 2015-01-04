package rent;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public abstract class ElementInfoPanel extends JPanel {
    public abstract void updateInfo(Object element);

    public abstract void updateInfo();
}