package fs.model;
import org.json.JSONObject;


public class FsPhraseLabel {
	private int phraseLabelId;
	private int labelId;
	private int phraseId;
	private int tabOrder;
	
	
	public FsPhraseLabel(JSONObject j) {
		super();
		this.phraseLabelId = j.getInt("phrase_label_id");
		this.labelId = j.getInt("label_id");
		this.phraseId = j.getInt("phrase_id");
		this.tabOrder = j.getInt("tab_order");
	}
	public int getPhraseLabelId() {
		return phraseLabelId;
	}
	public void setPhraseLabelId(int phraseLabelId) {
		this.phraseLabelId = phraseLabelId;
	}
	public int getLabelId() {
		return labelId;
	}
	public void setLabelId(int labelId) {
		this.labelId = labelId;
	}
	public int getPhraseId() {
		return phraseId;
	}
	public void setPhraseId(int phraseId) {
		this.phraseId = phraseId;
	}
	public int getTabOrder() {
		return tabOrder;
	}
	public void setTabOrder(int tabOrder) {
		this.tabOrder = tabOrder;
	}
	
	
}
