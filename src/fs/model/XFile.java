package fs.model;

import org.json.JSONObject;

public class XFile {
	private int fileId;
	private String dsc;
	private int fileSize;
	
	
	public XFile() {
		super();
	}
	
	public XFile(JSONObject o) {
		super();
		fileId = o.getInt("file_id");
		dsc = o.getString("dsc");
		fileSize = o.getInt("file_size");
	}
	
	public int getFileId() {
		return fileId;
	}
	public void setFileId(int fileId) {
		this.fileId = fileId;
	}
	public String getDsc() {
		return dsc;
	}
	public void setDsc(String dsc) {
		this.dsc = dsc;
	}
	public int getFileSize() {
		return fileSize;
	}
	public void setFileSize(int fileSize) {
		this.fileSize = fileSize;
	}
	
	
	
}
