package fs.cache;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import fs.model.FsLabel;
import fs.model.FsPhrase;
import fs.model.FsPhraseLabel;
import fs.model.XFile;
import fs.util.HttpUtil;

public class FsCache {
	public static Map<Integer, FsLabel> labelMap;
	public static Map<Integer, FsPhrase> phraseMap;
	private static String url = "http://localhost/rest/16c51c8e-28c9-4b6f-b603-0b584d2d78a7/FogSafe/";
	
	public static void reloadCache() {
		long startTime = System.currentTimeMillis();
		System.out.println("Start cache loading");
		String s = HttpUtil.send(url+"login", "userName=demo&passWord=demo");
		System.out.println(s);
		
		System.out.println("Authenticated in " + (System.currentTimeMillis()- startTime) + " ms");
		startTime = System.currentTimeMillis();

		JSONObject j = new JSONObject(s);
		if(!j.getBoolean("success")) {
			return;
		}
		String token = j.getString("token");
		s = HttpUtil.send(url+"ListFiles", "tokenKey=" + URLEncoder.encode(token));


		JSONObject jfiles =  new JSONObject(s);
		System.out.println("Files in " + (System.currentTimeMillis()- startTime) + " ms");
		startTime = System.currentTimeMillis();		
		
		s = HttpUtil.send(url+"ListLabels", "tokenKey=" + URLEncoder.encode(token));


		JSONObject jlabels =  new JSONObject(s);

		
//		
		JSONArray data = jlabels.getJSONArray("data");
		int labelSize = data.length();
		List<FsLabel> labels = new ArrayList(labelSize);//find("from FsLabel order by labelId");
		Map<Integer, FsLabel> labelMap = new HashMap<Integer, FsLabel>(labelSize*4/3+3);
		for(int qi=0;qi<labelSize;qi++) {
			FsLabel l = new FsLabel(data.getJSONObject(qi));
			labels.add(l);
			labelMap.put(l.getLabelId(), l);
		}
		for(FsLabel l : labels) if(l.getParentLabelId()!=0){
			FsLabel p = labelMap.get(l.getParentLabelId());
			if(p!=null) {
				if(p.get_children()==null)p.set_children(new ArrayList<FsLabel>());
				p.get_children().add(l);
			}		
		}
		System.out.println("Labels in " + (System.currentTimeMillis()- startTime) + " ms");
		startTime = System.currentTimeMillis();
		
		
		s = HttpUtil.send(url+"ListPhrases", "tokenKey=" + URLEncoder.encode(token));

		JSONObject jphrases =  new JSONObject(s);
		data = jphrases.getJSONArray("data");
		int phraseSize = data.length();
		List<FsPhrase> phrases = new ArrayList(phraseSize);//find("from FsPhrase order by phraseId");
		Map<Integer, FsPhrase> phraseMap = new HashMap<Integer, FsPhrase>(phraseSize*4/3+3);
		//for(FsPhrase p : phrases)phraseMap.put(p.getPhraseId(), p);
		for(int qi=0;qi<phraseSize;qi++) {
			FsPhrase p = new FsPhrase(data.getJSONObject(qi));
			phrases.add(p);
			phraseMap.put(p.getPhraseId(), p);
		}
		

		for(FsLabel l : labels) if(l.getParentLabelId()!=0){
			FsLabel p = labelMap.get(l.getParentLabelId());
			if(p!=null) {
				if(p.get_children()==null)p.set_children(new ArrayList<FsLabel>());
				p.get_children().add(l);
			}		
		}

		data = jfiles.getJSONArray("data");
		int fileSize = data.length();
		for(int qi=0;qi<fileSize;qi++) {
			JSONObject o = data.getJSONObject(qi);
			if(o.getInt("table_id")==5350) {//phrase
				FsPhrase p = phraseMap.get(o.getInt("table_pk"));
				if(p==null)continue;
				if(p.get_files()==null) {
					p.set_files(new ArrayList());
				}
				p.get_files().add(new XFile(o));
			}

		}
		
		System.out.println("Phrases in " + (System.currentTimeMillis()- startTime) + " ms");
		startTime = System.currentTimeMillis();
		
		s = HttpUtil.send(url+"ListPhraseLabels", "tokenKey=" + URLEncoder.encode(token));

		JSONObject jphraseLabels =  new JSONObject(s);
		data = jphraseLabels.getJSONArray("data");
		int phraseLabelSize = data.length();
		List<FsPhraseLabel> phraseLabels = new ArrayList(phraseLabelSize);//find("from FsPhraseLabel order by phraseId, tabOrder, labelId");
		for(int qi=0;qi<phraseLabelSize;qi++){
			FsPhraseLabel pl = new FsPhraseLabel(data.getJSONObject(qi));
			phraseLabels.add(pl);
			FsLabel l = labelMap.get(pl.getLabelId());
			if(l==null)continue;
			FsPhrase p = phraseMap.get(pl.getPhraseId());
			if(p==null)continue;

//			if(l.getLkpLabelCategoryType()!=1 && l.getLkpLabelCategoryType()!=6 && l.getLkpLabelCategoryType()!=14 && l.getLkpLabelCategoryType()!=19)
				p.get_setLkpLabelCategoryType().add(l.getLkpLabelCategoryType());

			switch(pl.getTabOrder()) {//1:body, 2:mechanism, 3:maagement, 4...
			case	1:
				if(l.getLkpLabelCategoryType()==1) {
					p.set_bodyLabelId(pl.getLabelId());
					continue;
				}
				break;
			case	2:
				if(l.getLkpLabelCategoryType()==6) {
					p.set_mechanismLabelId(pl.getLabelId());
					continue;
				}
				break;				
			case	3:
				if(l.getLkpLabelCategoryType()==14 || l.getLkpLabelCategoryType()==19) {
					p.set_managementLabelId(pl.getLabelId());
					continue;
				}
			}
			
			if(l.get_phraseMap()==null)l.set_phraseMap(new HashMap());
			if(l.get_phraseMap().get(pl.getPhraseId())==null)l.get_phraseMap().put(pl.getPhraseId(), new HashSet<Integer>());
			l.get_phraseMap().get(pl.getPhraseId()).add(pl.getTabOrder());
			
			if(p.get_setTabOrder()==null)p.set_setTabOrder(new HashSet());
			p.get_setTabOrder().add(pl.getTabOrder());
			
		}
		System.out.println("PhraseLabels in " + (System.currentTimeMillis()- startTime) + " ms");
		startTime = System.currentTimeMillis();

		
		s = HttpUtil.send(url+"ListPhraseConditions", "tokenKey=" + URLEncoder.encode(token));

		JSONObject jphraseConditions=  new JSONObject(s);
		data = jphraseConditions.getJSONArray("data");
		int phraseConditionSize = data.length();
		for(int qi=0;qi<phraseConditionSize;qi++){
			JSONObject jo = data.getJSONObject(qi);
			FsPhrase p = phraseMap.get(jo.getInt("phrase_id"));
			if(p==null)continue;
			switch(jo.getInt("lkp_condition_type")) {
			case	1://age
				p.set_minAge(jo.getInt("num_value1"));
				p.set_maxAge(jo.getInt("num_value2"));
				break;
			case	2://age:months
				p.set_minAge(-jo.getInt("num_value1"));
				p.set_maxAge(-jo.getInt("num_value2"));
				break;
			case	1001://gender 
				p.set_lkpGender(jo.getInt("str_values"));
				break;
			}	
			
		}
		System.out.println("PhraseConditions in " + (System.currentTimeMillis()- startTime) + " ms");
		
		FsCache.labelMap = labelMap;
		FsCache.phraseMap = phraseMap;
		
		

	}
	

}
