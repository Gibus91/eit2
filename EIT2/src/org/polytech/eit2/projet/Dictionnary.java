package org.polytech.eit2.projet;

import java.util.HashMap;
import java.util.TreeMap;

public class Dictionnary {

	private TreeMap<String, Integer> dictionary; // Occurrence de chaque mot
	private Integer totalWords;
	private Integer differentWords;
	private Integer nbTweets;

	public Dictionnary() {
		dictionary = new TreeMap<String, Integer>();
		totalWords = differentWords = nbTweets = 0;
	}

	public void addWord(String w) {
		int n = 1;
		if (dictionary.containsKey(w)) {
			n = dictionary.get(w) + 1;
			dictionary.remove(w);
		} else
			differentWords++;
		totalWords++;
		dictionary.put(w, n);
	}

	public HashMap<String, Integer> getTop20Frequency() {
		HashMap<String, Integer> top20Frequency = new HashMap<String, Integer>();
		ValueComparator vc = new ValueComparator(this.dictionary);
		TreeMap<String, Integer> sortedByValue = new TreeMap<String, Integer>(
				vc);
		sortedByValue.putAll(dictionary);
		int n = 0;
		for (String w : sortedByValue.keySet()) {
			if (n >= 20)
				break;
			top20Frequency.put(w, this.dictionary.get(w));
			n++;
		}
		return top20Frequency;
	}

	public HashMap<String, Integer> getLess20Frequency() {
		HashMap<String, Integer> top20Frequency = new HashMap<String, Integer>();
		ValueComparator vc = new ValueComparator(this.dictionary);
		TreeMap<String, Integer> sortedByValue = new TreeMap<String, Integer>(
				vc);
		sortedByValue.putAll(dictionary);
		int n = 0;
		for (String w : sortedByValue.descendingKeySet()) {
			if (n >= 20)
				break;
			top20Frequency.put(w, this.dictionary.get(w));
			n++;
		}
		return top20Frequency;
	}

	public double wordFrequency(String word) {
		if (!this.dictionary.containsKey(word))
			return 0.000000001;
		else
			return this.dictionary.get(word) / (double) this.getTotalWords();
	}

	public double bernouilli(String word) {
		double beta;
		if (this.dictionary.containsKey(word))
			beta = (double) this.dictionary.get(word) / this.nbTweets;
		else
			beta = 0.000000001 / this.nbTweets;
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
