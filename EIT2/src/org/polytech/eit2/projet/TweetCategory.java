package org.polytech.eit2.projet;

/**
 * Cette classe représente une société 
 * et contient 5 dictionnaires (un général et un pour chaque polarité)
 * @author Mathieu Jouve et Jean-Baptiste Borel
 *
 */
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

	/**
	 * Cette methode appelee depuis file browser lors de la construction des dictionnaires,
	 * permet d'ajouter ou mettre un jour un mot dans la polarité correspondante au tweet en cours
	 * et dans le dictionnaire general de la société
	 * @param w = le mot a ajouter au dictionnaires
	 * @param type = la polarite concernee
	 */
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

	/**
	 * Cette methode appelee depuis file browser lors de la construction des dictionnaires,
	 * permet d'incrementer le nombre de tweets dans la polarité correspondante au tweet en cours
	 * et dans le dictionnaire general de la société
	 * @param type = la polarite concernee
	 */
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
	
	/**
	 * Cette methode permet d'effectuer un algorithme binomial sur un tweet :
	 * Pour chaque mot du dictionnaire général, on vérifie si celui-ci se trouve
	 * dans le tweet, on  ajoute à la probabilite correspondant à une polarite,
	 * log10(beta) et log10(1 - beta) dans le cas contraire. Le beta est calcule pour 
	 * chaque mot et pour chaque polarité dans le dictionnary correspondant (experience de Bernouilli)
	 * @param words : le tweet etudie
	 * @return la probabilite la plus haute ("irrelevant", "neutral", "positive" ou "negative")
	 */
	public String biNommialeBernouilli(String words[]) {
		String highestProbability = "";
		double positiveProbability = 0.0;
		double negativeProbability = 0.0;
		double irrelevantProbability = 0.0;
		double neutralProbability = 0.0;
		for (String word : this.categoryDictionary.getDictionary().keySet()) {
			if (!word.toLowerCase().contentEquals(category)) {
				boolean found = false;
				for (String w : words) {
					if (w.toLowerCase().contentEquals(word)) {
						found = true;
						break;
					}
				}
				
				if (found) {
					positiveProbability += Math.log10(this.positive
							.bernouilli(word));
					negativeProbability += Math.log10(this.negative
							.bernouilli(word));
					irrelevantProbability += Math.log10(this.irrelevant
							.bernouilli(word));
					neutralProbability += Math.log10(this.neutral
							.bernouilli(word));
				} else {
					positiveProbability += Math.log10(1 - this.positive
							.bernouilli(word));
					negativeProbability += Math.log10(1 - this.negative
							.bernouilli(word));
					irrelevantProbability += Math.log10(1 - this.irrelevant
							.bernouilli(word));
					neutralProbability += Math.log10(1 - this.neutral
							.bernouilli(word));
				}
			}
		}
		double probaMax = Math.max(
				Math.max(positiveProbability, negativeProbability),
				Math.max(irrelevantProbability, neutralProbability));
		if (probaMax == irrelevantProbability)
			highestProbability = "irrelevant";
		else if (probaMax == neutralProbability
				|| Math.abs(positiveProbability - negativeProbability)
						/ Math.max(positiveProbability, negativeProbability) <= 0.05) // On renvoie "neutral" si il y a un rapport de moins de 5% entre positive et negative
			highestProbability = "neutral";
		else if (probaMax == positiveProbability)
			highestProbability = "positive";
		else if (probaMax == negativeProbability)
			highestProbability = "negative";
		return highestProbability;
	}

	/**
	 * Cette méthode permet d'effectuer un algorithme multinommial sur un tweet :
	 * Pour chaque mot du tweet, on calcul sa frequence dans chacun des dictionnaires
	 * des polarites. Ainsi la probabilite d'un tweet sur une polarite est egale au produit
	 * des probabilite de chaque mot sur cette polarite.
	 * @param words
	 * @return La probabilite la plus haute ("irrelevant", "neutral", "positive" ou "negative")
	 */
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
						/ Math.max(positiveProbability, negativeProbability) <= 0.05) // On renvoie "neutral" si il y a un rapport de moins de 5% entre positive et negative
			highestProbability = "neutral";
		else if (probaMax == positiveProbability)
			highestProbability = "positive";
		else if (probaMax == negativeProbability)
			highestProbability = "negative";
		return highestProbability;
	}

	public Dictionnary getIrrelevant() {
		return irrelevant;
	}

	public void setIrrelevant(Dictionnary irrelevant) {
		this.irrelevant = irrelevant;
	}

	public Dictionnary getNeutral() {
		return neutral;
	}

	public void setNeutral(Dictionnary neutral) {
		this.neutral = neutral;
	}

	public Dictionnary getPositive() {
		return positive;
	}

	public void setPositive(Dictionnary positive) {
		this.positive = positive;
	}

	public Dictionnary getNegative() {
		return negative;
	}

	public void setNegative(Dictionnary negative) {
		this.negative = negative;
	}
	
}