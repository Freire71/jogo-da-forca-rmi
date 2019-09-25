package server;

import java.rmi.Remote;
import java.rmi.RemoteException;

import client.ClientInterface;

public interface ServerInterface extends Remote {
	public boolean computeAnswer(String character) throws RemoteException;
	public MatchInterface startNewMatch(ClientInterface ci) throws RemoteException;
	public boolean connect(String matchID) throws RemoteException;
}
