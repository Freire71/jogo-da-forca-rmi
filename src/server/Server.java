package server;


import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

import client.ClientInterface;

public class Server extends UnicastRemoteObject implements ServerInterface  {
	
	private String message;
	private ArrayList<MatchInterface> matches;
	private MatchInterface currentMatch;
	private boolean newGame = true; 
	ClientInterface c1=null;
	ClientInterface c2=null;
    public int numberOfPlayers=0;
    ArrayList<String> words = new ArrayList<>();

	protected Server() throws RemoteException {
		super();
		loadWordsFile();
		
		// TODO Auto-generated constructor stub
	}
	
	private static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	
	public String randomAlphaNumeric(int count) {
		StringBuilder builder = new StringBuilder();
		while (count-- != 0) {
		int character = (int)(Math.random()*ALPHA_NUMERIC_STRING.length());
		builder.append(ALPHA_NUMERIC_STRING.charAt(character));
		}
		return builder.toString();
	}
	
	public void loadWordsFile() {
		File file = new File("words.txt");
		
		try {
	        Scanner buffer = new Scanner(file);
	        while (buffer.hasNextLine()) {
	          String word = buffer.nextLine();
	          System.out.println(word.trim());
	          words.add(word.trim());
	        }
	        System.out.println("Arquivo de palavras carregado!");
	        buffer.close();
	      } catch (FileNotFoundException e) {
	        System.out.println("Erro brabo: " + e);
	        // e.printStackTrace();
	      }
	    
	}

	@Override
	public boolean computeAnswer(String letter) throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean connect(String matchID) throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public MatchInterface startNewMatch(ClientInterface ci) throws RemoteException {
		// TODO Auto-generated method stub
		if (newGame)
        {
            c1=ci;
            newGame=false;
            System.out.println ("Partida criada pelo o jogador: " + ci.getName());
        } else {
        	Random r = new Random();
        	int wordIndex = r.nextInt(300);
            c2=ci;
            System.out.println ("Jogador: " + ci.getName() + " se conectou para jogar");

            newGame=true;
            new Match(c1, c2, words.get(wordIndex), randomAlphaNumeric(8));
	    }	
        return currentMatch;
	}
}
