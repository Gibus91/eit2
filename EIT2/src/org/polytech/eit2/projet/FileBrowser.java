package org.polytech.eit2.projet;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

public class FileBrowser {
	private String filePath;
	// private File tweet;
	private Dictionnary dictionnary;
	private HashMap<String, Integer> top20Frequency;
	private ArrayList<TweetCategory> categories;
	private HashMap<Character, ArrayList<String>> tokenWords;

	// private Integer nbIrrelevantTweets;
	// private Integer nbNeutralTweets;
	// private Integer nbPostiveTweets;
	// private Integer nbNegativeTweets;

	public FileBrowser(String filePath) {
		this.filePath = filePath;
		// tweet = new File(filePath);
		dictionnary = new Dictionnary();
		top20Frequency = new HashMap<String, Integer>();
		categories = new ArrayList<TweetCategory>();
		tokenWords = new HashMap<Character, ArrayList<String>>();
		// nbIrrelevantTweets = nbNeutralTweets = nbPostiveTweets =
		// nbNegativeTweets = 0;
	}

	public void setDictionaries() {
		InputStream ips;
		try {
			ips = new FileInputStream(filePath);
			InputStreamReader ipsr = new InputStreamReader(ips);
			BufferedReader br = new BufferedReader(ipsr);
			String[] words;
			String line;
			int n = 0;
			while ((line = br.readLine()) != null) {
				this.dictionnary
						.setNbTweets(this.dictionnary.getNbTweets() + 1);
				words = line.substring(line.indexOf(')') + 1).split(
						"[\\s\\!\"#&'()*+,-\\./:;<=>\\?\\[\\]^_`{|}~§@]+");
				String categoryName = line.substring(line.indexOf(',') + 1,
						line.indexOf(')'));
				String categoryType = line.substring(1, line.indexOf(','));
				TweetCategory currentCategory = null;
				for (TweetCategory tc : categories) {
					if (tc.getCategory().contentEquals(
							categoryName.toLowerCase())) {
						currentCategory = tc;
					}
				}
				if (currentCategory == null) {
					currentCategory = new TweetCategory(
							categoryName.toLowerCase());
					categories.add(currentCategory);
				}
				currentCategory.setNbTweets(categoryType.toLowerCase());
				for (String word : words) {
					if (word.compareTo("") != 0) {
						String w = word.toLowerCase();
						boolean isTokenWord = false;
						char firstLetter = w.charAt(0);
						if (this.tokenWords.containsKey(firstLetter)) {
							for (String tWord : this.tokenWords
									.get(firstLetter)) {
								if (tWord.compareTo(w) == 0) {
									isTokenWord = true;
									break;
								}
							}
						}
						if (!isTokenWord) {
							this.dictionnary.addWord(w);
							currentCategory.addWord(w,
									categoryType.toLowerCase());
						}
					}
				}
			}
			this.top20Frequency = dictionnary.getTop20Frequency();
			System.out.println("20 most frequent words : \n"
					+ this.top20Frequency.toString());
			System.out.println("Total words = "
					+ this.dictionnary.getTotalWords()
					+ "\nNumber of differents words = "
					+ this.dictionnary.getDifferentWords()
					+ "\nAverage number of words in a tweet = "
					+ (double) this.dictionnary.getTotalWords()
					/ this.dictionnary.getNbTweets());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public double testMultinomial() {
		double accuratePercentage = 0.0;
		int nbFalse = 0;
		int posWhenNeg = 0;
		int neuWhenIrr = 0;
		int posNegWhenIrrNeu = 0;
		int irrNeuWhenPosNeg = 0;
		int[][] matrice = { { 0, 0, 0, 0 }, { 0, 0, 0, 0 }, { 0, 0, 0, 0 },
				{ 0, 0, 0, 0 } };
		InputStream ips;
		try {
			ips = new FileInputStream(filePath);
			InputStreamReader ipsr = new InputStreamReader(ips);
			BufferedReader br = new BufferedReader(ipsr);
			String[] words;
			String line;
			while ((line = br.readLine()) != null) {
				String guess;
				words = line.substring(line.indexOf(')') + 1).split(
						"[\\s\\!\"#&'()*+,-\\./:;<=>\\?\\[\\]^_`{|}~§@]+");
				String categoryName = line.substring(line.indexOf(',') + 1,
						line.indexOf(')'));
				String categoryType = line.substring(1, line.indexOf(','));
				TweetCategory currentCategory = null;
				for (TweetCategory tc : categories) {
					if (tc.getCategory().contentEquals(
							categoryName.toLowerCase())) {
						currentCategory = tc;
					}
				}
				if (currentCategory == null) {
					currentCategory = new TweetCategory(
							categoryName.toLowerCase());
				}

				guess = currentCategory.biNommiale(words);
				int guessIndex = -1;
				int actualIndex = -1;
				if (guess.contentEquals("positive"))
					guessIndex = 0;
				else if (guess.contentEquals("negative"))
					guessIndex = 1;
				else if (guess.contentEquals("neutral"))
					guessIndex = 2;
				else if (guess.contentEquals("irrelevant"))
					guessIndex = 3;
				if (categoryType.contentEquals("positive"))
					actualIndex = 0;
				else if (categoryType.contentEquals("negative"))
					actualIndex = 1;
				else if (categoryType.contentEquals("neutral"))
					actualIndex = 2;
				else if (categoryType.contentEquals("irrelevant"))
					actualIndex = 3;
				matrice[guessIndex][actualIndex]++;
				if (!guess.contentEquals(categoryType)) {
					nbFalse++;
					if (guess.contentEquals("positive")
							|| guess.contentEquals("negative")) {
						if (categoryType.contentEquals("positive")
								|| categoryType.contentEquals("negative"))
							posWhenNeg++;
						else
							posNegWhenIrrNeu++;
					} else if (guess.contains("irrelevant")
							|| guess.contains("neutral")) {
						if (categoryType.contentEquals("irrelevant")
								|| categoryType.contentEquals("neutral"))
							neuWhenIrr++;
						else
							irrNeuWhenPosNeg++;
					}
				}

			}
			accuratePercentage = (double) nbFalse
					/ this.dictionnary.getNbTweets();
			System.out.println();
			System.out.println("false guesses = " + nbFalse
					+ "\nTotal tweets = " + this.dictionnary.getNbTweets()
					+ "\nPercentage false = " + accuratePercentage);
			System.out
					.println("\nPositive when Negative or Negative when Positive = "
							+ posWhenNeg
							+ "\tx 2\nIrrelevant when Neutral or Neutral when Irrelevant = "
							+ neuWhenIrr + "\tx 1");
			System.out
					.println("Positive or Negative when Neutral or Irrelevant = "
							+ posNegWhenIrrNeu
							+ "\tx 3\nIrrelevant or Neutral when Positive or Negative = "
							+ irrNeuWhenPosNeg + "\tx 4");
			System.out.println("\tpos\tneg\tneu\tirr\n");
			for (int i = 0; i < 4; i++) {
				String lineInd = (i == 0) ? "pos" : (i == 1) ? "neg"
						: (i == 2) ? "neu" : "irr";
				System.out.print(lineInd);
				for (int j = 0; j < 4; j++) {
					System.out.print("\t" + matrice[i][j]);
				}
				System.out.println();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return accuratePercentage;
	}

	public void addTokenWords(String filePath) {
		InputStream ips;
		try {
			ips = new FileInputStream(filePath);
			InputStreamReader ipsr = new InputStreamReader(ips);
			BufferedReader br = new BufferedReader(ipsr);
			String line;
			while ((line = br.readLine()) != null) {
				char firstLetter = line.toLowerCase().charAt(0);
				if (!this.tokenWords.containsKey(firstLetter)) {
					ArrayList<String> newLetter = new ArrayList<String>();
					newLetter.add(line.toLowerCase());
					this.tokenWords.put(firstLetter, newLetter);
				} else {
					ArrayList<String> knownLetter = this.tokenWords
							.get(firstLetter);
					boolean found = false;
					for (String word : knownLetter) {
						if (word.compareTo(line) == 0) {
							found = true;
							break;
						}
					}
					if (!found) {
						knownLetter.add(line.toLowerCase());
					}
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
