package server;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

import client.ClientInterface;

public interface ServerInterface extends Remote {
	public void startNewMatch(ClientInterface ci) throws RemoteException;
	public String getMenu() throws RemoteException;
	public ArrayList<Integer> getMenuOptions() throws RemoteException;
	public boolean receiveMenuOption(int option, ClientInterface ci, String matchId) throws RemoteException;
}
