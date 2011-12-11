//package main;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.net.MalformedURLException;
//import java.rmi.ConnectException;
//import java.rmi.Naming;
//import java.rmi.NotBoundException;
//import java.rmi.RemoteException;
//import java.rmi.server.UnicastRemoteObject;
//
//import chord.ChordInterface;
//import chord.ChordNode;
//import chord.FingerTable;
//import security.Hash;
//
//public class Client extends UnicastRemoteObject{
//	
//	/**
//	 * 
//	 */
//	private static final long serialVersionUID = 1L;
//	private ChordInterface chordNode;
//	private static int sleepTime = 100;
//	
//	public Client(String host, int port) throws RemoteException{
//		try {
//			chordNode = (ChordInterface) Naming.lookup("rmi://" + host + ":" + port + "/ChordNode");
//			System.out.println("the client is connected.\n");
//		} catch (IOException e) {
//			e.printStackTrace();
//		} catch (NotBoundException e) {
//			e.printStackTrace();
//		}
//	}
//	
//	public void run() throws RemoteException{
//		
//		System.out.println("Client is running");
//		
//		BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
//		System.out.println("What is your ip adress ? ");
//		String clientIp = "";
//		try {
//			clientIp = stdIn.readLine();
////			int id;
////			id = Hash.hash(clientIp) % (int) Math.pow(2, FingerTable.MAXFINGERS -1);
////			if(id < 0){
////				id = 128 + id;
////			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		
//		ChordNode n = new ChordNode(clientIp);
//		// nodes.add(n);
//		System.out.println("\n--> Adding NODE " + n.getChordKey().getKey() + "\n");
//		n.join(chordNode);
//		chordNode.addNode(n);
//		
//		
////		try {
////			
////			while (true) {
////				System.out.println("\n-------------------");
////				System.out.println("1) put an object");
////				System.out.println("2) get an object");
////				System.out.println("0) exit");
////				System.out.print("---> ");
////				int chx = Integer.parseInt(stdIn.readLine().trim());
////				switch (chx) {
////				case 1:
////					System.out.print("key = ");
////					String key = stdIn.readLine();
////					System.out.print("message = ");
////					Sentence s = new Sentence(stdIn.readLine());
////					System.out.print("TTL = ");
////					int TTL = Integer.parseInt(stdIn.readLine().trim());
////					chord.putObject(key, s, TTL);
////					System.out.println("Objet envoyé [key="+key+"].");
////					break;
////				case 2:
////					System.out.print("key = ");
////					String keytoget = stdIn.readLine();
////					System.out.println("searching...");
////					chord.getObject(keytoget, this);
////					int ttl = 50;
////					while(ttl != 0){
////						Thread.sleep(200);
////						if(sentence != null)
////							break;
////						ttl--;
////					}
////					System.out.println();
////					if(ttl > 0){
////						System.out.println("search succefully finished");
////					} else {
////						System.out.println("search fail!");
////					}
////					sentence =null;
////					break;
////				case 0:
////					System.out.println("=> Client exit.");
////					System.exit(0);
////					break;
////				default:
////					/* nothing to do */
////				}
////			}
////		} catch (MalformedURLException e) {
////			e.printStackTrace();
////		} catch (ConnectException e) {
////			System.err.println("Chord unreachable : " + e.getMessage());
////			System.err.println("\nSystem exit");
////		} catch (RemoteException e) {
////			e.printStackTrace();
////		} catch (IOException e) {
////			e.printStackTrace();
////		} catch (InterruptedException e) {
////			e.printStackTrace();
////		}
//	}
//	
//	public static void main (String args[]){
//		BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
//		System.out.println("Connection to a chord:");
//		System.out.print("- hostname of a node = ");
//		try {
//			String host = stdIn.readLine();
//			System.out.print("- port of this node = ");
//			int port = Integer.parseInt(stdIn.readLine().trim());
//			Client client = new Client(host, port);
//			client.run();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//
//}
