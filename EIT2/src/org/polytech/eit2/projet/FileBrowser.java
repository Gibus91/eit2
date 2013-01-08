package org.polytech.eit2.projet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

public class FileBrowser {
	private String filePath;
	private Dictionnary dictionnary;
	private HashMap<String, Integer> top20Frequency;
	private HashMap<String, Integer> top20MostIrrelevant;
	private HashMap<String, Integer> top20MostNeutral;
	private HashMap<String, Integer> top20MostPositive;
	private HashMap<String, Integer> top20MostNegative;
	private HashMap<String, Integer> top20LeastIrrelevant;
	private HashMap<String, Integer> top20LeastNeutral;
	private HashMap<String, Integer> top20LeastPositive;
	private HashMap<String, Integer> top20LeastNegative;
	private ArrayList<TweetCategory> categories;
	private HashMap<Character, ArrayList<String>> tokenWords;

	public FileBrowser(String filePath) {
		this.filePath = filePath;
		dictionnary = new Dictionnary();
		top20Frequency = new HashMap<String, Integer>();
		top20MostIrrelevant = new HashMap<String, Integer>();
		top20MostNeutral = new HashMap<String, Integer>();
		top20MostPositive = new HashMap<String, Integer>();
		top20MostNegative = new HashMap<String, Integer>();
		top20LeastIrrelevant = new HashMap<String, Integer>();
		top20LeastNeutral = new HashMap<String, Integer>();
		top20LeastPositive = new HashMap<String, Integer>();
		top20LeastNegative = new HashMap<String, Integer>();
		categories = new ArrayList<TweetCategory>();
		tokenWords = new HashMap<Character, ArrayList<String>>();
	}

