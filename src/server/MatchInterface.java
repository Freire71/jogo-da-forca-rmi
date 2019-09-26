package server;

import java.rmi.Remote;
import java.rmi.RemoteException;

import client.ClientInterface;

public interface MatchInterface extends Remote {
	
	public boolean addPlayer(ClientInterface ci) throws RemoteException;
	public boolean hasStarted() throws RemoteException;
	public void setGuestEmail(String email) throws RemoteException;
	public String getGuestEmail() throws RemoteException;

}
