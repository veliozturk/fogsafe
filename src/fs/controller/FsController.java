package fs.controller;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import fs.cache.FsCache;
import fs.model.FsLabel;
import fs.model.FsPhrase;



import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FsController {
		public final static int maxStrLen = 500;
		
		private boolean isEmpty(String s) {
			return s==null || s.length()==0;
		}
		

	
	  @GetMapping("/reload")
	  Map<String, Object> reload(){
		  Map<String, Object> m = new HashMap<String, Object>();
		  FsCache.reloadCache();
		  m.put("success", true);
		  return m;
		  
	  }
	  
	  private void recursiveLabel(FsLabel l, Map<Integer, Set<Integer>> result, Integer lkpLabelCategoryType, Set<Integer> setBodyLabels) {
		  //System.out.println(l.getLabelId());
		  if(l.get_phraseMap()!=null)for(Integer pid:l.get_phraseMap().keySet()){
			  FsPhrase p = FsCache.phraseMap.get(pid);

			  if(p!=null && p.get_setLkpLabelCategoryType().contains(lkpLabelCategoryType) && (setBodyLabels==null || (p.get_bodyLabelId()!=null && setBodyLabels.contains(p.get_bodyLabelId().intValue())))) {
				  Set<Integer> ss = result.get(pid);
				  if(ss==null) {
					  ss = new HashSet<Integer>();
					  ss.addAll(p.get_setTabOrder());
					  result.put(pid, ss);
				  }
				  ss.removeAll(l.get_phraseMap().get(pid));
			  }
		  }
		  if(l.get_children()!=null)
			  for(FsLabel cl:l.get_children())
				  recursiveLabel(cl, result, lkpLabelCategoryType, setBodyLabels);
	  }
	  
	  
	  private void recursiveLabel2(FsLabel l, Map<Integer, Set<Integer>> result, Set<Integer> setBodyLabels) {
		  //System.out.println(l.getLabelId());
		  if(l.get_phraseMap()!=null)for(Integer pid:l.get_phraseMap().keySet()){
			  FsPhrase p = FsCache.phraseMap.get(pid);

			  if(p!=null && (p.get_setLkpLabelCategoryType().contains(14) || p.get_setLkpLabelCategoryType().contains(19)) && (setBodyLabels==null || (p.get_bodyLabelId()!=null && setBodyLabels.contains(p.get_bodyLabelId().intValue())))) {
				  Set<Integer> ss = result.get(pid);
				  if(ss==null) {
					  ss = new HashSet<Integer>();
					  ss.addAll(p.get_setTabOrder());
					  result.put(pid, ss);
				  }
				  ss.removeAll(l.get_phraseMap().get(pid));
			  }
		  }
		  if(l.get_children()!=null)
			  for(FsLabel cl:l.get_children())
				  recursiveLabel2(cl, result, setBodyLabels);
	  }
	  
	  
	
	  
	  @GetMapping("/labels")
	  Map<String, Object> labels(
			  @RequestParam(required = false) String xdsc) {
		  Map<String, Object> m = new HashMap<String, Object>();
		  m.put("success", true);
		  List<FsLabel> labels = new ArrayList<FsLabel>();
		  System.out.println("labels: " + xdsc);
		  final String fdsc = xdsc.trim().toLowerCase();
		  if(isEmpty(fdsc))return m;
		  Set<Integer> cs = new HashSet<Integer>();
		  cs.add(1);cs.add(11);cs.add(13);cs.add(7);cs.add(15);cs.add(5);

		  for(FsLabel l:FsCache.labelMap.values())if(cs.contains(l.getLkpLabelCategoryType()) && l.getDsc().toLowerCase().contains(fdsc)) {
			  labels.add(l);
		  }
		  labels.sort((Comparator<? super FsLabel>) new Comparator<FsLabel>() {
			    @Override
			    public int compare(FsLabel m1, FsLabel m2) {
			        if(m1.getLkpLabelCategoryType() == m2.getLkpLabelCategoryType()){
			        	boolean b1 = m1.getDsc().toLowerCase().startsWith(fdsc);			        	
			        	boolean b2 = m2.getDsc().toLowerCase().startsWith(fdsc);
			        	
			        	if(b1 ^ b2) {
			        		return b1 ? -1:1;
			        	} else return m1.getDsc().toLowerCase().compareTo(m2.getDsc().toLowerCase());
			            //return m1.getDsc().compareTo(m2.getDsc());
			        }
			        return m1.getLkpLabelCategoryType() - m2.getLkpLabelCategoryType()==0?0:
			        	(m1.getLkpLabelCategoryType() - m2.getLkpLabelCategoryType()>0?1:-1)
			        	;
			     }
			});
		  List<Map> data = new ArrayList<Map>();
		  for(FsLabel l:labels) {
			  Map<String, Object> mm = new HashMap();
			  mm.put("id", l.getLabelId());
			  mm.put("dsc", l.getDsc());
			  mm.put("xtype", l.getLkpLabelCategoryType());
			  data.add(mm);
			  
		  }
		  m.put("data", data);

		  return m;
		  
	  }
	  
	  @GetMapping("/phrase")
	  Map<String, Object> phrase(
			  @RequestParam(required = true) Integer phraseId) {
		  Map<String, Object> m = new HashMap<String, Object>();
		  m.put("success", true);
		  System.out.println("phrase: " + phraseId);
		  FsPhrase p = FsCache.phraseMap.get(phraseId);
		  if(p!=null) {
			  m.put("body", p.getBody());
			  m.put("xtype", p.getLkpPhraseType());
			  if(p.get_files()!=null)
				  m.put("files", p.get_files());
		  }
		  return m;
		  
	  }
	  
	
	  @GetMapping("/query")
	  Map<String, Object> query(@RequestParam Integer lkpLabelCategoryType,
			  @RequestParam(required = false) Integer lkpGender,
			  @RequestParam(required = false) Integer age,
			  @RequestParam(required = false) String labelIds) {
		  Map<String, Object> m = new HashMap<String, Object>();
		  System.out.println("query: " + lkpLabelCategoryType);
		  long startTime = System.currentTimeMillis();
		  
		  Map<Integer, Set<Integer>> result = new HashMap<Integer, Set<Integer>>();
		  Set<Integer> setBodyLabels = null;
		  Set<Integer> setLabels = null;
		  
		  if(!isEmpty(labelIds) && !labelIds.equals("null")) {
			  String[] xx = labelIds.split(",");
			  for(int qi=0;qi<xx.length;qi++) {
				  FsLabel l = FsCache.labelMap.get(new Integer(xx[qi]));
				  if(l.getLkpLabelCategoryType() ==1) {
					  if(setBodyLabels == null)
						  setBodyLabels = new HashSet();
					  setBodyLabels.add(l.getLabelId());	
				  } else { //normal
					  if(setLabels == null)
						  setLabels = new HashSet();
					  setLabels.add(l.getLabelId());	
				  }
			  }			  
		  }
		  
		  if(setLabels!=null) {
			  for(Integer lid:setLabels) try{
				  recursiveLabel(FsCache.labelMap.get(lid), result, lkpLabelCategoryType, setBodyLabels);
			  } catch(Exception e) {
				  e.printStackTrace();
			  }
		  } else if(setBodyLabels!=null)for(Integer pid:FsCache.phraseMap.keySet()){
			  FsPhrase p = FsCache.phraseMap.get(pid);
			  if(pid==6626) {
				  p = p;
			  }
			  if(p.get_bodyLabelId()!=null && setBodyLabels.contains(p.get_bodyLabelId().intValue()) && p.get_setLkpLabelCategoryType().contains(lkpLabelCategoryType)) {
				  Set<Integer> ss = result.get(pid);
				  if(ss==null) {
					  ss = new HashSet<Integer>();
					  ss.addAll(p.get_setTabOrder());
					  result.put(pid, ss);
				  }
			  }
		  } else if(lkpGender!=null && age!=null) {
			  for(Integer pid:FsCache.phraseMap.keySet()){
				  FsPhrase p = FsCache.phraseMap.get(pid);
				  if(pid==6660) {
					  p = p;
				  }
				  if((p.get_lkpGender()==null || lkpGender.intValue()==p.get_lkpGender().intValue()) && p.get_minAge()<=age.intValue() && p.get_maxAge()>=age.intValue() && p.get_setLkpLabelCategoryType()!=null && p.get_setLkpLabelCategoryType().contains(lkpLabelCategoryType.intValue())) {
					  Set<Integer> ss = result.get(pid);
					  if(ss==null) {
						  ss = new HashSet<Integer>();
						  ss.addAll(p.get_setTabOrder());
						  result.put(pid, ss);
					  }
				  }
			  }
		  }
		  
		  List<FsPhrase> lp = new ArrayList<FsPhrase>(); // list of phrases
		  for(Integer pid:result.keySet()) {
			  if(result.get(pid).isEmpty()) {//.isEmpty() !=null
				  FsPhrase p = FsCache.phraseMap.get(pid); 
				  if((lkpGender==null || p.get_lkpGender()==null || lkpGender.intValue()==p.get_lkpGender().intValue())
						  && (age==null || (p.get_minAge()<=age.intValue() && p.get_maxAge()>=age.intValue()))) {
					  lp.add(p);
					  if(lp.size()>50)break;
				  }
			  }
		  }
		  m.put("data", lp);
		  
		  Map<String,Set<Integer>> lm = new HashMap(); //list of manifestation labels
		  Map<String,Set<Integer>> ld = new HashMap(); //list of disease labels
		  Map<String,Set<Integer>> lt = new HashMap(); //list of treatment labels
		  Set<String> bodyMechSet = new HashSet();
		  for(FsPhrase p:lp)  if(p.get_bodyLabelId()!=null && p.get_mechanismLabelId()!=null) {
			  String k = p.get_bodyLabelId()+"."+p.get_mechanismLabelId();
			  if(bodyMechSet.contains(k))continue;
			  bodyMechSet.add(k);
			  Set<Integer> m2 = null;
			  for(FsLabel l:FsCache.labelMap.values())if(l.getBodyLabelIds()!=null 
					  && (","+l.getBodyLabelIds()+",").contains(","+p.get_bodyLabelId()+",")
					  && l.getMechanismLabelIds()!=null
					  && (","+l.getMechanismLabelIds()+",").contains(","+p.get_mechanismLabelId()+",")) {
				  switch(l.getLkpLabelCategoryType()) {
				  case 7://manif
					  m2 = lm.get(k);
					  if(m2==null) {
						  m2 = new HashSet();
						  lm.put(k, m2);
					  }
					  break;
				  case 5://disease
					  m2 = ld.get(k);
					  if(m2==null) {
						  m2 = new HashSet();
						  ld.put(k, m2);
					  }
					  break;
				  case 13://treatment
					  m2 = lt.get(k);
					  if(m2==null) {
						  m2 = new HashSet();
						  lt.put(k, m2);
					  }
					  break;
				  default:
					  continue;
					  
				  }			  
				  m2.add(l.getLabelId());
			  }
		  }
		  
		  m.put("manifestations", lm);
		  m.put("diseases", ld);
		  m.put("treatments", lt);
		  m.put("totalCount", result.size());
		  m.put("success", true);
			System.out.println("queryResult in " + (System.currentTimeMillis()- startTime) + " ms");
		  return m;
	  }
	  
		
	  @GetMapping("/query2")
	  Map<String, Object> query2(
			  @RequestParam(required = false) Integer lkpGender,
			  @RequestParam(required = false) Integer age,
			  @RequestParam(required = false) Integer height,
			  @RequestParam(required = false) Integer weight,
			  @RequestParam(required = false) String labelIds) {
		  Map<String, Object> m = new HashMap<String, Object>();
		  int lkpLabelCategoryType2 = 14;
		  System.out.println("query: " + lkpLabelCategoryType2);
		  long startTime = System.currentTimeMillis();
		  
		  Map<Integer, Set<Integer>> result = new HashMap<Integer, Set<Integer>>();
		  Set<Integer> setBodyLabels = null;
		  Set<Integer> setLabels = null;
		  
		  if(!isEmpty(labelIds) && !labelIds.equals("null")) {
			  String[] xx = labelIds.split(",");
			  for(int qi=0;qi<xx.length;qi++) {
				  FsLabel l = FsCache.labelMap.get(new Integer(xx[qi]));
				  if(l.getLkpLabelCategoryType() ==1) {
					  if(setBodyLabels == null)
						  setBodyLabels = new HashSet();
					  setBodyLabels.add(l.getLabelId());	
				  } else { //normal
					  if(setLabels == null)
						  setLabels = new HashSet();
					  setLabels.add(l.getLabelId());	
				  }
			  }			  
		  }
		  
		  if(setLabels!=null) {
			  for(Integer lid:setLabels) try{
				  recursiveLabel2(FsCache.labelMap.get(lid), result, setBodyLabels);
			  } catch(Exception e) {
				  e.printStackTrace();
			  }
		  } else if(setBodyLabels!=null)for(Integer pid:FsCache.phraseMap.keySet()){
			  FsPhrase p = FsCache.phraseMap.get(pid);
			  if(p.get_bodyLabelId()!=null && setBodyLabels.contains(p.get_bodyLabelId().intValue()) && (p.get_setLkpLabelCategoryType().contains(14) || p.get_setLkpLabelCategoryType().contains(19))) {
				  Set<Integer> ss = result.get(pid);
				  if(ss==null) {
					  ss = new HashSet<Integer>();
					  ss.addAll(p.get_setTabOrder());
					  result.put(pid, ss);
				  }
			  }
		  } else if(lkpGender!=null && age!=null) {
			  for(Integer pid:FsCache.phraseMap.keySet()){
				  FsPhrase p = FsCache.phraseMap.get(pid);
				  if(p.get_maxAge()<0) {
					  p = p;
				  }
				  if((p.get_lkpGender()==null || lkpGender.intValue()==p.get_lkpGender().intValue()) && 
						  ((age.intValue()>0 && p.get_minAge()<=age.intValue() && p.get_maxAge()>=age.intValue()) ||
						   (age.intValue()<0 && p.get_minAge()>=age.intValue() && p.get_maxAge()<=age.intValue()))	  && 
						  p.get_setLkpLabelCategoryType()!=null 
						  && (p.get_setLkpLabelCategoryType().contains(14) || p.get_setLkpLabelCategoryType().contains(19))) {
					  Set<Integer> ss = result.get(pid);
					  if(ss==null) {
						  ss = new HashSet<Integer>();
						  ss.addAll(p.get_setTabOrder());
						  result.put(pid, ss);
					  }
				  }
			  }
		  }
		  
		  List<FsPhrase> phrases = new ArrayList<FsPhrase>(); // list of phrases
		  for(Integer pid:result.keySet()) {
			  FsPhrase p = FsCache.phraseMap.get(pid); 
			  if(pid==4950) {
				  pid = pid;
			  }
			  if(result.get(pid).isEmpty()) {//.isEmpty() !=null

				  if(p.getPhraseId()==4950) {
					  p = p;
				  }
				  if((lkpGender==null || p.get_lkpGender()==null || lkpGender.intValue()==p.get_lkpGender().intValue())
						  && (age==null || (age.intValue()>0 && p.get_minAge()<=age.intValue() && p.get_maxAge()>=age.intValue())
								  || (age.intValue()<0 && p.get_minAge()>=age.intValue() && p.get_maxAge()<=age.intValue()))) {
					  phrases.add(p);
					  //if(phrases.size()>50)break;
				  }
			  }
		  }
		  

		  

		  Map<String, Map> bodyMech = new HashMap();
		  int id = 0;
		  for(FsPhrase p:phrases)  if(p.get_bodyLabelId()!=null && p.get_mechanismLabelId()!=null) {
			  String k = p.get_bodyLabelId()+"."+p.get_mechanismLabelId();
			  Map xx = bodyMech.get(k);
			  boolean isNew = xx==null;
			  if(isNew) {
				  xx = new HashMap();
				  bodyMech.put(k, xx);				  
			  }
			  
			  if(p.get_setLkpLabelCategoryType().contains(14)) { //consider
				  List<Map> considers = (List)xx.get("considers");
				  if(considers==null) {
					  considers = new ArrayList();
					  xx.put("considers", considers);
				  }
				  considers.add(p.get_asMap());
			  }
			  
			  if(p.get_setLkpLabelCategoryType().contains(19)) { //action
				  List<Map> actions = (List)xx.get("actions");
				  if(actions==null) {
					  actions = new ArrayList();
					  xx.put("actions", actions);
				  }
				  actions.add(p.get_asMap());
			  }
			  
			  if(!isNew)continue;
			  
			  xx.put("id", ++id);
			  FsLabel bodyLabel = FsCache.labelMap.get(p.get_bodyLabelId());
			  xx.put("body_part", bodyLabel!=null ? bodyLabel.getFullDsc(): ("MissingBodyLabel: "+p.get_bodyLabelId()));
			  xx.put("lkp_mechanism_type", p.get_mechanismLabelId());
			  if(p.get_mechanismLabelId()!=2050) {
				  FsLabel mechanismLabel = FsCache.labelMap.get(p.get_mechanismLabelId());
				  xx.put("lkp_mechanism_type_qw_", mechanismLabel!=null ? mechanismLabel.getDsc(): ("MissinMechanismLabel: " + p.get_mechanismLabelId()));
			  }
			  
			  Set<Integer> lm = new HashSet(); //list of manifestation labels
			  Set<Integer> ld = new HashSet(); //list of disease labels
			  Set<Integer> lt = new HashSet(); //list of treatment labels
			  
			  Set<Integer> m2 = null;
			  for(FsLabel l:FsCache.labelMap.values())if(l.getBodyLabelIds()!=null 
					  && (","+l.getBodyLabelIds()+",").contains(","+p.get_bodyLabelId()+",")
					  && l.getMechanismLabelIds()!=null
					  && (","+l.getMechanismLabelIds()+",").contains(","+p.get_mechanismLabelId()+",")) {
				  switch(l.getLkpLabelCategoryType()) {
				  case 7://manif
					  if(lm==null) {
						  lm = new HashSet();
					  }
					  m2 = lm;
					  break;
				  case 5://disease
					  if(ld==null) {
						  ld = new HashSet();
					  }
					  m2 = ld;
					  break;
				  case 13://treatment
					  if(lt==null) {
						  lt = new HashSet();
					  }
					  m2 = lt;
					  break;
				  default:
					  continue;
					  
				  }			  
				  m2.add(l.getLabelId());
			  }
			  if(lm!=null) {
				  List<Map> lmm = new ArrayList();
				  xx.put("manifestations", lmm);
				  for(Integer labelId:lm) {
					  Map mmm = new HashMap();
					  mmm.put("id", labelId);
					  FsLabel l = FsCache.labelMap.get(labelId);
					  mmm.put("dsc", l.getDsc());
					  if(!isEmpty(l.getLongDsc()))mmm.put("longDsc", l.getLongDsc());
					  if(!isEmpty(l.getSynonyms()))mmm.put("synonyms", l.getSynonyms());
					  lmm.add(mmm);
				  }
				  
			  }
			  if(ld!=null) {
				  List<Map> lmm = new ArrayList();
				  xx.put("diseases", lmm);
				  for(Integer labelId:ld) {
					  Map mmm = new HashMap();
					  mmm.put("id", labelId);
					  FsLabel l = FsCache.labelMap.get(labelId);
					  mmm.put("dsc", l.getDsc());
					  if(!isEmpty(l.getLongDsc()))mmm.put("longDsc", l.getLongDsc());
					  if(!isEmpty(l.getSynonyms()))mmm.put("synonyms", l.getSynonyms());
					  lmm.add(mmm);
				  }
				  
			  }
			  if(lt!=null) {
				  List<Map> lmm = new ArrayList();
				  xx.put("treatments", lmm);
				  for(Integer labelId:lt) {
					  Map mmm = new HashMap();
					  mmm.put("id", labelId);
					  FsLabel l = FsCache.labelMap.get(labelId);
					  mmm.put("dsc", l.getDsc());
					  if(!isEmpty(l.getLongDsc()))mmm.put("longDsc", l.getLongDsc());
					  if(!isEmpty(l.getSynonyms()))mmm.put("synonyms", l.getSynonyms());
					  lmm.add(mmm);
				  }
				  
			  }

		  }
		  List<Map> data = new ArrayList(bodyMech.values());
		  

		  data.sort((Comparator<? super Map>) new Comparator<Map>() {
			    @Override
			    public int compare(Map m1, Map m2) {
			        return m1.get("body_part").toString().compareTo(m2.get("body_part").toString());
			     }
			});
		  
		  
		  m.put("data", data);

		  m.put("success", true);
			System.out.println("query2Result in " + (System.currentTimeMillis()- startTime) + " ms");
		  return m;
	  }
}
