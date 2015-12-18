import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;


public class ServerCreator extends JDialog {
	
	private JTextField beField1, beField2, feField1, feField2;
	private ServerManager sManager;

	public ServerCreator(Dialog owner, boolean modal, ServerManager serverManager) {
		super(owner, modal);
		this.sManager = serverManager;
		setLayout(new FlowLayout());
		setSize(250,250);
		setLocation(700,500);
		setResizable(false);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setTitle("Create server");
		
		
		JLabel beLabel1, beLabel2, feLabel1, feLabel2;
		beLabel1 = new JLabel("BE");
		beLabel2 = new JLabel("BE-Fallback");
		feLabel1 = new JLabel("FE");
		feLabel2 = new JLabel("FE-Fallback");
		
		beField1 = new JTextField(20);
		feField1 = new JTextField(20);
		beField2 = new JTextField(20);
		feField2 = new JTextField(20);
		
		JButton createButton = new JButton("Create");
		final JDialog parent = this;
		createButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String be1, be2, fe1, fe2;
				be1 = beField1.getText();
				be2 = beField2.getText();
				fe1 = feField1.getText();
				fe2 = feField2.getText();
				
				if( !be1.equals("") && !be2.equals("") && !fe1.equals("") && !fe2.equals("")) {
					sManager.addServer(be1, fe1, be2, fe2);
					dispose();
				} else {
					JOptionPane.showMessageDialog(parent, "Fill in all textfields!");
				}
			}
		});
		
		add(beLabel1);
		add(beField1);
		add(feLabel1);
		add(feField1);
		add(beLabel2);
		add(beField2);
		add(feLabel2);
		add(feField2);
		add(createButton);
		
		setVisible(true);
	}
	
}
