package client;

import java.rmi.Naming;
import java.util.ArrayList;
import java.util.Scanner;

import server.MatchInterface;
import server.ServerInterface;

public class ClientMain {
    public static void main (String[] argv) {         
        try {
	    	ServerInterface server =
	            (ServerInterface) Naming.lookup ("//127.0.0.1:1099/Forca");
//        	
//	    	String email = "";
//	    	String nome = "";
//	    	
//	    	System.out.println("Digite o seu nome: ");
//	    	do {
//	    		Scanner in = new Scanner(System.in);
//	    		nome = in.nextLine();
//	    	} while(nome.equals(""));
//	    	
//	    	System.out.println("Digite o seu e-mail:");
//	    	do {
//	    		Scanner in = new Scanner(System.in);
//	    		email = in.nextLine();
//	    	} while (email.equals(""));
//	    	
//	    	ClientInterface c =  new Client(email, nome);
	    	ClientInterface c =  new Client("alex@gmail.com", "Alex");

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
