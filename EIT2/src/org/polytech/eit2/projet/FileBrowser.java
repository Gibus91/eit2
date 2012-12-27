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


public class FileBrowser {
	
	private String directoryName;
	private File directory;
	private HashMap<String,Integer> dictionnary;
	private ArrayList<Document> documents;
	private HashMap<String, Double> IDF;
	private Integer nbDocs;
	
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

	public ArrayList<Document> getDocuments() {
		return documents;
	}

	public void setDocuments(ArrayList<Document> documents) {
		this.documents = documents;
	}

	public HashMap<String, Double> getIDF() {
		return IDF;
	}

	public void setIDF(HashMap<String, Double> iDF) {
		IDF = iDF;
	}

	public Integer getNbDocs() {
		return nbDocs;
	}

	public void setNbDocs(Integer nbDocs) {
		this.nbDocs = nbDocs;
	}

	public FileBrowser(){
		this.directoryName = "/Users/matmotherbrain/Cours_Inge5/EIT/TP2/discours/";
		this.directory = new File (directoryName);
		this.dictionnary = new HashMap<String,Integer>();
		this.documents = new ArrayList<Document>();
		this.IDF = new HashMap<String, Double>();
		this.nbDocs = 0;
	}
	
	public void constructDictionnary(){
		InputStream ips;
		if (this.directory.isDirectory()){
			String s[] = this.directory.list();
			for (int i=0; i<s.length; i++){
				try {
					String FileName = directoryName + s[i];
					if (FileName.endsWith(".txt")){
						this.nbDocs ++;
						Document currentDoc = new Document();
						int nbMots = 0;
						ips = new FileInputStream(FileName);
						InputStreamReader ipsr=new InputStreamReader(ips);
						BufferedReader br=new BufferedReader(ipsr);
						String[] words;
						String line;
						while ((line=br.readLine())!=null){
							words = line.split(" ") ;
							for(String word : words){
								int n = 1;
								int m = 1;
								nbMots ++;
								if (currentDoc.getWordOcc().containsKey(word)){
									m = currentDoc.getWordOcc().get(word) + 1;
									currentDoc.getWordOcc().remove(word);
								}
								else if (this.dictionnary.containsKey(word)){
									n = this.dictionnary.get(word) + 1;
									this.dictionnary.remove(word);
								}	
								this.dictionnary.put(word, n);
								currentDoc.getWordOcc().put(word,m);
							}
							this.documents.add(currentDoc);
						}
						br.close();
						currentDoc.setNbMots(nbMots);
						currentDoc.updateTF();
					}
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			this.updateIDF();
//			System.out.println(this.getIDF().toString());
		}
		
	}
	
	public void updateIDF(){
		for(String word : this.dictionnary.keySet()){
			this.IDF.put(word, Math.log10(this.nbDocs/this.dictionnary.get(word)));
//			System.out.println(word+" : "+Math.log10(this.nbDocs/this.dictionnary.get(word)));
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		FileBrowser myMain = new FileBrowser();
		myMain.constructDictionnary();
//		System.out.println(myMain.getDictionnary().toString());
		System.out.println(myMain.getIDF().toString());
		int i = 0;
		for(Document doc : myMain.getDocuments()){
			System.out.println("***** DOC : "+i+" *****");
			for (String word : doc.getTF().keySet()){
				doc.updateTfIdf(word, doc.getTF().get(word));
				System.out.println(word+" : "+doc.getTFIDF().get(word));
			}
			i++;
		}
		
		
//		File myDirectory = new File("/Users/matmotherbrain/Cours_Inge5/EIT/TP1/lingspam/bare/part6/");
//		if (myDirectory.isDirectory()){
//			String s[] = myDirectory.list();
//			for (int i=0; i<s.length; i++){
//				
//			}
//		}
	}

}

