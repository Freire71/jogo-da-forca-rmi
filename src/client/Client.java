package client;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

public class Client extends UnicastRemoteObject implements ClientInterface {
	
	private String email;
	private String name;
	private int score;
	
	
	public Client(String email, String name) throws RemoteException {
		super();
		this.email = email;
		this.name = name;
		this.score = 0;
	}
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public void receiveMessage(String message) throws RemoteException {
		// TODO Auto-generated method stub
		System.out.println(message);
	}

	@Override
	public String yourTurn() throws RemoteException {
		// TODO Auto-generated method stub
        Scanner in = new Scanner(System.in);
        return  in.nextLine();
	}

	@Override
	public void increaseScore(int score) throws RemoteException {
		// TODO Auto-generated method stub
		this.score += score;
	}

	@Override
	public int getScore() throws RemoteException {
		// TODO Auto-generated method stub
		return this.score;
	}


	

}
