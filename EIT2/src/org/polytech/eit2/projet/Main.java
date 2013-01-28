package org.polytech.eit2.projet;


public class Main {

	private static String trainFilePath = "./twitter/train.txt";
	private static String testFilePath = "./twitter/test.txt";
	private static String shortTokenFilePath = "./token/english_short.txt";
	private static String longTokenFilePath = "./token/english_long.txt";
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		FileBrowser fb = new FileBrowser(trainFilePath);
		FileBrowser fbTokenizer = new FileBrowser(trainFilePath);
		FileBrowser fbTokenizer2 = new FileBrowser(trainFilePath);
		
		fbTokenizer.addTokenWords(shortTokenFilePath);
		fbTokenizer2.addTokenWords(longTokenFilePath);
		
		System.out.println("\n**********Resultats sans Tokenizer**********\n");
		fb.setDictionaries();
		fb.setAllTops(); // Recupere et affiche les top (plus et moins) significatifs pour chaque polarité
		fb.testMultinomial("./results/trainMultinomialSansToken.txt");
		//fb.testBiNommialeBernouilli("./results/trainBinommialSansToken.txt");
		//On lance Multinommial sur le corpus de test
		
		fb.setFilePath(testFilePath);
		fb.testMultinomial("./results/BOREL-JOUVE-test-eit-13.txt");
		
		System.out.println("\n**********Resultats avec Tokenizer Short**********\n");
		fbTokenizer.setDictionaries();
		fbTokenizer.testMultinomial("./results/trainMultinomialShortToken.txt");
		//fbTokenizer.testBiNommialeBernouilli("./results/trainBinommialShortToken.txt");
		
		System.out.println("\n**********Resultats avec Tokenizer Long**********\n");
		fbTokenizer2.setDictionaries();
		fbTokenizer2.testMultinomial("./results/trainMultinomialLongToken.txt");
		//fbTokenizer2.testBiNommialeBernouilli("./results/trainBinommialLongToken.txt");
	}
}
