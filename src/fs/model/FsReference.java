package fs.model;

import org.json.JSONObject;

public class FsReference {
	private int referenceId;
	private String title;
	private String url;
	private String publishDt;
	private String institutions;
	private static String[] months = new String[] {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
	
	public FsReference() {
		super();
	}
	
	
	public FsReference(JSONObject o) {
		super();
		referenceId = o.getInt("reference_id");
		title = o.getString("reference_title");
		url = o.getString("reference_url");
		publishDt = o.getString("publish_dt").substring(0,10);
		int month = Integer.parseUnsignedInt(publishDt.substring(3, 5))-1;
		publishDt = months[month] + ", " + publishDt.substring(6);
		if(o.has("reference_institution_ids"))institutions = o.getString("reference_institution_ids");
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


	public String getInstitutions() {
		return institutions;
	}


	public void setInstitutions(String institutions) {
		this.institutions = institutions;
	}

	
}
