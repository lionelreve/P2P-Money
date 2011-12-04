package main;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.util.ArrayList;

import chord.ChordInterface;
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
		    firstNode.setNodes(new ArrayList<ChordInterface>());
		    firstNode.addNode(firstNode);
		    System.out.println("Chord cr��e");
		} catch (Exception e) {
		    e.printStackTrace();
		}

	}

}
