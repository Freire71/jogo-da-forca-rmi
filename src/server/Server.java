package server;


import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;
import java.awt.List;
import java.io.File;
import java.io.FileNotFoundException;

import client.ClientInterface;

public class Server extends UnicastRemoteObject implements ServerInterface  {

	private static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	private HashMap<String, MatchInterface> matches = new HashMap<String, MatchInterface>();
    public int numberOfPlayers=0;
    ArrayList<String> words = new ArrayList<>();

	protected Server() throws RemoteException {
		super();
		loadWordsFile();
		// TODO Auto-generated constructor stub
	}
	
	
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
	public void startNewMatch(ClientInterface ci) throws RemoteException {
		// TODO Auto-generated method stub
        System.out.println ("Partida criada pelo o jogador: " + ci.getName());
        Random r = new Random();
    	int wordIndex = r.nextInt(300);
    	String matchId = randomAlphaNumeric(8); 
    	MatchInterface match =  new Match(ci, words.get(wordIndex), matchId);
        matches.put(matchId, match);
        numberOfPlayers += 1;
        ci.receiveMessage("Para sua segurança, digite o e-mail do seu oponente: ");
        String guestEmail = ci.yourTurn();
        match.setGuestEmail(guestEmail);
        
        ci.receiveMessage("Partida criada com sucesso!\nID: " + matchId + "\nEnvie o ID da partida para seu oponente");
	}
	
	public boolean enterMatch(String matchId, ClientInterface ci) throws RemoteException {
		MatchInterface match = matches.get(matchId);
		if(match.hasStarted()) {
			ci.receiveMessage("Não é possível entrar em uma partida em andamento");
			return false;
		}
		if(!match.getGuestEmail().equals(ci.getEmail())) {
			ci.receiveMessage("Você não possui autorização para entrar nessa partida");
			return false;
		}
		match.addPlayer(ci);
        numberOfPlayers += 1;
		return true;
	}
	
	public boolean matchExists(String matchId) {
		return matches.containsKey(matchId);
	}
	
	public String getMenu() throws RemoteException  {
		return "------ Jogo da Forca ------\n(1) Novo Jogo\n(2) Conectar-se a uma partida\n(3) Sair";
	}

	@Override
	public boolean receiveMenuOption(int option, ClientInterface ci, String matchId) throws RemoteException {
		if (option == 1 ) {
			startNewMatch(ci);
		} else if (option == 2) {
			if (!matchExists(matchId)) {
				ci.receiveMessage("Não existe uma partida com o ID: " + matchId);
				return false;
			} else {
				enterMatch(matchId, ci);
			}
		} else if (option == 3){
			ci.receiveMessage("Foi um prazer jogar com você!");
			return false;
		}
		return true;
	}

	@Override
	public  ArrayList<Integer> getMenuOptions() throws RemoteException {
		ArrayList<Integer> list = new ArrayList<Integer>();
		list.add(1);
		list.add(2);
		list.add(3);

		return list;
	}
}
