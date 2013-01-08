package org.polytech.eit2.projet;

import java.util.HashMap;
import java.util.TreeMap;

/**
 * Cette classe represente un dictionnaire (mot -> frequence) et comptabilise
 * le nombre de mots totals, le nombre de mots distincts, et le nombre de tweet recenses
 * @author matmotherbrain
 *
 */
public class Dictionnary {

	private TreeMap<String,Integer> dictionary; // Occurrence de chaque mot
	private Integer totalWords;
	private Integer differentWords;
	private Integer nbTweets;
	
	public Dictionnary(){
		dictionary = new TreeMap<String, Integer>();
		totalWords = differentWords = nbTweets = 0;
	}
	
	/**
	 * Ajoute un mot a la HashMap ou met a l'entree existante correspondante
	 *  et met a jour le nombre total de mots
	 * et si besoin le nombre de mots distincts
	 * @param w : le mot ˆ ajouter
	 */
	public void addWord(String w){
		int n = 1;
		if(dictionary.containsKey(w)){
			n = dictionary.get(w) + 1;
			dictionary.remove(w);
		}else
			differentWords++;
		totalWords++;
		dictionary.put(w, n);
	}
	
	/**
	 * Cette classe utilise la classe Value comparator pour classer les mots du dictionnaire
	 * de la frequence la plus haute a la plus basse pour ainsi recuperer le top 20
	 * @return une hashMap contenant les 20 mots les plus frequents du dictionnaire
	 */
	public HashMap<String, Integer> getTop20Frequency() {
		HashMap<String, Integer> top20Frequency = new HashMap<String, Integer>();
		ValueComparator vc = new ValueComparator(this.dictionary);
		TreeMap<String, Integer> sortedByValue = new TreeMap<String, Integer>(vc);
		sortedByValue.putAll(dictionary);
		int n = 0;
		for (String w : sortedByValue.keySet()){
			if (n>=20)
				break;
			top20Frequency.put(w, this.dictionary.get(w));
			n++;
		}
		return top20Frequency;
	}
	
	/**
	 * Cette classe utilise la classe Value comparator pour classer les mots du dictionnaire
	 * de la frequence la plus haute a la plus basse pour ainsi recuperer les 20 mots les moins frequents
	 * @return une hashMap contenant les 20 mots les plus frequents du dictionnaire
	 */
	public HashMap<String, Integer> getLess20Frequency() {
		HashMap<String, Integer> top20Frequency = new HashMap<String, Integer>();
		ValueComparator vc = new ValueComparator(this.dictionary);
		TreeMap<String, Integer> sortedByValue = new TreeMap<String, Integer>(vc);
		sortedByValue.putAll(dictionary);
		int n = 0;
		for (String w : sortedByValue.descendingKeySet()){
			if (n>=20)
				break;
			top20Frequency.put(w, this.dictionary.get(w));
			n++;
		}
		return top20Frequency;
	}
	
	/**
	 * 
	 * @param word : le mot recherche
	 * @return la frequence du mot dans le dictionnaire.
	 * On choisit de ne pas retourner 0 si un mot n'est pas present
	 * au cas ou un tweet d'une polarite contienne un mot n'etant pas dans
	 * le fichier d'entrainement. Ainsi la probabilite est fortement reduite
	 * mais non nulle
	 */
	public double wordFrequency(String word){
		if (!this.dictionary.containsKey(word))
			return 0.000000001;
		else
			return this.dictionary.get(word)/(double)this.getTotalWords();
	}
	
	/**
	 * Permet de calculer le nombre d'apparition moyen par tweet
	 * d'un mot dans une polarite ou categorie concernee
	 * @param word
	 * @return le nombre d'appartion moyen d'un mot par tweet
	 */
	public double bernouilli (String word){
		double beta;
		if(this.dictionary.containsKey(word))
			beta = (double)this.dictionary.get(word)/this.nbTweets;
		else
			beta = 0.000000001/this.nbTweets;
//		if(word.contentEquals("microsoft")){
//			System.out.println("Beta for microsoft = "+beta);
//		}
		return beta;
	}

	public TreeMap<String, Integer> getDictionary() {
		return dictionary;
	}

	public void setDictionary(TreeMap<String, Integer> dictionary) {
		this.dictionary = dictionary;
	}

	public Integer getTotalWords() {
		return totalWords;
	}

	public void setTotalWords(Integer totalWords) {
		this.totalWords = totalWords;
	}

	public Integer getDifferentWords() {
		return differentWords;
	}

	public void setDifferentWords(Integer differentWords) {
		this.differentWords = differentWords;
	}

	public Integer getNbTweets() {
		return nbTweets;
	}

	public void setNbTweets(Integer nbTweets) {
		this.nbTweets = nbTweets;
	}
	
}
