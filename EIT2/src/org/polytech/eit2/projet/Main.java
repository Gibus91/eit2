package org.polytech.eit2.projet;


public class Main {

	private static String trainFilePath = "./twitter/train.txt";
	private static String testFilePath = "./twitter/test.txt";
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		FileBrowser fb = new FileBrowser(trainFilePath);
		fb.setDictionaries();
	}

}
