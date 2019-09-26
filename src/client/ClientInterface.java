package client;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientInterface extends Remote {
	public String getName() throws RemoteException;
	public String getEmail() throws RemoteException;
	public void increaseScore(int score) throws RemoteException;
	public void decreaseGuesses() throws RemoteException;
	public int getGuesses() throws RemoteException;
	public int getScore() throws RemoteException;
	public void receiveMessage(String message) throws RemoteException;
	public String yourTurn() throws RemoteException; 
}