	public void setDictionaries() {
		InputStream ips;
		try {
			ips = new FileInputStream(filePath);
			InputStreamReader ipsr = new InputStreamReader(ips);
			BufferedReader br = new BufferedReader(ipsr);
			String[] words;
			String line;

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
			br.close();
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

	public void testMultinomial(String destFile) {
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

			File outFile = new File(destFile);
			if (outFile.exists())
				outFile.delete();
			FileOutputStream fos = new FileOutputStream(outFile);
			PrintWriter out = new PrintWriter(fos);
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

				guess = currentCategory.multinomial(words);
				out.println(guess);
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

			afficherResultats(nbFalse, posWhenNeg, neuWhenIrr,
					posNegWhenIrrNeu, irrNeuWhenPosNeg, matrice);

			br.close();
			ips.close();
			out.flush();
			out.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void testBiNommialeBernouilli(String destFile) {
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

			File outFile = new File(destFile);
			if (outFile.exists())
				outFile.delete();
			FileOutputStream fos = new FileOutputStream(outFile);
			PrintWriter out = new PrintWriter(fos);

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

				guess = currentCategory.binomialBernouilli(words);
				out.println(guess);

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
			afficherResultats(nbFalse, posWhenNeg, neuWhenIrr,
					posNegWhenIrrNeu, irrNeuWhenPosNeg, matrice);

			ips.close();
			br.close();
			out.flush();
			out.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
			br.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void top20Significance(String polarite){
		HashMap<String, Integer> top20 = new HashMap<String, Integer>();
		HashMap<String, Integer> less20 = new HashMap<String, Integer>();
		
		for(TweetCategory c : this.categories){
			Dictionnary tmp;
			if(polarite.contentEquals("irrelevant"))
				tmp = c.getIrrelevant();
			else if(polarite.contentEquals("neutral"))
				tmp = c.getNeutral();
			else if(polarite.contentEquals("positive"))
				tmp = c.getPositive();
			else if(polarite.contentEquals("negative"))
				tmp = c.getNegative();
			else
				tmp = new Dictionnary();
			HashMap <String, Integer> topReturn = tmp.getTop20Frequency();
			HashMap <String, Integer> lessReturn = tmp.getLess20Frequency();
			
			for(String word : topReturn.keySet()){
				if(!top20.containsKey(word))
					top20.put(word, topReturn.get(word));
				else if (top20.get(word) < topReturn.get(word)){
					top20.remove(word);
					top20.put(word, topReturn.get(word));					
				}
			}
			for(String word : lessReturn.keySet()){
				if(!less20.containsKey(word)){
					less20.put(word, lessReturn.get(word));
				}
				else if (less20.get(word) < lessReturn.get(word)){
					less20.remove(word);
					less20.put(word, lessReturn.get(word));					
				}
			}
		}

		ValueComparator vcTop = new ValueComparator(top20);
		ValueComparator vcLeast = new ValueComparator(less20);
		TreeMap<String, Integer> tmpTop = new TreeMap<String, Integer>(vcTop);
		TreeMap<String, Integer> tmpLeast = new TreeMap<String, Integer>(vcLeast);
		tmpTop.putAll(top20);
		tmpLeast.putAll(less20);
		HashMap<String, Integer> bufferTmp = new HashMap<String, Integer>();
		HashMap<String, Integer> bufferTmp2 = new HashMap<String, Integer>();
		int n = 0;
		for(String w : tmpTop.keySet()){
			if(n == 20){
				break;
			}
			bufferTmp.put(w, top20.get(w));
			n++;
		}
		n = 0;
		for(String w : tmpLeast.descendingKeySet()){
			if(n == 20){
				break;
			}
			bufferTmp2.put(w, less20.get(w));
			n++;
		}
		if(polarite.contentEquals("irrelevant")){
			this.top20MostIrrelevant = bufferTmp;
			this.top20LeastIrrelevant = bufferTmp2;
		}else if(polarite.contentEquals("neutral")){
			this.top20MostNeutral = bufferTmp;
			this.top20LeastNeutral = bufferTmp2;
		}else if(polarite.contentEquals("positive")){
			this.top20MostPositive = bufferTmp2;
			this.top20LeastPositive = bufferTmp;
		}else if(polarite.contentEquals("negative")){
			this.top20MostNegative = bufferTmp;
			this.top20LeastNegative = bufferTmp2;
		}
		
	}
	
	public void setAllTops(){
		top20Significance("irrelevant");
		top20Significance("neutral");
		top20Significance("positive");
		top20Significance("negative");
		ArrayList<String> toDelete = new ArrayList<String>();
		for(String wordIrre : this.top20MostIrrelevant.keySet()){
			boolean foundWord = false;
			if(this.top20MostNeutral.containsKey(wordIrre)){
					this.top20MostNeutral.remove(wordIrre);
					foundWord=true;
			}
			if(this.top20MostPositive.containsKey(wordIrre)){
					this.top20MostPositive.remove(wordIrre);
					foundWord=true;
			}
			if(this.top20MostNegative.containsKey(wordIrre)){
					this.top20MostNegative.remove(wordIrre);
					foundWord=true;
			}
			if(foundWord){
				toDelete.add(wordIrre);
			}
		}
		for(String word : toDelete){
			this.top20MostIrrelevant.remove(word);
		}
		toDelete = new ArrayList<String>();

		for(String wordNeut : this.top20MostNeutral.keySet()){
			boolean foundWord = false;
			if(this.top20MostPositive.containsKey(wordNeut)){
					this.top20MostPositive.remove(wordNeut);
					foundWord=true;
			}
			if(this.top20MostNegative.containsKey(wordNeut)){
					this.top20MostNegative.remove(wordNeut);
					foundWord=true;
			}
			if(foundWord){
				toDelete.add(wordNeut);
			}
		}
		for(String word : toDelete){
			this.top20MostNeutral.remove(word);
		}
		toDelete = new ArrayList<String>();

		for(String wordPos : this.top20MostPositive.keySet()){
			boolean foundWord = false;
			if(this.top20MostNegative.containsKey(wordPos)){
					this.top20MostNegative.remove(wordPos);
					foundWord=true;
			}
			if(foundWord){
				toDelete.add(wordPos);
			}
		}
		for(String word : toDelete){
			this.top20MostPositive.remove(word);
		}
		toDelete = new ArrayList<String>();
		

		for(String wordIrre : this.top20LeastIrrelevant.keySet()){
			boolean foundWord = false;
			if(this.top20LeastNeutral.containsKey(wordIrre)){
					this.top20LeastNeutral.remove(wordIrre);
					foundWord=true;
			}
			if(this.top20LeastPositive.containsKey(wordIrre)){
					this.top20LeastPositive.remove(wordIrre);
					foundWord=true;
			}
			if(this.top20LeastNegative.containsKey(wordIrre)){
					this.top20LeastNegative.remove(wordIrre);
					foundWord=true;
			}
			if(foundWord){
				toDelete.add(wordIrre);
			}
		}
		for(String word : toDelete){
			this.top20LeastIrrelevant.remove(word);
		}
		toDelete = new ArrayList<String>();

		for(String wordNeut : this.top20LeastNeutral.keySet()){
			boolean foundWord = false;
			if(this.top20LeastPositive.containsKey(wordNeut)){
					this.top20LeastPositive.remove(wordNeut);
					foundWord=true;
			}
			if(this.top20LeastNegative.containsKey(wordNeut)){
					this.top20LeastNegative.remove(wordNeut);
					foundWord=true;
			}
			if(foundWord){
				toDelete.add(wordNeut);
			}
		}
		for(String word : toDelete){
			this.top20LeastNeutral.remove(word);
		}
		toDelete = new ArrayList<String>();

		for(String wordPos : this.top20LeastPositive.keySet()){
			boolean foundWord = false;
			if(this.top20LeastNegative.containsKey(wordPos)){
					this.top20LeastNegative.remove(wordPos);
					foundWord=true;
			}
			if(foundWord){
				toDelete.add(wordPos);
			}
		}
		for(String word : toDelete){
			this.top20LeastPositive.remove(word);
		}
		System.out.println("\nTop most irrelevant : \n"+ this.top20MostIrrelevant.toString()+"\nTop least irrelevant : \n "+ this.top20LeastIrrelevant.toString()+"\n");
		System.out.println("\nTop most neutral : \n"+ this.top20MostNeutral.toString()+"\nTop least neutral : \n "+ this.top20LeastNeutral.toString()+"\n");
		System.out.println("\nTop most positive : \n"+ this.top20MostPositive.toString()+"\nTop least positive : \n "+ this.top20LeastPositive.toString()+"\n");
		System.out.println("\nTop most negative : \n"+ this.top20MostNegative.toString()+"\nTop least negative : \n "+ this.top20LeastNegative.toString()+"\n");
	}

	public void afficherResultats(int nbFalse, int posWhenNeg, int neuWhenIrr,
			int posNegWhenIrrNeu, int irrNeuWhenPosNeg, int matrice[][]) {
		double accuratePercentage = 0.0;
		accuratePercentage = (double) nbFalse / this.dictionnary.getNbTweets();
		System.out.println();
		System.out.println("false guesses = " + nbFalse + "\nTotal tweets = "
				+ this.dictionnary.getNbTweets() + "\nPercentage false = "
				+ accuratePercentage);
		System.out
				.println("\nPositive when Negative or Negative when Positive = "
						+ posWhenNeg
						+ "\tx 2\nIrrelevant when Neutral or Neutral when Irrelevant = "
						+ neuWhenIrr + "\tx 1");
		System.out.println("Positive or Negative when Neutral or Irrelevant = "
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
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public Dictionnary getDictionnary() {
		return dictionnary;
	}

	public void setDictionnary(Dictionnary dictionnary) {
		this.dictionnary = dictionnary;
	}

	public HashMap<String, Integer> getTop20Frequency() {
		return top20Frequency;
	}

	public void setTop20Frequency(HashMap<String, Integer> top20Frequency) {
		this.top20Frequency = top20Frequency;
	}

	public ArrayList<TweetCategory> getCategories() {
		return categories;
	}

	public void setCategories(ArrayList<TweetCategory> categories) {
		this.categories = categories;
	}

	public HashMap<Character, ArrayList<String>> getTokenWords() {
		return tokenWords;
	}

	public void setTokenWords(HashMap<Character, ArrayList<String>> tokenWords) {
		this.tokenWords = tokenWords;
	}

}
