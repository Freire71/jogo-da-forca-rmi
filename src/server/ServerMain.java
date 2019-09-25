package server;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class ServerMain {


  public static void main(String[] args) {
    try {
      LocateRegistry.createRegistry(1099);
      Naming.rebind("Forca", new Server());
      System.out.println("Servidor pronto para uso!!");
    } catch (Exception e) {
      System.out.println("Erro ao inicializar o servidor: " + e);
    }
  }
}
