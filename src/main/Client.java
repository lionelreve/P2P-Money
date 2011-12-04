package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

import chord.ChordInterface;
import chord.ChordNode;

public class Client extends UnicastRemoteObject{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ChordInterface chordNode;
	private static int sleepTime = 100;
	
	public Client(String host, int port) throws RemoteException{
		try {
			chordNode = (ChordInterface) Naming.lookup("rmi://" + host + ":" + port + "/ChordNode");
			System.out.println("the client is connected.\n");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
	}
	
	public void run() throws RemoteException{
		
		System.out.println("Client is running");
		
		BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Quelle est votre adresse ip ? ");
		String clientIp = "";
		ChordNode n = null;
		int clientPort = 0;
		try {
			clientIp = stdIn.readLine();
			System.out.println("Sur quel port ?");
			clientPort = Integer.parseInt(stdIn.readLine().trim());
			n = new ChordNode(clientIp);
			LocateRegistry.createRegistry(clientPort);
			Naming.rebind("rmi://" + clientIp + ":" + clientPort+"/ChordNode", n);
//			int id;
//			id = Hash.hash(clientIp) % (int) Math.pow(2, FingerTable.MAXFINGERS -1);
//			if(id < 0){
//				id = 128 + id;
//			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		// nodes.add(n);
		System.out.println("\n--> Adding NODE " + n.getChordKey().getKey() + "\n");
		n.join(chordNode);
		chordNode.addNode(clientIp, clientPort);
		
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
							System.out.println(chordNode.display());
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
						Thread.sleep(sleepTime);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
		
		
//		try {
//			
//			while (true) {
//				System.out.println("\n-------------------");
//				System.out.println("1) put an object");
//				System.out.println("2) get an object");
//				System.out.println("0) exit");
//				System.out.print("---> ");
//				int chx = Integer.parseInt(stdIn.readLine().trim());
//				switch (chx) {
//				case 1:
//					System.out.print("key = ");
//					String key = stdIn.readLine();
//					System.out.print("message = ");
//					Sentence s = new Sentence(stdIn.readLine());
//					System.out.print("TTL = ");
//					int TTL = Integer.parseInt(stdIn.readLine().trim());
//					chord.putObject(key, s, TTL);
//					System.out.println("Objet envoyé [key="+key+"].");
//					break;
//				case 2:
//					System.out.print("key = ");
//					String keytoget = stdIn.readLine();
//					System.out.println("searching...");
//					chord.getObject(keytoget, this);
//					int ttl = 50;
//					while(ttl != 0){
//						Thread.sleep(200);
//						if(sentence != null)
//							break;
//						ttl--;
//					}
//					System.out.println();
//					if(ttl > 0){
//						System.out.println("search succefully finished");
//					} else {
//						System.out.println("search fail!");
//					}
//					sentence =null;
//					break;
//				case 0:
//					System.out.println("=> Client exit.");
//					System.exit(0);
//					break;
//				default:
//					/* nothing to do */
//				}
//			}
//		} catch (MalformedURLException e) {
//			e.printStackTrace();
//		} catch (ConnectException e) {
//			System.err.println("Chord unreachable : " + e.getMessage());
//			System.err.println("\nSystem exit");
//		} catch (RemoteException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
	}
	
	public static void main (String args[]){
		BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Connection to a chord:");
		System.out.print("- hostname of a node = ");
		try {
			String host = stdIn.readLine();
			System.out.print("- port of this node = ");
			int port = Integer.parseInt(stdIn.readLine().trim());
			Client client = new Client(host, port);
			client.run();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}