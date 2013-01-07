package org.polytech.eit2.projet;

import java.util.ArrayList;
import java.util.HashMap;

public class TweetCategory {
	private String category;
	private Dictionnary categoryDictionary;
	private Dictionnary irrelevant; // Occurence de chaque mot dans les tweets
									// irrelevant
	private Dictionnary neutral; // Occurence de chaque mot dans les tweets
									// neutral
	private Dictionnary positive; // Occurence de chaque mot dans les tweets
									// positive
	private Dictionnary negative; // Occurence de chaque mot dans les tweets
									// negative

	public TweetCategory(String category) {
		this.category = category;
		categoryDictionary = new Dictionnary();
		irrelevant = new Dictionnary();
		neutral = new Dictionnary();
		positive = new Dictionnary();
		negative = new Dictionnary();
	}

	public void addWord(String w, String type) {
		this.categoryDictionary.addWord(w);
		if (type.contentEquals("irrelevant"))
			this.irrelevant.addWord(w);
		else if (type.contentEquals("neutral"))
			this.neutral.addWord(w);
		else if (type.contentEquals("positive"))
			this.positive.addWord(w);
		else if (type.contentEquals("negative"))
			this.negative.addWord(w);

	}

	public void setNbTweets(String type) {
		this.categoryDictionary.setNbTweets(this.categoryDictionary
				.getNbTweets() + 1);
		if (type.contentEquals("irrelevant"))
			this.irrelevant.setNbTweets(this.irrelevant.getNbTweets() + 1);
		else if (type.contentEquals("neutral"))
			this.neutral.setNbTweets(this.neutral.getNbTweets() + 1);
		else if (type.contentEquals("positive"))
			this.positive.setNbTweets(this.positive.getNbTweets() + 1);
		else if (type.contentEquals("negative"))
			this.negative.setNbTweets(this.negative.getNbTweets() + 1);
	}

	public String getCategory() {
		return category;
	}

	public String multiNommiale(String words[]) {
		String highestProbability = "";
		double positiveProbability = 1.0;
		double negativeProbability = 1.0;
		double irrelevantProbability = 1.0;
		double neutralProbability = 1.0;
		for (String word : words) {
			positiveProbability *= positive.wordFrequency(word.toLowerCase());
			negativeProbability *= negative.wordFrequency(word.toLowerCase());
			irrelevantProbability *= irrelevant.wordFrequency(word
					.toLowerCase());
			neutralProbability *= neutral.wordFrequency(word.toLowerCase());
		}
		double probaMax = Math.max(
				Math.max(positiveProbability, negativeProbability),
				Math.max(irrelevantProbability, neutralProbability));
		if (probaMax == irrelevantProbability)
			highestProbability = "irrelevant";
		else if (probaMax == neutralProbability
				|| Math.abs(positiveProbability - negativeProbability)
						/ Math.max(positiveProbability, negativeProbability) <= 0.05)
			highestProbability = "neutral";
		else if (probaMax == positiveProbability)
			highestProbability = "positive";
		else if (probaMax == negativeProbability)
			highestProbability = "negative";
		return highestProbability;
	}

	public String biNommiale(String words[]) {
		String highestProbability = "";
		double positiveProbability = 1.0;
		double negativeProbability = 1.0;
		double irrelevantProbability = 1.0;
		double neutralProbability = 1.0;
		for (String word : words) {
			positiveProbability *= positive.wordFrequency(word.toLowerCase());
			negativeProbability *= negative.wordFrequency(word.toLowerCase());
			irrelevantProbability *= irrelevant.wordFrequency(word
					.toLowerCase());
			neutralProbability *= neutral.wordFrequency(word.toLowerCase());
		}
		double probaMax = Math.max(
				Math.max(positiveProbability, negativeProbability),
				Math.max(irrelevantProbability, neutralProbability));
		if (Math.max(Math.max(negativeProbability, positiveProbability),
				Math.max(irrelevantProbability, neutralProbability)) == irrelevantProbability) {
			highestProbability = "irrelevant";
		} else if (Math.min(positiveProbability, negativeProbability)
				/ Math.max(positiveProbability, negativeProbability) >= 0.9) {
			highestProbability = "neutral";
		} else if (Math.max(positiveProbability, negativeProbability) == positiveProbability) {
			highestProbability = "positive";
		} else {
			highestProbability = "negative";
		}
		return highestProbability;
	}
	
}
