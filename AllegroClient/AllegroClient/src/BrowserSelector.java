import java.awt.Container;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class BrowserSelector {
	private JComboBox<String> box;
	private JLabel label;

	public BrowserSelector(int x, int y, int width, int height) {
		String[] browsers = { "Standard", "Firefox", "Chrome",
				"Internet Explorer" };
		box = new JComboBox<>(browsers);
		box.setSize(width, height);
		box.setLocation(x, y);

		label = new JLabel("Browser:");
		label.setSize(60, 30);
		label.setLocation(x-60, 0);
	}

	public void addTo(Container comp) {
		comp.add(label);
		comp.add(box);
	}

	public boolean isDefault() {
		return box.getSelectedIndex() == 0;
	}

	public String getStartCommand() {
		if (box.getSelectedIndex() == 1) {
			return "cmd.exe /c start firefox ";
		} else if (box.getSelectedIndex() == 2) {
			return "cmd.exe /c start chrome ";
		} else if (box.getSelectedIndex() == 3) {
			return "cmd.exe /c start iexplore ";
		} else {
			return null;
		}
	}

}
