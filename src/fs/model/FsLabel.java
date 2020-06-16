package fs.model;

import java.util.HashSet;
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
	private String longDsc;
	private String synonyms;
	
	private String bodyLabelIds;
	private String mechanismLabelIds;
	private Integer gender;
	private Integer minAge;
	private Integer maxAge;
	
	private Map<Integer, Set<Integer>> _phraseMap;
	private List<FsLabel> _children;
	private Set<Integer> _setBodyLabels;
	private Set<Integer> _setMechanismLabels;

	
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
		if(j.has("long_dsc"))this.longDsc = j.getString("long_dsc");
		if(j.has("lkp_gender_type"))this.gender = j.getInt("lkp_gender_type");
		if(j.has("min_age"))this.minAge = j.getInt("min_age");
		if(j.has("max_age"))this.maxAge = j.getInt("max_age");
		this._setBodyLabels = new HashSet<Integer>();
		this._setMechanismLabels = new HashSet<Integer>();
		if(this.bodyLabelIds!=null && this.bodyLabelIds.length()>0)for(String k:this.bodyLabelIds.split(","))this._setBodyLabels.add(Integer.parseInt(k));
		if(this.mechanismLabelIds!=null && this.mechanismLabelIds.length()>0)for(String k:this.mechanismLabelIds.split(","))this._setMechanismLabels.add(Integer.parseInt(k));
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

	public String getLongDsc() {
		return longDsc;
	}

	public void setLongDsc(String longDsc) {
		this.longDsc = longDsc;
	}

	public Integer getGender() {
		return gender;
	}

	public void setGender(Integer gender) {
		this.gender = gender;
	}

	public Integer getMinAge() {
		return minAge;
	}

	public void setMinAge(Integer minAge) {
		this.minAge = minAge;
	}

	public Integer getMaxAge() {
		return maxAge;
	}

	public void setMaxAge(Integer maxAge) {
		this.maxAge = maxAge;
	}

	public Set<Integer> get_setBodyLabels() {
		return _setBodyLabels;
	}

	public void set_setBodyLabels(Set<Integer> _setBodyLabels) {
		this._setBodyLabels = _setBodyLabels;
	}

	public Set<Integer> get_setMechanismLabels() {
		return _setMechanismLabels;
	}

	public void set_setMechanismLabels(Set<Integer> _setMechanismLabels) {
		this._setMechanismLabels = _setMechanismLabels;
	}


	
	
	
}
