package fojo.client.net;

import org.json.JSONException;

public interface FojoRESTEntity {
	public void createRemote();
	public void updateRemote();
	public void deleteRemote();
	public void showRemote(int fojoEntryId);
	public void showAllRemote();
	
	public String getRESTName();
	public int getId();
	
	public String toJSON() throws JSONException;
	public void fromJSON(String jsonString);
}
