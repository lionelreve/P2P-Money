package main;

import java.io.BufferedReader;
import java.io.InputStreamReader;
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
		    System.out.println("Chord créée");
		} catch (Exception e) {
		    e.printStackTrace();
		}
		
		new Thread(new Runnable(){
			@Override
			public void run() {
				while(true){
					try {
						BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
						System.out.println("#########################");
						System.out.println("# 1) Afficher la chord \t#");
						System.out.println("# 2) Quitter la chord \t#");
						System.out.println("#########################");
						System.out.print("--> ");
						int chx = Integer.parseInt(input.readLine().trim());
						
						String nodeId;
						switch (chx) {
						case 1:
							System.out.println(firstNode.display());
							break;
						case 2:
//							while(true){
//								System.out.println("id = " );
//								nodeId = input.readLine();
//								int id;
//								id = Hash.hash(nodeId);
//								id = id % (int) Math.pow(2, FingerTable.MAXFINGERS -1);
//								if(!ifNodeExist(id)){
//									System.err.println("Node doesn't exist !");
//								} else {
//									ChordNode nToDelete = nodes.get(id);
//									if (nodes.remove(nodeId))
//									{
//										System.out.println("\n--> Deleting NODE " + nToDelete.getChordKey().getKey() + "\n");
//										nToDelete.delete();
//										nbNode--;
//									}
//									else 
//										System.err.println("\n--> Problem during deletion of NODE " + nToDelete.getChordKey().getKey() + "\n");
//									break;
//								}
//							}
							System.out.println("Not yet implemented");
							break;
						case 3:
							
							break;
						case 0:
							System.out.println("=> Chord exit");
							System.exit(0);
							break;
						default:
							break;
						}
					} catch (Exception e) {
						e.printStackTrace();
					}

					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();

	}

}
