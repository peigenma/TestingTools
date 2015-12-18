import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class HomeFrame extends JFrame implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JButton exe, selImport, selScript;
	private JTextField importFile, scriptFile;
	private JLabel importLabel, scriptLabel;
	private JTextArea errorArea, consoleArea;
	private JScrollPane errorScrollPane, consoleScrollPane;
	private JButton consoleButton;
	private boolean consoleMode;

	public HomeFrame() {
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new FlowLayout());
		setSize(650, 350);
		setTitle("AllegroCSVTool");
		setResizable(false);

		Dimension screenDim = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation(screenDim.width / 3, screenDim.height / 3);

		importLabel = new JLabel("Import file:");
		add(importLabel);
		importFile = new JTextField(40);
		add(importFile);

		selImport = new JButton("Select");
		selImport.addActionListener(this);
		add(selImport);

		scriptLabel = new JLabel("Script file:");
		add(scriptLabel);
		scriptFile = new JTextField(40);
		add(scriptFile);

		selScript = new JButton("Select");
		selScript.addActionListener(this);
		add(selScript);

		exe = new JButton("Execute!");
		exe.addActionListener(this);
		add(exe);

		setUpConsoleArea();
		setUpErrorArea();
		
		consoleMode = false;
		consoleButton = new JButton("Start console mode");
		consoleButton.addActionListener(this);
		add(consoleButton);

		setVisible(true);
	}
	
	public void setUpErrorArea() {
		errorArea = new JTextArea(11, 50);
		errorArea.setEditable(false);
		errorArea.setLineWrap(true);
		errorArea.setWrapStyleWord(true);

		errorScrollPane = new JScrollPane(errorArea);
		errorScrollPane
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		add(errorScrollPane);
	}
	
	public void setUpConsoleArea() {
		consoleArea = new JTextArea(7, 50);
		consoleArea.setEditable(true);
		consoleArea.setLineWrap(true);
		consoleArea.setWrapStyleWord(true);
		
		consoleScrollPane = new JScrollPane(consoleArea);
		consoleScrollPane.setVisible(false);
		consoleScrollPane
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		
		add(consoleScrollPane);
	}

	public void actionPerformed(ActionEvent event) {
		if( consoleMode ) {
			consoleModeAction(event);
		} else {
			scriptFileAction(event);
		}
	}
	
	public void toggleConsoleMode(boolean console) {
		consoleMode = console;
		errorArea.setText("");
		scriptFile.setVisible(!console);
		selScript.setVisible(!console);
		scriptLabel.setVisible(!console);
		consoleScrollPane.setVisible(console);
		errorArea.setRows(console ? 6 : 10);
	}
	
	public void scriptFileAction(ActionEvent event)
	{
		if (event.getSource() == exe) {
			errorArea.setText("");

			String importPath = importFile.getText();
			String scriptPath = scriptFile.getText();

			if (importPath.equals("") || scriptPath.equals("")) {
				errorArea
						.append("Please select the import and the script file!\n");
				return;
			}

			try {
				CSVManager csv = new CSVManager(importPath, ";");

				boolean success = CommandManager.handleFileCommands(csv,
						scriptPath, errorArea);

				if (success) {
					csv.saveCSV();
					errorArea.append("successfully saved\n");
				} else {
					errorArea
							.append("CSV file has not been saved because of errors\n");
				}
			} catch (IOException | IndexOutOfBoundsException e) {
				errorArea.append("CSV file could not be opened: "
						+ e.getMessage());
			} catch (OutOfMemoryError e) {
				errorArea.append("There is not enough memory for such a big file");
			}
		} else if (event.getSource() == selImport) {
			JFileChooser fileDialog = new JFileChooser();
			fileDialog.setDialogTitle("Select import .csv file");
			int returnVal = fileDialog.showOpenDialog(this);

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				importFile.setText(fileDialog.getSelectedFile().getPath());
			}
		} else if (event.getSource() == selScript) {
			JFileChooser fileDialog = new JFileChooser();
			fileDialog.setDialogTitle("Select script file");
			int returnVal = fileDialog.showOpenDialog(this);

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				scriptFile.setText(fileDialog.getSelectedFile().getPath());
			}
		} else if( event.getSource() == consoleButton ) {
			toggleConsoleMode(true);
			consoleButton.setText("End console mode");
		}
	}
	
	public void consoleModeAction(ActionEvent event)
	{
		if (event.getSource() == exe) {
			errorArea.setText("");
			String importPath = importFile.getText();

			if (importPath.equals("")) {
				errorArea
						.append("Please select the import file!\n");
				return;
			}
			
			String commandText = consoleArea.getText();

			try {
				CSVManager csv = new CSVManager(importPath, ";");

				boolean success = CommandManager.handleCommandText(csv,
						commandText, errorArea);

				if (success) {
					csv.saveCSV();
					errorArea.append("successfully saved\n");
				} else {
					errorArea
							.append("CSV file has not been saved because of errors\n");
				}
			} catch (IOException | IndexOutOfBoundsException | IllegalArgumentException e) {
				errorArea.append("CSV file could not be opened: "
						+ e.getMessage());
			} catch (OutOfMemoryError e) {
				errorArea.append("There is not enough memory for such a big file!");
			}
		} else if (event.getSource() == selImport) {
			JFileChooser fileDialog = new JFileChooser();
			fileDialog.setDialogTitle("Select import .csv file");
			int returnVal = fileDialog.showOpenDialog(this);

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				importFile.setText(fileDialog.getSelectedFile().getPath());
			}
		} else if( event.getSource() == consoleButton ) {
			toggleConsoleMode(false);
			consoleButton.setText("Start console mode");
		}
	}

}
