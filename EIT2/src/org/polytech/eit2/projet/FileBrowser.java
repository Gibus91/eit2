package org.polytech.eit2.projet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class FileBrowser {

	private String directoryName;
	private File directory;
	private HashMap<String, Integer> dictionnary;
	private HashMap<String, Integer> spam;
	private HashMap<String, Integer> ham;
	private Integer nbSpamFiles;
	private Integer nbHamFiles;

	public String getDirectoryName() {
		return directoryName;
	}

	public void setDirectoryName(String directoryName) {
		this.directoryName = directoryName;
	}

	public File getDirectory() {
		return directory;
	}

	public void setDirectory(File directory) {
		this.directory = directory;
	}

	public HashMap<String, Integer> getDictionnary() {
		return dictionnary;
	}

	public void setDictionnary(HashMap<String, Integer> dictionnary) {
		this.dictionnary = dictionnary;
	}

	public HashMap<String, Integer> getSpam() {
		return spam;
	}

	public void setSpam(HashMap<String, Integer> spam) {
		this.spam = spam;
	}

	public HashMap<String, Integer> getHam() {
		return ham;
	}

	public void setHam(HashMap<String, Integer> ham) {
		this.ham = ham;
	}

	public Integer getNbSpamFiles() {
		return nbSpamFiles;
	}

	public void setNbSpamFiles(Integer nbSpamFiles) {
		this.nbSpamFiles = nbSpamFiles;
	}

	public Integer getNbHamFiles() {
		return nbHamFiles;
	}

	public void setNbHamFiles(Integer nbHamFiles) {
		this.nbHamFiles = nbHamFiles;
	}

	public FileBrowser() {
		this.directoryName = "/Users/matmotherbrain/Cours_Inge5/EIT/TP1/lingspam/bare/part1/";
		this.directory = new File(directoryName);
		this.dictionnary = new HashMap<String, Integer>();
		this.spam = new HashMap<String, Integer>();
		this.ham = new HashMap<String, Integer>();
		this.nbHamFiles = 0;
		this.nbSpamFiles = 0;
	}

	public double BernouilliSpamKnowingWord(String word) {
		double beta;
		if (this.spam.containsKey(word))
			beta = (double) this.spam.get(word) / this.nbSpamFiles;
		else
			beta = 0.01 / this.nbSpamFiles;
		return beta;
	}

	public double BernouilliHamKnowingWord(String word) {
		double beta;
		if (this.ham.containsKey(word))
			beta = (double) this.ham.get(word) / this.nbHamFiles;
		else
			beta = 0.01 / this.nbHamFiles;
		return beta;
	}

	public boolean isASpam(String FileName) {
		double probaHam = 0;
		double probaSpam = 0;
		try {
			InputStream ips;
			ArrayList<String> listWordFile = new ArrayList<String>();
			ips = new FileInputStream(FileName);
			InputStreamReader ipsr = new InputStreamReader(ips);
			BufferedReader br = new BufferedReader(ipsr);
			String[] words;
			String line;
			while ((line = br.readLine()) != null) {
				words = line.split(" ");
				for (String word : words) {
					if (!listWordFile.contains(word)) {
						listWordFile.add(word);
					}
				}
			}
			br.close();
			for (String word : this.dictionnary.keySet()) {
				if (listWordFile.contains(word)) {
					probaHam += Math.log10(this.BernouilliHamKnowingWord(word));
					probaSpam += Math.log10(this
							.BernouilliSpamKnowingWord(word));
				} else {
					probaHam += Math.log10(1 - this
							.BernouilliHamKnowingWord(word));
					probaSpam += Math.log10(1 - this
							.BernouilliSpamKnowingWord(word));
				}
			}
			// double probaH = probaHam/(probaHam + probaSpam);
			// double probaS = probaSpam/(probaHam + probaSpam);
			// System.out.println(FileName+"\tham : "+probaH+"\tspam : "+probaS);
			if (probaSpam > probaHam)
				return true;
			else
				return false;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	public void countOccurencePerType() {
		InputStream ips;
		if (this.directory.isDirectory()) {
			String s[] = this.directory.list();
			for (int i = 0; i < s.length; i++) {
				try {
					String FileName = directoryName + s[i];
					if (FileName.endsWith(".txt")) {
						if (FileName.contains("spmsg"))
							this.nbSpamFiles++;
						else
							this.nbHamFiles++;
						ArrayList<String> listWordFile = new ArrayList<String>();
						ips = new FileInputStream(FileName);
						InputStreamReader ipsr = new InputStreamReader(ips);
						BufferedReader br = new BufferedReader(ipsr);
						String[] words;
						String line;
						while ((line = br.readLine()) != null) {
							words = line.split(" ");
							for (String word : words) {
								int n = 1;
								if (!listWordFile.contains(word)) {
									listWordFile.add(word);
									if (FileName.contains("spmsg")) {
										if (this.spam.containsKey(word)) {
											n = this.spam.get(word) + 1;
											this.spam.remove(word);
										}
										this.spam.put(word, n);
									} else {
										if (this.ham.containsKey(word)) {
											n = this.ham.get(word) + 1;
											this.ham.remove(word);
										}
										this.ham.put(word, n);
									}
								}
							}
						}
						br.close();
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
	}

	public void constructDictionnary() {
		InputStream ips;
		int n = 0;
		if (this.directory.isDirectory()) {
			String s[] = this.directory.list();
			for (int i = 0; i < s.length; i++) {
				try {
					String FileName = directoryName + s[i];
					if (FileName.endsWith(".txt")) {
						ips = new FileInputStream(FileName);
						InputStreamReader ipsr = new InputStreamReader(ips);
						BufferedReader br = new BufferedReader(ipsr);
						String[] words;
						String line;
						while ((line = br.readLine()) != null) {
							words = line.split(" ");
							for (String word : words) {
								if (!this.dictionnary.containsKey(word)) {
									// n = this.dictionnary.get(word) + 1;
									// this.dictionnary.remove(word);
									this.dictionnary.put(word, n);
									n++;
								}
							}
						}
						br.close();
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

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		FileBrowser myMain = new FileBrowser();
		myMain.constructDictionnary();
		myMain.countOccurencePerType();
		// System.out.println(myMain.getDictionnary().toString());
		// System.out.println("\nHam : "+myMain.getNbHamFiles());
		// System.out.println(myMain.getHam().toString());
		// System.out.println("\nSpam : "+myMain.getNbSpamFiles());
		// System.out.println(myMain.getSpam().toString());
		// for (String w : myMain.getDictionnary().keySet()){
		// System.out.print(w+"\t");
		// System.out.print(myMain.BernouilliSpamKnowingWord(w)+"\t");
		// System.out.println(myMain.BernouilliHamKnowingWord(w));
		// }
		int nbError = 0;
		int nbCorrect = 0;
		File myDirectory = new File(
				"/Users/matmotherbrain/Cours_Inge5/EIT/TP1/lingspam/bare/part6/");
		if (myDirectory.isDirectory()) {
			String s[] = myDirectory.list();
			for (int i = 0; i < s.length; i++) {
				boolean spam = myMain.isASpam(myDirectory.getAbsolutePath()
						+ "/" + s[i]);
				// System.out.println(s[i]+"\teval = "+spam+"\treal = "+s[i].startsWith("spmsg"));
				if (spam == s[i].startsWith("spmsg"))
					nbCorrect++;
				else
					nbError++;
			}
			System.out.println("correct = " + nbCorrect + "\terrors = "
					+ nbError + "\npourcentage d'erreurs = " + 100
					* (double) nbError / nbCorrect + "%");
		}
		// String pathTest = "5-1298msg1.txt";
		// myMain.isASpam(pathTest);
	}

}
