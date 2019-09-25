package client;

import java.rmi.Naming;

import server.MatchInterface;
import server.ServerInterface;

public class ClientMain2 {
	 public static void main (String[] argv) {

	        MatchInterface match = null;
	         
	        try {
		    	ServerInterface server =
		            (ServerInterface) Naming.lookup ("//127.0.0.1:1099/Forca");
	        	
		    		ClientInterface c =  new Client("caio@gmail.com", "Caio");
		    	if(server != null) {
		    		System.out.println("Servidor não é null");
		    	}
		          server.startNewMatch((c));
		          System.out.println("Jogador: " +  c.getName() +" conectado!");

	        } catch (Exception e) {
	        	System.out.println ("Error ao conectar no servidor " +e + e.getMessage().toString());
	        	e.printStackTrace();
	        }
	    }


}
