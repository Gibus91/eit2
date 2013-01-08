package org.polytech.eit2.projet;

import java.util.HashMap;

public class Document {
	
	private HashMap <String, Double> TF;
	private HashMap<String, Double> TFIDF;
	private HashMap <String, Integer> wordOcc;
	private Integer nbMots;
	
	public HashMap<String, Double> getTF() {
		return TF;
	}

	public void setTF(HashMap<String, Double> tF) {
		TF = tF;
	}

	public HashMap<String, Integer> getWordOcc() {
		return wordOcc;
	}

	public void setWordOcc(HashMap<String, Integer> wordOcc) {
		this.wordOcc = wordOcc;
	}

	public Integer getNbMots() {
		return nbMots;
	}

	public void setNbMots(Integer nbMots) {
		this.nbMots = nbMots;
	}

	public HashMap<String, Double> getTFIDF() {
		return TFIDF;
	}

	public void setTFIDF(HashMap<String, Double> tFIDF) {
		TFIDF = tFIDF;
	}

	public Document (){
		TF = new HashMap <String, Double>();
		TFIDF = new HashMap<String,Double>();
		wordOcc = new HashMap <String, Integer>();
	}
	
	public void updateTF(){
		for(String word : this.wordOcc.keySet()){
			if(this.TF.containsKey(word)){
				this.TF.remove(word);
			}
			this.TF.put(word, (double)this.wordOcc.get(word)/nbMots);
		}
	}
	
	public void updateTfIdf(String word, Double IDF){
		if(this.TFIDF.containsKey(word)){
			this.TFIDF.remove(word);
		}
		this.TFIDF.put(word, IDF*this.TF.get(word));
	}
	
	public void KMeanAlgorithm(){
		
	}

}
