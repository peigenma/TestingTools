import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/*
 * dialog to change login data
 */

public class SettingsFrame extends JFrame implements ActionListener,
		KeyListener {
	JLabel puttyUsername, puttyPassword;
	JTextField puttyUsernameField;
	JPasswordField puttyPasswordField;
	JButton save;
	DataFile props;

	public SettingsFrame(DataFile props) {
		this.props = props;

		setLayout(new FlowLayout());

		setLocation(800, 400);
		setSize(220, 170);
		setTitle("Login settings");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setResizable(false);

		puttyUsername = new JLabel("Putty username");
		puttyPassword = new JLabel("Putty password");

		puttyUsernameField = new JTextField(props.getData("puttyUsername"), 15);
		puttyUsernameField.addKeyListener(this);
		puttyPasswordField = new JPasswordField(props.getData("puttyPassword"),
				15);
		puttyPasswordField.addKeyListener(this);

		save = new JButton("Save");
		save.addActionListener(this);

		add(puttyUsername);
		add(puttyUsernameField);
		add(puttyPassword);
		add(puttyPasswordField);
		add(save);

		setVisible(true);
	}

	/*
	 * saves data and closes the dialog
	 */
	public void save() {
		props.setData("puttyUsername", puttyUsernameField.getText());
		props.setData("puttyPassword",
				new String(puttyPasswordField.getPassword()));
		props.save();

		dispose();
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == save) {
			save();
		}
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		if (arg0.getKeyCode() == KeyEvent.VK_ENTER) {
			save();
		}
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}
}
