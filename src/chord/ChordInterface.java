package chord;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface ChordInterface extends Remote{
	
	public void join(ChordInterface cNode) throws  RemoteException;
	
	public ChordInterface find_successor(int key) throws RemoteException;
	
	public ChordInterface closest_preceding_node(int cKey) throws RemoteException;
	
	public boolean isBetween(int id, int begin, int end) throws RemoteException;
	
	public void stabilize() throws RemoteException;
	
	public void notify(ChordInterface cNode) throws RemoteException;
	
	public void fix_fingers() throws RemoteException;
	
	public void addNode(String ip, int port) throws RemoteException;
	
	public void sortNodes() throws RemoteException;
	
	public String display() throws RemoteException;
	
	public ChordKeyInterface getChordKey() throws RemoteException;
	
	public ArrayList<ChordInterface> getNodes() throws RemoteException;
	
	public void setNodes(ArrayList<ChordInterface> nodes) throws RemoteException;
	
	public ChordInterface getPredecessor() throws RemoteException;
	
	public void setPredecessor(ChordInterface predecessor) throws RemoteException;
	
	public void setSuccessor(ChordInterface successor) throws RemoteException;
	
	public boolean isAlive() throws RemoteException;

}
