/*
    Methods dealing with connected clients
    Written by Kane Templeton Dec 20, 2018
 */

package server;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import server.util.Misc;
import server.util.Poll;

/**
 *
 * @author Kane
 */
public class ClientHandler {
    
    private Socket[] client;
    public static int maxId,numConnects;
    public static final int MAX_CONNECTIONS = 1000;
    
    public HashMap<Integer,Poll> activePolls;
    public ArrayList<Integer> recycledIDs;
    
    
    public ClientHandler() {
        maxId = 0;
        curId = 0;
        numConnects = 0;
        fetchPlayers=false;
        client = new Socket[MAX_CONNECTIONS];
        activePolls = new HashMap<>();
        recycledIDs = new ArrayList<>();
        for (int i=0; i<MAX_CONNECTIONS; i++) {
            client[i] = null;
      
        }
    }
    
    /* connect(s):
        connects a socket to the server
    */
    
    public boolean connect(Socket s) {
        try {
            if (numConnects>=MAX_CONNECTIONS) {
                Main.server.print("Server is full. Connection refused.");
                sendMessage(s,Packet.TEXT,"REJECT");
                return false;
            }
            int idToAssign=-1;
           /* if (recycledIDs.size()>0) 
                idToAssign=recycledIDs.remove(0);
            else
                idToAssign = maxId; //or numConnects
            */
            
            idToAssign = curId++;
            
            client[idToAssign]=s;
            Main.server.print("Got connection; Assigned server ID "+idToAssign+" to "+s);
            sendMessage(idToAssign,Packet.CONNECT,"ACCEPT "+idToAssign);
            numConnects++;
            if (numConnects>maxId)
                maxId=numConnects;
            if (curId>maxId)
                maxId=curId;
            return true;
        }
        catch (IOException e) {
            return false;
        }
    }
    private int curId;
    
    public void disconnect(int id) throws IOException {
        if (recycledIDs.contains(id)) 
            return;
        try {
        Main.server.print("Disconnecting "+id+": "+client[id]);
        sendMessage(id,Packet.DISCONNECT,"OK");
        client[id].close();
        remove(id);
        } catch (NullPointerException e) {
            //do nothing
        }
        /*for (int i=id; i<numConnects-1; i++) {
            client[id]=client[id+1];
            sendMessage(id,Packet.CONNECT,"UPDATE "+id);
        }
        client[numConnects-1]=null;
        numConnects--;*/
    }
    
    //like disconnect but doesn't close socket
    public void remove(int id) throws IOException {
        if (recycledIDs.contains(id)) 
            return;
        client[id]=null;
        numConnects--;
    }
        
    
    
    //methods for sending packets to clients
    
    public void sendMessage(int to, String type, String mes) throws IOException {
        Socket c = getClient(to);
        if (c==null)
            return;
        if (c.isClosed())
            return;
        DataOutputStream outStream = new DataOutputStream(c.getChannel().socket().getOutputStream());
        Packet P = new Packet(type,"12 "+mes);
        
        ByteBuffer bytebuf = ByteBuffer.wrap(P.toString().getBytes());
        try {
            c.getChannel().write(bytebuf);
        } catch (IOException ex) {
            //Logger.getLogger(NIOServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    public void sendMessage(Socket c, String type, String mes) throws IOException {
       // Socket c = getClient(to);
        DataOutputStream outStream = new DataOutputStream(c.getChannel().socket().getOutputStream());
        Packet P = new Packet(type,"9999 "+mes);
        
        ByteBuffer bytebuf = ByteBuffer.wrap(P.toString().getBytes());
        try {
            c.getChannel().write(bytebuf);
        } catch (IOException ex) {
            //Logger.getLogger(NIOServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    public void sendText(int to, String mes) throws IOException {
        sendMessage(to,Packet.TEXT,mes);
    }
    public void sendText(Socket to, String mes) throws IOException {
        sendMessage(to,Packet.TEXT,mes);
    }
    public void messageToAll(String type, String mes) throws IOException {
        
        for (int i=0; i<maxId; i++) {
            sendMessage(i,type,mes);
        }
    }
    
    /* checkAlivePoll():
        polls all clients to see if they are still connected to the server
        recycles ids of clients that do not respond
    
    */
    public void checkAlivePoll() throws IOException {
        poll("ALIVE",500);
        fetchPlayers=true;
    }
    
    public boolean fetchPlayers;
    
    
    
    public Socket getClient(int id) {
        return client[id];
    }
    
    public int numConnects() {
        return numConnects;
    }
    
    /** poll(msg,tRespond):
     * sends a message to all clients. clients must respond within specified time
     * 
     * @param msg: message to broadcast in the poll
     * @param tRespond: number of milliseconds before the poll expires (must be responded to by then)
    
    */
    public void poll(String msg, long tRespond) throws IOException {
        activePolls.put(Poll.pollidcounter,new Poll(msg,tRespond));
    }
    
    
    public void checkResponse(String pollResponse) {
        String words[] = pollResponse.split(" ");
        int x1 = Integer.parseInt(words[0]);
        int x2 = Integer.parseInt(words[1]);
        String resp = Misc.arrayToString(words, 2);
        Poll P = activePolls.get(x2);
        P.takeResponse(x1,resp);
        System.out.println("Poll "+x2+" took response from "+x1);
    }
    
    /* addToRecycled(id):
        add an id to the list of disconnected clients whose IDs can be reused
    
    */
    public void addToRecycled(int id) throws IOException {
        try {
            disconnect(id);
        } catch (IOException e) {
            remove(id);
        }
        recycledIDs.add((Integer)id);
        //System.out.println("removing "+id+" and adding to recycled list.");
        //System.out.println("now have);
    }
    
    
    

}
