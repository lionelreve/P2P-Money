package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;

import security.Hash;
import transaction.History;
import transaction.TransactionException;
import transaction.TransactionObject;

import chord.ChordKey;
import chord.ChordNode;
import chord.FingerTable;

public class MainMain {

	public static ArrayList<ChordNode> nodes;
	public static ChordNode firstNode;
	private static int nbNode = 2;
	private static int sleepTime = 100;

	private static ArrayList<ChordNode> nodesToBeUse;

	public static boolean ifNodeExist(int id) throws RemoteException {
		for (ChordNode n : nodes) {
			if (n.getChordKey().getKey() == id)
				return true;
		}
		return false;
	}

	public static void sortNodes() throws RemoteException {
		ArrayList<ChordNode> res = new ArrayList<ChordNode>();
		ChordNode nodeMin;
		int i, j;
		while (nodes.size() != 0) {
			i = 0;
			j = 0;
			nodeMin = nodes.get(0);
			for (ChordNode n : nodes) {
				if (n.getChordKey().getKey() < nodeMin.getChordKey().getKey()) {
					nodeMin = n;
					i = j;
				}
				j++;
			}
			res.add(nodeMin);
			nodes.remove(i);
		}
		nodes = res;
	}

	public static void display() throws RemoteException {
		String res = "";
		res += "________________________________________________________\n\n";
		sortNodes();
		for (ChordNode n : nodes) {
			res += n;
		}
		res += "________________________________________________________\n";
		System.out.println(res);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		nodes = new ArrayList<ChordNode>();
		int nbTotalNodes = 1;
		System.out.println("Nombres de noeuds d�sir�s ? ");
		BufferedReader input_first = new BufferedReader(new InputStreamReader(System.in));
		try {
			nbTotalNodes = Integer.parseInt(input_first.readLine());
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		createNodes(nbTotalNodes);

		try {
			firstNode = new ChordNode("localhost:8000");
		} catch (RemoteException e1) {
			e1.printStackTrace();
		}
		nodes.add(firstNode);

		try {
			Registry registry = LocateRegistry.createRegistry(8000);
			Naming.rebind("//localhost:8000/Node", registry);
		} catch (Exception e) {
			e.printStackTrace();
		}

		joinNodes(firstNode);

		new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						BufferedReader input = new BufferedReader(
								new InputStreamReader(System.in));
						System.out.println("################################");
						System.out.println("# 1) Inserer un node \t\t\t\t#");
						System.out.println("# 2) Retirer un node \t\t\t\t#");
						System.out.println("# 3) Afficher la chord \t\t\t\t#");
						System.out.println("# 4) Faire une transaction \t\t\t#");
						System.out.println("# 5) Afficher l'historique de transaction \t#");
						System.out.println("# 0) Quitter jChord \t\t\t\t#");
						System.out.println("################################");
						System.out.print("--> ");
						int chx = Integer.parseInt(input.readLine().trim());

						String nodeId;
						switch (chx) {
						case 1:
							while (true) {
								System.out.print("Node id = ");
								nodeId = input.readLine();
								int id;
								id = Hash.hash(nodeId)
										% (int) Math.pow(2,
												FingerTable.MAXFINGERS - 1);
								if (id < 0) {
									id = 128 + id;
								}
								if (ifNodeExist(id)) {
									System.err
											.println("Node already existing !");
								} else {
									break;
								}
							}
							ChordNode n = new ChordNode(nodeId);
							nodes.add(n);
							System.out.println("\n--> Adding NODE "
									+ n.getChordKey().getKey() + "\n");
							n.join(firstNode);

							nbNode++;
							break;
						case 2:
							while (true) {
								System.out.println("id = ");
								int id = Integer.parseInt(input.readLine());
								if (!ifNodeExist(id)) {
									System.err.println("Node doesn't exist !");
								} else {
									ChordNode nToDelete = null;
									for(int i =0; i < nodes.size(); i++){
										if(nodes.get(i).getChordKey().getKey() == id){
											nToDelete = nodes.get(i);
											break;
										}
									}
									if (nToDelete != null && nodes.remove(nToDelete)) {
										System.out
												.println("\n--> Deleting NODE "
														+ nToDelete
																.getChordKey()
																.getKey()
														+ "\n");
										nToDelete.delete();
										nbNode--;
									} else
										System.err
												.println("\n--> Problem during deletion of NODE "
														+ nToDelete
																.getChordKey()
																.getKey()
														+ "\n");
									break;
								}
							}
							break;
						case 3:
							display();
							break;
						case 4:
							System.out.print("id of the source Node = ");
							int from_id = Integer.parseInt(input.readLine().trim());
							ChordNode from_Node = null;
							if (!ifNodeExist(from_id)) {
								System.err.println("Node doesn't exist !");
							} else {
								for(int i = 0; i < nodes.size(); i++){
									if(nodes.get(i).getChordKey().getKey() == from_id){
										from_Node = nodes.get(i);
										break;
									}
								}
							}

							System.out.print("id of the destination Node = ");
							int to_id = Integer.parseInt(input.readLine().trim());
							ChordNode to_Node = null;
							if (!ifNodeExist(to_id)) {
								System.err.println("Node doesn't exist !");
							} else {
								for(int i =0; i < nodes.size(); i++){
									if(nodes.get(i).getChordKey().getKey() == to_id){
										to_Node = nodes.get(i);
										break;
									}
								}
							}
							
							System.out.print("Value of the transaction = ");
							double value = Double.parseDouble(input.readLine().trim());
							
							TransactionObject transacObject = new TransactionObject(from_Node,(ChordKey) to_Node.getChordKey() , value);
							if (History.transactionIsAllowed(transacObject)){
								History.historyTransactions.add(transacObject);
								from_Node.getWallet().giveMoney(value);
								to_Node.getWallet().receiveMoney(value);
							}
							else {
								new TransactionException("Transaction not allowed");
							}
							
							break;
						case 5:
							System.out.println(History.toString2());
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

	}

	private static void createNodes(int nbTotalNodes) {
		nodesToBeUse = new ArrayList<ChordNode>();
		int lower = 8001;
		int higher = 9000;
		
		for(int i =0;  i < nbTotalNodes-1; i++){
			try {
				nodesToBeUse.add(new ChordNode("localhost:" + (int) (Math.random() * (higher-lower)) + lower));
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}

	private static void joinNodes(ChordNode nodeToJoin) {
		for (int i = 0; i < nodesToBeUse.size(); i++) {
			int id;
			id = Hash.hash(nodesToBeUse.get(i).getNodeId())
					% (int) Math.pow(2, FingerTable.MAXFINGERS - 1);
			if (id < 0) {
				id = 128 + id;
			}
			try {
				if (ifNodeExist(id)) {
					System.err.println("Node "
							+ nodesToBeUse.get(i).getNodeId()
							+ " already existing !");
				} else {
					nodes.add(nodesToBeUse.get(i));
					System.out
							.println("\n--> Adding NODE "
									+ nodesToBeUse.get(i).getChordKey()
											.getKey() + "\n");
					nodesToBeUse.get(i).join(firstNode);

					nbNode++;
				}
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
