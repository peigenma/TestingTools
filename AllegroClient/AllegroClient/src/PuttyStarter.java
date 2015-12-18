import java.awt.Container;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;

/*
 * starts putty 
 */
public class PuttyStarter {
	private DataFile properties;
	private String puttyPath;
	private Container parent;

	public PuttyStarter(DataFile properties, Container parent) {
		this.properties = properties;
		puttyPath = properties.getData("puttyPath");
	}

	/*
	 * starts putty with the given server
	 */
	public void startPutty(String server) {
		File file = new File(puttyPath);

		if (file.exists() && puttyPath.endsWith("putty.exe")) {
			String username = properties.getData("puttyUsername");
			String password = properties.getData("puttyPassword");
			String login = "";

			if (!username.equals("")) {
				login += " -l " + username + " ";

				if (!password.equals("")) {
					login += " -pw " + password + " ";
				}
			}

			try {
				Runtime.getRuntime().exec(
						"\"" + puttyPath + "\" -ssh " + server + login);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			if (puttyPath.equals("") || !puttyPath.endsWith("putty.exe")
					|| !file.exists()) {
				JFileChooser fileDialog = new JFileChooser("c:\\");
				fileDialog.setDialogTitle("Select putty.exe");
				int returnVal = fileDialog.showOpenDialog(parent);

				if (returnVal == JFileChooser.APPROVE_OPTION) {
					puttyPath = fileDialog.getSelectedFile().getPath();
				}

				properties.setData("puttyPath", puttyPath);
				properties.save();
			} else {
				startPutty(server);
			}
		}
	}
}
