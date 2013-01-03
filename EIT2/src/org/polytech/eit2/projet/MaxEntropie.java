package org.polytech.eit2.projet;

import java.io.File;
import java.util.HashMap;


public class MaxEntropie {
	private HashMap<String, Double> tetaSpam;
	private HashMap<String, Double> tetaHam;
	
	public MaxEntropie(){
		tetaSpam = new HashMap<String, Double>();
		tetaHam = new HashMap<String, Double>();
	}

	public void algoDiscriminant(){
		File myDirectory = new File("/Users/matmotherbrain/Cours_Inge5/EIT/TP1/lingspam/bare/part6/");
		if (myDirectory.isDirectory()){
			String s[] = myDirectory.list();
			HashMap <String, Boolean> read = new HashMap<String, Boolean>();
			for (int i = 0; i < s.length; i++){
				read.put(s[i], false);
			}
			int rand, n = 0;
			while (n<s.length){
				do{
					rand = (int)Math.random() % s.length;
				}while (read.get(s[rand]));
				
			}
			
		}
	}
}
