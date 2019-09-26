package server;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.text.Normalizer;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import client.ClientInterface;

public class Match extends Thread implements MatchInterface {
	
	private boolean hasStarted = false;
    private ClientInterface c1, c2;
    private String guestEmail = "";
    private String word;
    private int[] characters;
    private int remainingPoints;
    private String matchId;
    private Timestamp startTime;
    private Timestamp endTime;
    private String matchLog = "Jogadas realizadas: \n";
    
    public Match (ClientInterface c1, ClientInterface c2, String word, String matchId){
        this.c1=c1;
        this.c2=c2;
        this.word = word.toUpperCase();
        this.startTime = new Timestamp(System.currentTimeMillis());
        this.matchId = matchId;
    	System.out.println("ID da partida: " + matchId);
    	System.out.println("Palavra: " + word.toUpperCase());
        setupWord();
        this.start();
    }
    
    public Match (ClientInterface c1, String word, String matchId){
        this.c1=c1;
        this.word = word.toUpperCase();
        this.startTime = new Timestamp(System.currentTimeMillis());
        this.matchId = matchId;
    	System.out.println("ID da partida: " + matchId);
    	System.out.println("Palavra: " + word);
        setupWord();
    }
    
    public void setGuestEmail(String email) {
    	this.guestEmail = email;
    }
    
    public String getGuestEmail() {
    	return this.guestEmail;
    }
    
    
    public void setupWord() {
    	int[] charactersArray = new int[word.length()];
    	for (int i = 0; i < word.length(); i++) {
    		charactersArray[i] = 0;
    	}
    	characters = charactersArray;
    	remainingPoints = word.length();
    }
    
    public String printWord() {
    	String printedWord = "";
    	for (int i = 0; i < word.length(); i++) {
    		if(characters[i] == 0) {
    			printedWord += "_ ";
    		} else {
    			printedWord += word.charAt(i) + " ";
    		}
    	}
    	return printedWord;
    }
    
    public int verifyCharacter(String character) {
    	int score = 0; 
    	String normalized = Normalizer.normalize(word, Normalizer.Form.NFD);
		String accentRemoved = normalized.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    	for(int i = 0; i< word.length(); i++) {
    		String wordCharacter = accentRemoved.substring(i, i+1);
    		if (wordCharacter.equals(character) && characters[i] == 0) {
				score += 1;
				remainingPoints -= 1;
    			characters[i] = 1;
    		}
    	}
    	return score;
    }
    
    public void turn(ClientInterface playerTurn, ClientInterface awaitingPlayer) {
    	try {
    		playerTurn.receiveMessage("É a sua vez");
    		awaitingPlayer.receiveMessage("É a vez do seu adversário jogar");
    		int score = 0;
    		String character = playerTurn.yourTurn().toUpperCase();
    		if(character.substring(0,1).equals("!")) {
    			if (playerTurn.getGuesses() == 0) {
    				playerTurn.receiveMessage("Você não possui mais palpites de ouro!");
    				return;
    			}
    			playerTurn.decreaseGuesses();
    			String guess = character.substring(1, character.length()).toUpperCase();
    			String play = playerTurn.getName() + " deu um palpite de ouro: " + guess;
    			System.out.println(play);
    			matchLog += play + "\n";
    			if (guess.equals(word)) {
    				score = remainingPoints;
    				remainingPoints = 0;
    			}
    		} else {
    			String play = playerTurn.getName() + " digitou a letra: " + character.toUpperCase();
        		matchLog += play + "\n";
        		System.out.println(play);
        		score = verifyCharacter(character);
        	
    		}
    		playerTurn.receiveMessage("Você marcou " + score + " ponto(s)");
    		
    		if(score != 0) {
    			playerTurn.increaseScore(score);
    			playerTurn.receiveMessage("Palavra: " + printWord());
        		awaitingPlayer.receiveMessage("Palavra: " + printWord());
    		}
    	}
    	catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
    }
    
    public void createLogFile(String matchScore) throws IOException{
	    endTime = new Timestamp(System.currentTimeMillis());
	    List<String> lines = Arrays.asList("Partida de ID: " + matchId,
	    		"Timestamp de inicio: " + startTime, "Timestamp de encerramento: " + endTime,"Palavra: " + word,  matchScore, matchLog);
	    
	    File file = new File("logs/" + matchId + ".txt");
	    file.createNewFile();
	    
	    Path path = Paths.get("logs/" + matchId + ".txt");
	    Files.write(path, lines, StandardCharsets.UTF_8);
    	System.out.println("Arquivo de log gravado");
    }
    
    
    public void run()
    { 
    	try {
    		String aboutGuesses = "Para dar um palpite de ouro comece com '!'\nAtenção! Você possui apenas 1 palpite de ouro!\n";
    		c1.receiveMessage("Seja bem-vindo " + c1.getName() + "!\n" + aboutGuesses);
			c2.receiveMessage("Seja bem-vindo " + c2.getName() + "!\n" + aboutGuesses);
			c1.receiveMessage("Palavra: " + printWord());
    		c2.receiveMessage("Palavra: " + printWord());
    		Random r = new Random();
    		int playsFirst = r.nextInt(2);
    		System.out.println("Começa jogando:  " + playsFirst);
    		
    		if (playsFirst == 0) {
    			while (remainingPoints > 0) {
        			turn(c1, c2);
        			if (remainingPoints == 0) {
        				break;
        			}
            		turn(c2, c1);	
        		}
    		} else {
    			while (remainingPoints > 0) {
	    			turn(c2, c1);
	    			if (remainingPoints == 0) {
	    				break;
	    			}
	        		turn(c1, c2);	
        		}
    		}
    		
    		int c1Score = c1.getScore();
    		int c2Score = c2.getScore();
    		if (c1Score > c2Score) {
    			c1.receiveMessage("Parabéns! Você venceu de " + c2.getName());
    			c2.receiveMessage("Você foi derrotado por " + c1.getName());
    		} else if (c2Score > c1Score) {
     			c2.receiveMessage("Parabéns! Você venceu de " + c1.getName());
    			c1.receiveMessage("Você foi derrotado por " + c2.getName());
    		} else {
    			c1.receiveMessage("Você empatou com " + c2.getName());
    			c2.receiveMessage("Você empatou " + c1.getName());
    		}
    		String matchScore = "Placar Final:\n" + c1.getName() + ": " + c1Score + " ponto(s)\n" + c2.getName() + ": " + c2Score + " ponto(s)";
    		String matchScoreWithEmail = "Placar Final:\n" + c1.getName() + "(" + c1.getEmail() + "): " + c1Score + " ponto(s)\n" + c2.getName() + "(" + c2.getEmail() + "): " + c2Score + " ponto(s)";

    		createLogFile(matchScoreWithEmail);
    		c1.receiveMessage("Palavra: " + word);
    		c2.receiveMessage("Palavra: " + word);
    		c1.receiveMessage(matchScore);
    		c2.receiveMessage(matchScore);

		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			e.getMessage();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }

	@Override
	public boolean addPlayer(ClientInterface ci) throws RemoteException {
		// TODO Auto-generated method stub
		this.c2 = ci;
		hasStarted = true;
		start();
		return true;
	}

	@Override
	public boolean hasStarted() throws RemoteException {
		// TODO Auto-generated method stub
		return this.hasStarted;
	}

}
