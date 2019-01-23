/*
    message to send to all clients and expect a certain response

    Written by Kane Templeton Jan 21, 2019
 */

package server.util;

import java.io.IOException;
import server.ClientHandler;
import server.Main;
import server.Packet;


public class Poll {
    
    private String sendMsg;
    private long timeToRespond;
    
    private boolean[] responses;
    
    public static int pollidcounter = 0;
    private int pollID;
    
    public Poll(String msg, long respond) throws IOException {
        sendMsg=msg;
        pollID=pollidcounter++;
        timeToRespond=System.currentTimeMillis()+respond;
        responses = new boolean[ClientHandler.maxId];
        Main.server.clientHandler.messageToAll(Packet.POLL, pollID+" "+msg);
        if (pollidcounter>=1000000) {
            System.out.println("One million polls... resetting counter.");
            pollidcounter=0;
        }
    }
    
    /** takeResponse(id,response):
     * check response from client <id> 
     * if the response matches what it is expected to be,
     * mark the respective ID as having completed the poll
    
    */
    public void takeResponse(int id, String response) {
        if (response.equals(response(sendMsg))) {
            if (id<responses.length)
                responses[id]=true;
        }
    }
    
    /** expire():
     * called after <timeToRespond> milliseconds
     * after a poll's creation; 
     * handles clients that have not responded.
    
    */
    public void expire() throws IOException {
        for (int i=0;i<responses.length;i++) {
            if (!responses[i]) {
                //if (!NIOServer.clientHandler.recycledIDs.contains((Integer)i)) {
                    Main.server.clientHandler.addToRecycled(i);
                    //NIOServer.clientHandler.fetchPlayers=false;
                    //System.out.println("Poll["+sendMsg+"]no response from "+i);
                //}
                
            }
        }
    }
    
    public boolean equals(Poll P) {
        return P.pollID==this.pollID;
    }
    

    
    public long expireTime() { return timeToRespond;}
    public int pollID() {return pollID;}
    
    
    /* response(msg):
        the response that should be received from clients
        after polling "msg"
    
    */
    private static String response(String msg) {
        if (msg.equalsIgnoreCase("ALIVE")) {
            return "HELLO";
        }
        return "REJECT";
    }

}
