package fs.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONObject;

import fs.cache.FsCache;
import fs.controller.FsController;

public class FsPhrase {
	private int phraseId;
	private int referenceId;
	private String body;
	private String textBody;
	private String abbreviationIds;
//	private short activeFlag;
	private Short lkpPhraseType;
	private Short lkpMechanismType;
	
	private Integer _bodyLabelId;
	private Integer _mechanismLabelId;
	private Integer _managementLabelId;
	private Integer _lkpGender;
	private int _minAge;
	private int _maxAge;
	
	
	private Set<Integer> _setTabOrder;
	private Set<Integer> _setLkpLabelCategoryType;
	private List<XFile> _files;
	
	
	
	public FsPhrase() {
		super();
	}
	public FsPhrase(JSONObject j) {
		super();
		this.phraseId = j.getInt("phrase_id");
		this.referenceId = j.getInt("reference_id");
//		this.body = j.getString("body");
		if(j.has("body"))this.body = j.getString("body");
		this.textBody = j.getString("text_body");
		this.abbreviationIds = j.getString("abbreviation_ids");
//		this.activeFlag = (short)j.getInt("active_flag");
		this.lkpPhraseType = j.has("lkp_phrase_type") && !j.isNull("lkp_phrase_type") && j.get("lkp_phrase_type").toString().length()>0 ? (short)j.getInt("lkp_phrase_type"):null;
		this.lkpMechanismType = j.has("lkp_mechanism_type") && !j.isNull("lkp_mechanism_type") && j.get("lkp_mechanism_type").toString().length()>0  ? (short)j.getInt("lkp_mechanism_type"):null;
		this._setTabOrder = new HashSet<Integer>();
		this._setLkpLabelCategoryType = new HashSet<Integer>();
		this._minAge = 0;
		this._maxAge = 1000;
	}
	////	@Id
////	@Column(name="phrase_id")
	public int getPhraseId() {
		return phraseId;
	}
	public void setPhraseId(int phraseId) {
		this.phraseId = phraseId;
	}

//	@Column(name="reference_id")
	public int getReferenceId() {
		return referenceId;
	}
	public void setReferenceId(int referenceId) {
		this.referenceId = referenceId;
	}

//	@Column(name="body")
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public String getTextBody() {
		return textBody!=null && textBody.length()>FsController.maxStrLen ? textBody.substring(0, FsController.maxStrLen):textBody;
	}
//	@Column(name="text_body")
	public void setTextBody(String textBody) {
		this.textBody = textBody;
	}
	public String getAbbreviationIds() {
		return abbreviationIds;
	}
	public void setAbbreviationIds(String abbrevationIds) {
		this.abbreviationIds = abbrevationIds;
	}
/*
	//	@Column(name="active_Flag")
	public short getActiveFlag() {
		return activeFlag;
	}
	public void setActiveFlag(short activeFlag) {
		this.activeFlag = activeFlag;
	}*/
//	@Column(name="lkp_phrase_type")
	public Short getLkpPhraseType() {
		return lkpPhraseType;
	}
	public void setLkpPhraseType(Short lkpPhraseType) {
		this.lkpPhraseType = lkpPhraseType;
	}
//	@Column(name="lkp_mechanism_type")
	public Short getLkpMechanismType() {
		return lkpMechanismType;
	}
	public void setLkpMechanismType(Short lkpMechanismType) {
		this.lkpMechanismType = lkpMechanismType;
	}
	
//	@Transient
	public Set<Integer> get_setTabOrder() {
		return _setTabOrder;
	}
	public void set_setTabOrder(Set<Integer> _setTabOrder) {
		this._setTabOrder = _setTabOrder;
	}
	
//	@Transient
	public Integer get_bodyLabelId() {
		return _bodyLabelId;
	}
	public void set_bodyLabelId(Integer _bodyLabelId) {
		this._bodyLabelId = _bodyLabelId;
	}
	
//	@Transient
	public Integer get_mechanismLabelId() {
		return _mechanismLabelId;
	}
	public void set_mechanismLabelId(Integer _mechanismLabelId) {
		this._mechanismLabelId = _mechanismLabelId;
	}
	
//	@Transient
	public Integer get_managementLabelId() {
		return _managementLabelId;
	}
	public void set_managementLabelId(Integer _managementLabelId) {
		this._managementLabelId = _managementLabelId;
	}
	
	
	public Set<Integer> get_setLkpLabelCategoryType() {
		return _setLkpLabelCategoryType;
	}
	public void set_setLkpLabelCategoryType(Set<Integer> _setLkpLabelCategoryType) {
		this._setLkpLabelCategoryType = _setLkpLabelCategoryType;
	}
	public Integer get_lkpGender() {
		return _lkpGender;
	}
	public void set_lkpGender(Integer _lkpGender) {
		this._lkpGender = _lkpGender;
	}
	public int get_minAge() {
		return _minAge;
	}
	public void set_minAge(int _minAge) {
		this._minAge = _minAge;
	}
	public int get_maxAge() {
		return _maxAge;
	}
	public void set_maxAge(int _maxAge) {
		this._maxAge = _maxAge;
	}
	
	public Map get_asMap() {
		Map m = new HashMap();
		m.put("id", phraseId);
		m.put("dsc", getTextBody());
//		m.put("html", getBody());
		m.put("xtype", lkpPhraseType);
		FsLabel l = FsCache.labelMap.get(_managementLabelId);
		if(l!=null)m.put("xman", l.getDsc());
//		if(_files!=null)m.put("files", _files);
		return m;
	}
	
	public List<XFile> get_files() {
		return _files;
	}
	public void set_files(List<XFile> _files) {
		this._files = _files;
	}	

	
	
}
