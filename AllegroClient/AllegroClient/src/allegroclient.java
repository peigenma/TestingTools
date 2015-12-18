import java.awt.Color;
import java.awt.Desktop;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;

/*
 * Copyright (c) 2015 Ergon Informatik AG
 * Kleinstrasse 15, 8008 Zuerich, Switzerland
 * All rights reserved.
 */

public class allegroclient extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	private static ArrayList<JButton> buttonsBE, buttonsFE;
	private JButton status, deployment, settings, twp, smca, alm, serverConfButton;

	private JLabel FE, BE, modus;
	private Font font = new Font(Font.SANS_SERIF, Font.BOLD, 14);

	private JCheckBox httpsBox;
	private JCheckBox devModeBox;

	private static JComboBox<String> box;
	private BrowserSelector browserSelector;
	private DataFile properties;
	private PuttyStarter putty;
	private ServerManager serverManager;

	public void setColor(JButton y) {
		Font f = new Font(Font.SANS_SERIF, Font.BOLD, 14);
		y.setFont(f);
		y.addActionListener(this);
		add(y);
	}

	public void setColor2(JButton y) {
		Font f = new Font(Font.SANS_SERIF, Font.BOLD, 14);
		y.setFont(f);
		y.setForeground(Color.white);
		y.setBackground(Color.black);
		y.addActionListener(this);
		add(y);

	}

	public void openWebsite(String link) {
		try {
			if (browserSelector.isDefault()) {
				Desktop.getDesktop().browse(new URI(link));
			} else {
				Runtime.getRuntime().exec(
				browserSelector.getStartCommand() + link);
			}

		} catch (IOException | URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public allegroclient() {

		setLayout(null);
		setLocation(300, 300);
		setTitle("Allegro Client -- Ergon AG");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);

		browserSelector = new BrowserSelector(500, 0, 150, 30);
		properties = new DataFile();
		serverManager = new ServerManager(properties);

		putty = new PuttyStarter(properties, this);

		buttonsBE = new ArrayList<>();
		buttonsFE = new ArrayList<>();

		String[] Server = { "Standard", "Fallback", "Putty", "Bonita" };

		box = new JComboBox<>(Server);
		box.addActionListener(this);
		box.setSize(150, 30);
		box.setLocation(750, 0);
		add(box);

		ArrayList<String[]> servers = serverManager.getActiveServers();
		for (String[] serverNames : servers) {
			buttonsBE.add(new JButton(serverNames[0]));
			buttonsFE.add(new JButton(serverNames[1]));
		}

		status = new JButton("STATUS");
		deployment = new JButton("DEPLOYMENT");
		settings = new JButton(" Putty Login");
		twp = new JButton("TWP");
		smca = new JButton("SMCA");
		alm = new JButton("ALM");
		
		settings.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
		settings.addActionListener(this);
		add(settings);
		
		serverConfButton = new JButton("Select Servers");
		serverConfButton.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 13));
		final allegroclient parent = this;
		serverConfButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ServerSelector serverSelector = new ServerSelector(parent, true, serverManager);
				serverManager = serverSelector.getServerManager();
				parent.reloadServers();
			}
		});
		add(serverConfButton);

		// Erstellen der TExtfelder
		BE = new JLabel();
		FE = new JLabel();
		modus = new JLabel("Server: ");

		browserSelector.addTo(this);

		httpsBox = new JCheckBox("HTTPS", false);
		httpsBox.setBounds(buttonsBE.size() * 110 - 100, 190, 100, 20);
		add(httpsBox);

		devModeBox = new JCheckBox("Developer-Mode", true);
		devModeBox.setBounds(buttonsBE.size() * 110 - 250, 190, 150, 20);
		add(devModeBox);

		// hinzuf�gen der Labels
		add(modus);
		add(BE);
		add(FE);

		setColor2(status);
		setColor2(deployment);
		setColor2(alm);
		setColor2(twp);
		setColor2(smca);
		
		setServerButtons();

		// Status Deployment
		status.setBounds(50, 185, 90, 30);
		deployment.setBounds(140, 185, 140, 30);
		alm.setBounds(260, 185, 100, 30);
		twp.setBounds(360, 185, 100, 30);
		smca.setBounds(460, 185, 100, 30);
		serverConfButton.setBounds(250, 5, 150, 20);
		settings.setBounds(150, 5, 100, 20);

		// Beschriften der Textfelder
		BE.setBounds(10, 10, 100, 30);
		FE.setBounds(10, 90, 100, 30);
		FE.setText("FE-SERVER");
		FE.setFont(font);
		BE.setText("BE-SERVER");
		BE.setFont(font);

		modus.setBounds(700, 0, 50, 30);

		// ICON
		// Class<?> aClass = new Object().getClass();
		// URL imgUrl = aClass.getResource("/ErgonLogo.png");
		// Image icon = new ImageIcon(imgUrl).getImage();
		// setIconImage(icon);

		pack();
		setVisible(true);
	}

	public static void main(String[] args) {

		try {
			new ServerSocket(7238);
		} catch (IOException e) {

			System.exit(0);

		}
		try {

			allegroclient x = new allegroclient();
			x.setSize(buttonsBE.size() * 110, 250);
			Thread.sleep(100);
		} catch (InterruptedException ie) {
		}

	}

	@Override
	public void actionPerformed(ActionEvent ae) {

		for (int i = 0; i < buttonsBE.size(); i++) {
			if (ae.getSource() == buttonsBE.get(i)) {
				if (box.getSelectedIndex() == 2) {
					putty.startPutty(buttonsBE.get(i).getText());
				} else if (box.getSelectedIndex() == 3) {
					String link = "http://" + buttonsBE.get(i).getText()
							+ ".ergon.ch:7080/bonita-ux/";
					openWebsite(link);
				} else {
					try {
						Process proc = Runtime
								.getRuntime()
								.exec("javaws http://"
										+ buttonsBE.get(i).getText()
										+ ".ergon.ch:8080/scsl/WebStart/client.jnlp?username=allegro_ergon&password=162534&language=german"
										+ (devModeBox.isSelected() ? "&param=-x"
												: ""));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				return;
			}
		}

		for (int i = 0; i < buttonsFE.size(); i++) {
			if (ae.getSource() == buttonsFE.get(i)) {
				if (box.getSelectedIndex() == 2) {
					putty.startPutty(buttonsFE.get(i).getText());
				} else {
					String link = "http" + (httpsBox.isSelected() ? "s" : "")
							+ "://" + buttonsFE.get(i).getText()
							+ ".ergon.ch:8080/ordermgmt";
					openWebsite(link);
				}

				return;
			}
		}

		if (ae.getSource() == status) {
			String link = "http://cui.ergon.ch:2380";
			openWebsite(link);
		} else if (ae.getSource() == deployment) {
			String link = "http://cui.ergon.ch";
			openWebsite(link);
		} else if (ae.getSource() == settings) {
			new SettingsFrame(properties);
		} else if (ae.getSource() == smca) {
			openWebsite("https://smca.swisscom.com/vpn/index.html");
		} else if (ae.getSource() == twp) {
			openWebsite("https://twp.swisscom.com/vpn/index.html");
		} else if (ae.getSource() == alm) {
			try {
				Runtime.getRuntime()
						.exec("cmd.exe /c start iexplore https://itsalm1.swisscom.com/qcbin/start_a.jsp");

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		refreshServerNames();
	}
	
	public void refreshServerNames() {
		if (box.getSelectedIndex() == 1) {
			// Erstellen der Buttons
			// BE Servers

			ArrayList<String[]> servers = serverManager.getActiveServers();
			for (int i = 0; i < buttonsBE.size(); i++) {
				buttonsBE.get(i).setText(servers.get(i)[2]);
				buttonsFE.get(i).setText(servers.get(i)[3]);
			}

			repaint();
		} else if (box.getSelectedIndex() == 0) {
			ArrayList<String[]> servers = serverManager.getActiveServers();
			for (int i = 0; i < buttonsBE.size(); i++) {
				buttonsBE.get(i).setText(servers.get(i)[0]);
				buttonsFE.get(i).setText(servers.get(i)[1]);
			}

			repaint();
		}
	}
	
	public void setServerButtons() {
		for (int i = 0; i < buttonsBE.size(); i++) {
			setColor(buttonsBE.get(i));
			setColor(buttonsFE.get(i));
		}
		
		// Positionierung
		int x = 0;
		// BE-Server
		for (int i = 0; i < buttonsBE.size(); i++) {
			buttonsBE.get(i).setBounds(x, 40, 110, 50);
			x += 110;
		}

		int y = 0;
		// FE-Server
		for (int i = 0; i < buttonsFE.size(); i++) {
			buttonsFE.get(i).setBounds(y, 120, 110, 50);
			y += 110;
		}
	}
	
	public void reloadServers() {
		for (int i = 0; i < buttonsBE.size(); i++) {
			remove(buttonsBE.get(i));
			remove(buttonsFE.get(i));
		}
		buttonsBE.clear();
		buttonsFE.clear();
		
		
		ArrayList<String[]> servers = serverManager.getActiveServers();
		
		if (box.getSelectedIndex() == 1) {
			for (String[] serverNames : servers) {
				buttonsBE.add(new JButton(serverNames[2]));
				buttonsFE.add(new JButton(serverNames[3]));
			}
		} else {
			for (String[] serverNames : servers) {
				buttonsBE.add(new JButton(serverNames[0]));
				buttonsFE.add(new JButton(serverNames[1]));
			}
		}
		
		setServerButtons();
		setSize(buttonsBE.size() * 110, 250);
		httpsBox.setBounds(buttonsBE.size() * 110 - 100, 190, 100, 20);
		devModeBox.setBounds(buttonsBE.size() * 110 - 250, 190, 150, 20);
		repaint();
	}
}