
import java.util.ArrayList;
import java.util.HashSet;


public class ServerManager {

	private HashSet<Integer> activeServers;
	private ArrayList<String[]> servers;
	private DataFile props;
	
	public ServerManager() {
		activeServers = new HashSet<Integer>();
		props = new DataFile();
		loadServers();
	}
	
	public ServerManager(DataFile props) {
		activeServers = new HashSet<Integer>();
		this.props = props;
		loadServers();
	}
	
	private void loadServers() {
		String[] activeServersIndexes = props.getData("activeServers").split(";");
		for( String index : activeServersIndexes ) {
			try {
				activeServers.add(Integer.parseInt(index));
			} catch( NumberFormatException e ) {
				e.printStackTrace();
			}
		}
		
		String[] rawServers = props.getData("servers").split(";");
		String[] serverNames;
		servers = new ArrayList<>();
		for (int i = 0; i < rawServers.length; i++) {
			serverNames = rawServers[i].split(",");
			servers.add(serverNames);
		}
	}
	
	
	public void saveServers() {
		StringBuilder serverString = new StringBuilder();
		for (String[] serverNames : servers) {
			serverString.append(serverNames[0]);
			for (int i = 1; i < 4; i++) {
				serverString.append(",");
				serverString.append(serverNames[i]);
			}
			serverString.append(";");
		}
		
		props.setData("servers", serverString.toString());
		
		StringBuilder activeServerString = new StringBuilder();
		for (Integer index : activeServers) {
			activeServerString.append(index);
			activeServerString.append(";");
		}
		
		props.setData("activeServers", activeServerString.substring(0, activeServerString.length()-1).toString());
		props.save();
	}
	
	public ArrayList<String[]> getActiveServers() {
		ArrayList<String[]> activeServerList = new ArrayList<>();
		for (int i = 0; i < servers.size(); i++) {
			if(activeServers.contains(i)) {
				activeServerList.add(servers.get(i));
			}
		}
		return activeServerList;
	}
	
	public ArrayList<String[]> getInactiveServers() {
		ArrayList<String[]> activeServerList = new ArrayList<>();
		for (int i = 0; i < servers.size(); i++) {
			if (!activeServers.contains(i)) {
				activeServerList.add(servers.get(i));
			}
		}
		return activeServerList;
	}
	
	public ArrayList<String[]> getAllServers() {
		return servers;
	}
	
	public void setStatus(String[] serverName, boolean active) {
		int index = servers.indexOf(serverName);
		if (active && !activeServers.contains(index)) {
			activeServers.add(index);
		} else if (!active && activeServers.contains(index)){
			activeServers.remove(index);
		}
	}
	
	public void addServer(String beName, String feName, String befName, String fefName) {
		String[] serverNames = { beName, feName, befName, fefName };
		servers.add(serverNames);		
	}
	
	public void removeServer(String[] serverNames) {
		int index = servers.indexOf(serverNames);
		servers.remove(index);
		
		if (activeServers.contains(index)) {
			activeServers.remove(index);
		}
		
		for (int serverIndex : activeServers ) {
			if( serverIndex > index ) {
				activeServers.remove(serverIndex);
				activeServers.add(serverIndex-1);
			}
		}
	}
	
	public String[] getServerByIndex(int index) {
		return servers.get(index);
	}
	
}
