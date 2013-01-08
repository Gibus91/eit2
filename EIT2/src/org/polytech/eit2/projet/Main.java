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
<<<<<<< HEAD
		fb.testMultinomial();
		fb.testBiNommialeBernouilli();
		System.out.println("\n**********Resultats avec Tokenizer Short**********\n");
		fbTokenizer.setDictionaries();
		fbTokenizer.testMultinomial();
		fbTokenizer.testBiNommialeBernouilli();
		System.out.println("\n**********Resultats avec Tokenizer Long**********\n");
		fbTokenizer2.setDictionaries();
		fbTokenizer2.testMultinomial();
		fbTokenizer2.testBiNommialeBernouilli();
=======
		fb.testMultinomial("./results/trainBinommial.txt");
//		System.out.println("\n**********Resultats avec Tokenizer**********\n");
//		fbTokenizer.setDictionaries();
//		fbTokenizer.testMultinomial();
>>>>>>> branch 'master' of https://github.com/Gibus91/eit2.git
	}
}
