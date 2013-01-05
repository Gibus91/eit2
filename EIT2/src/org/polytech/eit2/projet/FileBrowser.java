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
//	private File tweet;
	private Dictionnary dictionnary;
	private HashMap<String,Integer> top20Frequency;
	private ArrayList<TweetCategory> categories;
//	private Integer nbIrrelevantTweets;
//	private Integer nbNeutralTweets;
//	private Integer nbPostiveTweets;
//	private Integer nbNegativeTweets;
	
	public FileBrowser(String filePath) {
		this.filePath = filePath;
//		tweet = new File(filePath);
		dictionnary = new Dictionnary();
		top20Frequency = new HashMap<String, Integer>();
		categories = new ArrayList<TweetCategory>();
//		nbIrrelevantTweets = nbNeutralTweets = nbPostiveTweets = nbNegativeTweets = 0;
	}
	
	public void setDictionaries(){
		InputStream ips;
		try {
			ips = new FileInputStream(filePath);
			InputStreamReader ipsr=new InputStreamReader(ips);
			@SuppressWarnings("resource")
			BufferedReader br =new BufferedReader(ipsr);
			String[] words;
			String line;
			while ((line=br.readLine())!=null){
				this.dictionnary.setNbTweets(this.dictionnary.getNbTweets() + 1);
				words = line.substring(line.indexOf(')') + 1).split("[\\s\\!\"#&'()*+,-\\./:;<=>\\?\\[\\]^_`{|}~§@]+");
				String categoryName = line.substring(line.indexOf(',')+1, line.indexOf(')'));
				String categoryType = line.substring(1, line.indexOf(','));
				TweetCategory currentCategory = null;
				for (TweetCategory tc : categories){
					if(tc.getCategory().contentEquals(categoryName.toLowerCase())){
						currentCategory = tc;
					}
				}
				if (currentCategory == null){
					currentCategory = new TweetCategory(categoryName.toLowerCase());
				}
				currentCategory.setNbTweets(categoryType.toLowerCase());
				for (String word : words){
					String w = word.toLowerCase();
					this.dictionnary.addWord(w);
					currentCategory.addWord(w, categoryType.toLowerCase());
				}
			}
			this.top20Frequency = dictionnary.getTop20Frequency();
			System.out.println("20 most frequent words : \n"+ this.top20Frequency.toString());
			System.out.println("Total words = "+ this.dictionnary.getTotalWords() + "\nNumber of differents words = "+ this.dictionnary.getDifferentWords() +"\nAverage number of words in a tweet = "+ (double)this.dictionnary.getTotalWords()/this.dictionnary.getNbTweets());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
}
