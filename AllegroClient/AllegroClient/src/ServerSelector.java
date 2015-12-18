
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;


public class ServerSelector extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ServerManager serverManager;
	private boolean saved = false;
	private ArrayList<String[]> activeServers, inactiveServers;
	private JList<String> activeList, inactiveList;
	private DefaultListModel<String> activeListModel, inactiveListModel;
	private JButton addButton, removeButton, saveButton, createButton, deleteButton;
	
	public ServerSelector(JFrame owner, boolean modal, final ServerManager serverManager) {
		super(owner, modal);
		this.serverManager = serverManager;
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocation(600, 400);
		setTitle("Allegro Client -- Ergon AG");
		setResizable(false);
		setSize(400, 230);
		setLayout(new GridLayout());
		
		JPanel background = new JPanel();
		background.setLayout(new BoxLayout(background,BoxLayout.Y_AXIS));
		add(background);
		
		inactiveListModel = new DefaultListModel<>();
		inactiveList = new JList<String>(inactiveListModel);
		JScrollPane inactiveScrollPane = new JScrollPane(inactiveList);
		inactiveScrollPane 
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		
		activeListModel = new DefaultListModel<>();
		activeList = new JList<String>(activeListModel);
		JScrollPane activeScrollPane = new JScrollPane(activeList);
		inactiveScrollPane 
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);		
		
		loadServers();
		
		
		addButton = new JButton(">>>");
		addButton.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int index = inactiveList.getSelectedIndex();
				changeServerStatus(index, true);
			}
		});
		
		removeButton = new JButton("<<<");
		removeButton.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int index = activeList.getSelectedIndex();
				changeServerStatus(index, false);
			}
		});
		
		saveButton = new JButton("Save");
		saveButton.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				save();
			}
		});
		
		createButton = new JButton("Create");
		final JDialog parent = this;
		createButton.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ServerCreator creator = new ServerCreator(parent, true, serverManager);
				loadServers();
			}
		});
		
		deleteButton = new JButton("Delete");
		deleteButton.setToolTipText("Deletes the selected inactive server");
		deleteButton.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int index = inactiveList.getSelectedIndex();
				String[] serverNames = inactiveServers.get(index);
				serverManager.removeServer(serverNames);
				loadServers();
			}
		});
		
		JPanel buttons = new JPanel();
		buttons.setLayout(new BoxLayout(buttons,BoxLayout.Y_AXIS));
		buttons.add(addButton);
		buttons.add(removeButton);
		
		Component[] firstRow = { inactiveScrollPane, buttons, activeScrollPane };
		inactiveScrollPane.addComponentListener(new SizeChangeListener(firstRow, this));

		JPanel upperPanel = new JPanel();
		upperPanel.add(inactiveScrollPane);
		upperPanel.add(buttons);
		upperPanel.add(activeScrollPane);
		
		JPanel lowerPanel = new JPanel();
		lowerPanel.add(deleteButton);
		lowerPanel.add(saveButton);
		lowerPanel.add(createButton);
		
		background.add(upperPanel);
		background.add(lowerPanel);

		setVisible(true);
	}
	
	public void loadServers() {
		activeListModel.clear();
		inactiveListModel.clear();
		
		activeServers = serverManager.getActiveServers();
		for (String[] serverNames : activeServers) {
			activeListModel.addElement(serverNames[0] + "/" + serverNames[1]);
		}
		
		inactiveServers = serverManager.getInactiveServers();
		for (String[] serverNames : inactiveServers) {
			inactiveListModel.addElement(serverNames[0] + "/" + serverNames[1]);
		}
	}

	
	public void changeServerStatus(int index, boolean setActive) {
		if (setActive) {
			String[] serverNames = inactiveServers.get(index);
			serverManager.setStatus(serverNames, true);
		} else {
			String[] serverNames = activeServers.get(index);
			serverManager.setStatus(serverNames, false);
		}
		
		loadServers();
		repaint();
	}
	
	public void save() {
		serverManager.saveServers();
		saved = true;
		dispose();
	}
	
	public ServerManager getServerManager() {
		if( saved ) {
			return serverManager;
		} else {
			return new ServerManager();
		}
	}
}
