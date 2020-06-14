package fs.model;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONObject;

import fs.cache.FsCache;

public class FsLabel implements java.io.Serializable {

	private int labelId;
	private int parentLabelId;
	private int lkpLabelCategoryType;
	private String dsc;
	private String synonyms;
	
	private String bodyLabelIds;
	private String mechanismLabelIds;
	
	
	private Map<Integer, Set<Integer>> _phraseMap;
	private List<FsLabel> _children;
	
	public FsLabel() {
		super();
	}
	
	public FsLabel(JSONObject j) {
		super();
		this.labelId = j.getInt("label_id");
		this.parentLabelId = j.getInt("parent_label_id");
		this.lkpLabelCategoryType = j.getInt("lkp_label_category_type");
		this.bodyLabelIds = j.getString("body_label_ids");
		this.mechanismLabelIds = j.getString("mechanism_label_ids");
		if(j.has("dsc"))this.dsc = j.getString("dsc");
		if(j.has("synonyms"))this.synonyms = j.getString("synonyms");
	}

//	@Id
//	@Column(name="label_id")
	public int getLabelId() {
		return labelId;
	}


	public void setLabelId(int labelId) {
		this.labelId = labelId;
	}

	

	public String getDsc() {
		return dsc;
	}

	public void setDsc(String dsc) {
		this.dsc = dsc;
	}


	public String getFullDsc() {
		if(parentLabelId==0)return dsc;
		String s = dsc;
		int qi=0;
		for(FsLabel l = FsCache.labelMap.get(parentLabelId); l!=null && qi<10;l = FsCache.labelMap.get(l.getParentLabelId())) {
			s = l.getDsc() + " > " + s;
			qi++;
		}

		return s;
	}
	
	//	@Column(name="parent_label_id")
	public int getParentLabelId() {
		return parentLabelId;
	}


	public void setParentLabelId(int parentLabelId) {
		this.parentLabelId = parentLabelId;
	}


//	@Column(name="lkp_label_category_type")
	public int getLkpLabelCategoryType() {
		return lkpLabelCategoryType;
	}


	public void setLkpLabelCategoryType(int lkpLabelCategoryType) {
		this.lkpLabelCategoryType = lkpLabelCategoryType;
	}


//	@Column(name="body_label_ids")
	public String getBodyLabelIds() {
		return bodyLabelIds;
	}


	public void setBodyLabelIds(String bodyLabelIds) {
		this.bodyLabelIds = bodyLabelIds;
	}


//	@Column(name="mechanism_label_ids")
	public String getMechanismLabelIds() {
		return mechanismLabelIds;
	}


	public void setMechanismLabelIds(String mechanismLabelIds) {
		this.mechanismLabelIds = mechanismLabelIds;
	}


//	@Transient
	public Map<Integer, Set<Integer>> get_phraseMap() {
		return _phraseMap;
	}


	public void set_phraseMap(Map<Integer, Set<Integer>> _phraseMap) {
		this._phraseMap = _phraseMap;
	}

//	@Transient
	public List<FsLabel> get_children() {
		return _children;
	}

	public void set_children(List<FsLabel> _children) {
		this._children = _children;
	}

	public String getSynonyms() {
		return synonyms;
	}

	public void setSynonyms(String synonyms) {
		this.synonyms = synonyms;
	}
	
	
	
}
