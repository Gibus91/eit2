package org.polytech.eit2.projet;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Categorie {

	private int nbMotTotal;
	private int nbMotDifferent;
	private float tailleMoyenne;
	private HashMap<String, Integer> listeMotOcc;
	private ArrayList<String> listeMotPopulaire;

	public Categorie() {
		this.listeMotPopulaire = new ArrayList<String>();
		this.listeMotOcc = new HashMap<String, Integer>();
	}

	public int getNbMotTotal() {
		return nbMotTotal;
	}

	public void setNbMotTotal(int nbMotTotal) {
		this.nbMotTotal = nbMotTotal;
	}

	public int getNbMotDifferent() {
		return nbMotDifferent;
	}

	public void setNbMotDifferent(int nbMotDifferent) {
		this.nbMotDifferent = nbMotDifferent;
	}

	public float getTailleMoyenne() {
		return tailleMoyenne;
	}

	public void setTailleMoyenne(float tailleMoyenne) {
		this.tailleMoyenne = tailleMoyenne;
	}

	public ArrayList<String> getListeMotPopulaire() {
		return listeMotPopulaire;
	}

	public void setListeMotPopulaire(ArrayList<String> listeMotPopulaire) {
		this.listeMotPopulaire = listeMotPopulaire;
	}

	public Map<String, Integer> getListeMotOcc() {
		return listeMotOcc;
	}

	public void setListeMotOcc(HashMap<String, Integer> listeMotOcc) {
		this.listeMotOcc = listeMotOcc;
	}

	public void constructDictionnary(String line) {

		String[] words;
		words = line.split(" ");
		for (String word : words) {

			int m = 1;
			if (this.getListeMotOcc().containsKey(word)) {
				m = this.getListeMotOcc().get(word) + 1;
				this.getListeMotOcc().remove(word);
			}
			this.getListeMotOcc().put(word, m);
		}

		// System.out.println(this.getIDF().toString());
	}

	public static void main(String[] argv) {
		Categorie irrelevant = new Categorie();

		InputStream ips;
		String info[];
		String FileName = "./twitter/train.txt";
		if (FileName.endsWith(".txt")) {
			try {
				ips = new FileInputStream(FileName);

				InputStreamReader ipsr = new InputStreamReader(ips);
				BufferedReader br = new BufferedReader(ipsr);

				String line;
				try {
					while ((line = br.readLine()) != null) {
						
						if (line.contains("irrelevant"))
							irrelevant.constructDictionnary(line);
					}
					br.close();
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		for (String word : irrelevant.getListeMotOcc().keySet()) {
			System.out.println("Mot : " + word + " Nb occurence : "+ irrelevant.getListeMotOcc().get(word));
		}

	}

}
