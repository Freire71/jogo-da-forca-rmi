package server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class MatchWrapper extends UnicastRemoteObject {
	Match match;
	protected MatchWrapper(Match match) throws RemoteException {
		super();
		this.match = match;
	}

}
