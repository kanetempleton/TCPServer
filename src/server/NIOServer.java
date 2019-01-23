/*

    The main server file.

    Handles incoming connections
    and server loop stuff


*/
package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.command.CommandHandler;
import server.util.Poll;


public class NIOServer implements Runnable {
    
    
    public final int PORT = 10000;
    private PriorityQueue<Integer> recycledIDs;
    
    public ClientHandler clientHandler;
    public CommandHandler commandHandler;
    public PacketHandler packetHandler;
    //public Game game;
    
    
    private int numConnections;
    
    private ServerSocketChannel socketChannel;
    private ByteBuffer buffer;
    private Selector selector;
    
    private long lastTime;
    public int t_sec,t_min,t_hr;
    
    public NIOServer() {
        
        socketChannel = null;
        recycledIDs = new PriorityQueue<>();
        clientHandler = new ClientHandler();
        commandHandler = new CommandHandler();
        packetHandler = new PacketHandler();
        buffer = ByteBuffer.allocate(256);
        selector = null;
        
        numConnections = 0;
        lastTime = System.currentTimeMillis();
        t_sec = 0;
        t_min = 0;
        t_hr = 0;
        
    }
    
    /* run():
        called when this thread is started
    
    */
    
    public void run() {
        
        
        
        boolean killServer = false;
        
        try {
           // welcomeSocket = new ServerSocket(PORT);
            socketChannel = ServerSocketChannel.open();
            socketChannel.socket().bind(new InetSocketAddress(PORT));
            socketChannel.configureBlocking(false);
            
            System.out.println("Starting server on port "+PORT+"...");
            
            //SocketChannel clientSockets = socketChannel.accept();
            
            selector = Selector.open();
            
            socketChannel.register(selector, SelectionKey.OP_ACCEPT);
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            String build = "";
            while (!killServer) {
                
                //update server time
                long cur = System.currentTimeMillis();
                
                //Handle active client polls
                Iterator it = clientHandler.activePolls.values().iterator();
                try {
                    while (it.hasNext()) {
                       Poll P = (Poll)it.next();
                       long t = P.expireTime();
                       if (cur>=t) {
                           P.expire(); 
                           clientHandler.activePolls.remove(P.pollID());
                       }
                    }
                } catch(ConcurrentModificationException e) {
                    //do nothing and figure out why this happens
                }
                
                
                if ((cur - lastTime) >= 1000) {
                    lastTime = cur;
                    t_sec++;
                    if (t_sec>=60) {
                        clientHandler.checkAlivePoll();//poll every 60 seconds to check for disconnected clients
                        t_sec=0;
                        t_min++;
                        if (t_min>=60) {
                            t_min=0;
                            t_hr++;
                        }
                    }
                }
                int numConnects = selector.select(1);
            
                //text input from terminal
                char ch = 0;
                if (br.ready()) {
                try {
                    ch = (char)br.read();
                    //System.out.println("char "+ch);
                    build+=ch;
                } catch (IOException e) {
                    System.out.println("Error reading from Input device");
                }
                }
                if (!br.ready() && build.length()>0) {
                    build = build.substring(0, build.length()-1); //remove \n at end
                    System.out.println("typed: "+build);
                    if (build.startsWith("kick ")) {
                        int kickID = Integer.parseInt(build.split(" ")[1]);
                        print("kicking "+kickID);
                        clientHandler.disconnect(kickID);
                    }
                    if (build.equalsIgnoreCase("connections"))
                        System.out.println("Number of active clients: "+numConnections);
                    //sendMessage(Packet.TEXT,build,socketChannel);
                    //clientHandler.messageToAll(Packet.TEXT, build);
                    commandHandler.processText(build);
                    build="";
                }

            if (numConnects != 0) {
                Set keys = selector.selectedKeys(); //current sockets
                it = keys.iterator();
                while (it.hasNext()) {
                    SelectionKey key = (SelectionKey)it.next(); //key to deal with
                    
                    if ((key.readyOps() & SelectionKey.OP_ACCEPT) == SelectionKey.OP_ACCEPT) { //incoming connection
                        
                        Socket s = socketChannel.socket().accept(); 
                        
                       boolean con = clientHandler.connect(s);
                       if (!con)
                           continue;
                        
                        /*//check if server is full
                        if (numClients >= MAXIMUM_CONNECTIONS) {
                            System.out.println("Server is full. Connection refused: "+s);
                            //sendMessage(Packet.TEXT,"REJECT",s);
                            continue;
                        }*/
                        /*
                        //increase number
                        System.out.println( "Got connection from "+s );
                        numClients++;
                        numConnections++;
                        
                        */
                       /*
                        //now place this socket into list of connected clients
                        if (recycledIDs.isEmpty()) {
                            connectedClients.put(serverID, s);
                            //sendMessage(Packet.CONNECT,"ACCEPT "+serverID,serverID);
                            serverID++;
                        }
                        else {
                            int id = recycledIDs.poll();
                            connectedClients.put(id, s);
                            //sendMessage(Packet.CONNECT,"ACCEPT "+id,id);
                        }
                       */

                        SocketChannel sc = s.getChannel();
                        sc.configureBlocking(false);

                        // Register it with the selector, for reading
                        sc.register(selector, SelectionKey.OP_READ);

                    } else if ((key.readyOps() & SelectionKey.OP_READ) == SelectionKey.OP_READ) { //incoming data
                        SocketChannel sc = (SocketChannel)key.channel();

                        try {

                            
                              sc = (SocketChannel)key.channel();
                              buffer.clear();
                              buffer.put(new byte[256]);
                              buffer.clear();
                              sc.read(buffer);
                              buffer.flip();
                              
                              //read data from buffer
                              if (buffer.limit()!=0) {
                                String data = new String( buffer.array(), Charset.forName("UTF-8") );
                                
                                //System.out.println("got data: "+data);
       
                                String[] packets = Packet.packets(data);
                                int count=0;
                                for (String s: packets) {
                                    Packet P = new Packet(s);
                                    //System.out.println("["+P.getPacketID()+"][Packet "+(count++)+"]"+P.toString());
                                    int from = P.getPacketID();
                                    
                                    //send messages to clients here
                                    
                                    //process certain things
                                    /*
                                    
                                    */
                                    packetHandler.handle(P);
                                    /*if (P.getType().equalsIgnoreCase(Packet.DISCONNECT))
                                        disconnect(from);*/

                                }
                              }



                              // If the connection is dead, then remove it
                              // from the selector and close it
                              if (buffer.limit()==0) {
                                  
                                  //update player count
                                  clientHandler.poll("ALIVE", 1000);

                                key.cancel();

                                Socket s = null;
                                try {
                                  s = sc.socket();
                                  s.close();
                                } catch( IOException ie ) {
                                  System.err.println( "Error closing socket "+s+": "+ie );
                                }
                              }

                            } catch( IOException ie ) {
                                System.out.println(ie);

                              // On exception, remove this channel from the selector
                              //key.cancel();

                             // try {
                               // sc.close();
                             // } catch( IOException ie2 ) { System.out.println( ie2 ); }

                              System.out.println( "Closed "+sc );
                            }
                    } //end else if for incoming data
                    
                    
                }
                keys.clear(); //gtfo cuz we read u already
            }
            }
            
            
            
            //end of server loop; shut down stuff
            
            System.out.println("Shutting down server...");
           // inStream.close();
            socketChannel.close();
           // welcomeSocket.close();
            
        } catch (IOException ex) {
            Logger.getLogger(NIOServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    
    public void print(String s) {
        System.out.println("[Server]"+s);
    }
    
    public String serverTime() {
        return t_hr+" hours "+t_min+" minutes "+t_sec+" seconds";
    }
    
    public int serverTimeSeconds() {
        return t_hr*60*60+t_min*60+t_sec;
    }
    
}
