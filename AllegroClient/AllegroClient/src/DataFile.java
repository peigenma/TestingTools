import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

import org.jasypt.util.text.BasicTextEncryptor;

/*
 *  DataFile allows the saving of encrypted name-value pairs in a .properties file
 */
public class DataFile {
	private Properties props;
	private File outputFile;
	private BasicTextEncryptor encryptor;

	public DataFile() {
		props = new Properties();
		encryptor = new BasicTextEncryptor();
		encryptor.setPassword(System.getProperty("user.name") + "%&/2"
				+ System.getProperty("os.name"));

		String filePath = System.getProperty("user.home")
				+ "\\.client.properties";

		try {
			FileReader file = new FileReader(new File(filePath));
			props.load(file);
			file.close();
			
			if (getData("servers") == null) {
				init();
			}
		} catch (IOException e) {
			init();
		}

		try {
			outputFile = new File(filePath);
			FileWriter output = new FileWriter(outputFile);
			props.store(output, "");
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*
	 * if there is no existing property file, properties are initialized with
	 * init()
	 */
	public void init()
	{
		setData( "puttyPath", "c:\\Program Files (x86)\\PuTTY\\putty.exe" );
		setData( "puttyUsername", "" );
		setData( "puttyPassword", "" );
		setData( "activeServers", "0;1;2;3;4;5;6;8;9;10;11;12;13");
		
		String[] standardBEServers = { "altmann", "bondy", "bonis", "buntrock", "berlioz","bizet", "byrne", "burdon","bonanza","bosch","biber","ortiz", "bixler", "dorati" };
		String[] standardFEServers = { "breiter", "komma", "frankel", "fiala", "farkas", "feo", "ferry", "gabriel","fritz","fuchsberg","fontana","mills", "nowka", "kollo" };
		String[] FallbackServersBE = { "agricola","arrigo","boone", "binchois", "berwald", "born","brent","britney","bonanza","bosch","biber","ortiz","bixler","dorati" };
		String[] FallbackServersFE = { "damm","dachstein","froberger", "fervers", "fasch", "fleischer","ferro","gane","fritz","fuchsberg","fontana","mills","nowka","kollo" };
		
		StringBuilder serverString = new StringBuilder();
		for(int i = 0; i < standardBEServers.length; i++) {
			serverString.append(standardBEServers[i]);
			serverString.append(",");
			serverString.append(standardFEServers[i]);
			serverString.append(",");
			serverString.append(FallbackServersBE[i]);
			serverString.append(",");
			serverString.append(FallbackServersFE[i]);
			serverString.append(";");
		}
		
		setData("servers", serverString.toString());
	}

	/*
	 * sets name-value pair
	 */
	public void setData(String name, String value) {
		props.setProperty(name, encryptor.encrypt(value));
	}

	/*
	 * saves properties
	 */
	public void save() {
		try {
			FileWriter output = new FileWriter(outputFile);
			props.store(output, "");
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*
	 * returns value of the associated name
	 */
	public String getData(String name) {
		return encryptor.decrypt(props.getProperty(name));
	}

}
