package fs.model;

import org.json.JSONObject;

public class FsInstitution {
	private int institutionId;
	private String dsc;
	private String url;
	private String acronym;

	public FsInstitution() {
		super();
	}
	
	
	public FsInstitution(JSONObject o) {
		super();
		institutionId = o.getInt("reference_institution_id");
		dsc = o.getString("dsc");
		url = o.getString("institution_url");
		acronym = o.getString("acronym");

	}


	public int getInstitutionId() {
		return institutionId;
	}


	public void setInstitutionId(int institutionId) {
		this.institutionId = institutionId;
	}


	public String getDsc() {
		return dsc;
	}


	public void setDsc(String dsc) {
		this.dsc = dsc;
	}


	public String getUrl() {
		return url;
	}


	public void setUrl(String url) {
		this.url = url;
	}


	public String getAcronym() {
		return acronym;
	}


	public void setAcronym(String acronym) {
		this.acronym = acronym;
	}
	
	
	
}
