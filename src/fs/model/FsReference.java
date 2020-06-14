package fs.model;

import org.json.JSONObject;

public class FsReference {
	private int referenceId;
	private String title;
	private String url;
	private String publishDt;
	
	public FsReference() {
		super();
	}
	
	
	public FsReference(JSONObject o) {
		super();
		referenceId = o.getInt("reference_id");
		title = o.getString("reference_title");
		url = o.getString("reference_url");
		publishDt = o.getString("publish_dt").substring(0,10);
	}
	
	
	public int getReferenceId() {
		return referenceId;
	}
	public void setReferenceId(int referenceId) {
		this.referenceId = referenceId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}


	public String getPublishDt() {
		return publishDt;
	}

	public void setPublishDt(String publishDt) {
		this.publishDt = publishDt;
	}

	
}
