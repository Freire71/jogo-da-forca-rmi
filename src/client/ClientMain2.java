package client;

import java.rmi.Naming;
import java.util.ArrayList;
import java.util.Scanner;

import server.MatchInterface;
import server.ServerInterface;

public class ClientMain2 {
	 public static void main (String[] argv) {
        try {
	    	ServerInterface server =
	            (ServerInterface) Naming.lookup ("//127.0.0.1:1099/Forca");
        	
	    		ClientInterface c =  new Client("caio@gmail.com", "Caio");

		    	if(server != null) {
		    		System.out.println("O servidor está online!");
		    	}
		    	
		    	System.out.println(server.getMenu());
		    	ArrayList<Integer> menuOpts = server.getMenuOptions();
		    	int opt;
		    	do {
		    		System.out.println("Opção: ");
		    		Scanner in = new Scanner(System.in);
		    		opt = Integer.parseInt(in.nextLine());
		    	} while(!menuOpts.contains(opt));
		    	if (opt == 2) {
		    		System.out.println("Digite o ID da partida (exit p/ sair): ");
		    		boolean connected = false;
		    		String typed = "";
		    		do {
		    			Scanner in = new Scanner(System.in);
		    			typed = in.nextLine();
		    			if(typed.equals("exit") ) {
		    				System.out.println("Até a próxima!");
		    				break;
		    			}
			    		connected = server.receiveMenuOption(2, c, typed);
		    		} while (!connected);
		    		
		    	} else {
			    	server.receiveMenuOption(opt, c, "");
		    	}

	        } catch (Exception e) {
	        	System.out.println ("Error ao conectar no servidor " +e + e.getMessage().toString());
	        	e.printStackTrace();
	        }
    }

}


