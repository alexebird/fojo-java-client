package fojo.client.net;

import org.json.*;

public class FojoEntry implements FojoRESTEntity {
	private int id;
	private int userId;
	private String description;
	/**
	 * Basename of file.
	 */
	private String fileName;
	/**
	 * Base64 encoded file contents.
	 */
	private String fileContentsBase64;
	
	public FojoEntry(int userId, String description) {
		this.userId = userId;
		this.description = description;
	}

	public String toJSON() throws JSONException {
		JSONObject fojoAttrs = new JSONObject();
		fojoAttrs.putOpt("user_id", this.userId);
		fojoAttrs.putOpt("description",this.description);
		
		JSONObject foodPhotoBase64 = new JSONObject();
		fojoAttrs.putOpt("file_name", this.fileName);
		fojoAttrs.putOpt("file_contents", this.fileContentsBase64);
		
		JSONObject obj = new JSONObject();
		obj.put("fojo_entry", fojoAttrs);
		obj.put("food_photo_base64_encoded", foodPhotoBase64);
		
		return obj.toString();
	}

	public void fromJSON(String jsonString) {
		// TODO Auto-generated method stub
		
	}

	public void createRemote() {

	}

	public void updateRemote() {
		// TODO Auto-generated method stub
		
	}

	public void deleteRemote() {
		// TODO Auto-generated method stub
		
	}

	public void showRemote(int fojoEntryId) {
		// TODO Auto-generated method stub
		
	}

	public void showAllRemote() {
		// TODO Auto-generated method stub
		
	}

	public String getDescription() {
		return this.description;
	}
	
	public int getUserId() {
		return this.userId;
	}

	public String getRESTName() {
		return "fojo_entries";
	}

	public int getId() {
		return this.id;
	}
}
