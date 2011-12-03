package main;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.text.SimpleDateFormat;
import java.util.Date;

import chord.ChordNode;

public class Main {

	private static final int port = 8000;
	private static final String host = "localhost";
	public static ChordNode firstNode;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		
		try {
		    LocateRegistry.createRegistry(port);
		    firstNode = new ChordNode(host);
		    Naming.rebind("rmi://" + host + ":" + port+"/ChordNode", firstNode);
		    System.out.println("Chord créée");
		} catch (Exception e) {
		    e.printStackTrace();
		}

	}

}
