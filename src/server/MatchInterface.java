package server;

import java.rmi.Remote;
import java.rmi.RemoteException;

import client.ClientInterface;

public interface MatchInterface extends Remote {
	
	public boolean addPlayer(ClientInterface ci) throws RemoteException;
}
