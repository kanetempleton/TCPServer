/*
    Handle incoming packets
    Written by Kane Templeton Dec 22, 2018
 */

package server;

import java.io.IOException;
import server.game.login.LoginHandler;
import server.game.login.User;

/**
 *
 * @author Kane
 */
public class PacketHandler {
    
    public PacketHandler() {
        
    }
    
    public void handle(Packet P) throws IOException {
        int from = P.getPacketID();
        print(P.toString());
        print(P.getData());
        if (P.getType().equalsIgnoreCase(Packet.DISCONNECT))
            Main.server.clientHandler.disconnect(from);
        if (P.getType().equalsIgnoreCase(Packet.LOGIN)) {
            if (P.getText().trim().equals(";;")) {
                //ignore blank requests
            } else {
            String words[] = P.getText().split(";;");
            String user = words[0];
            String pass = words[1];
            print("Login request from "+P.getPacketID()+": ("+user+","+pass+")");
            User U = new User(user,pass,P.getPacketID());
            LoginHandler.check(U);
            }
        }
        if (P.getType().equalsIgnoreCase(Packet.POLL)) {
            int id = P.getPacketID();
            Main.server.clientHandler.checkResponse(P.getData());
            //NIOServer.clientHandler.fetchPlayers=false;
            //String words[] = P.getText().split(" ");
            //int r = Integer.parseInt(words[1]);
            //String msg = Misc.arrayToString(words, 2);
            
            //NIOServer.clientHandler.
        }
    }
    
    public void print(String x) {
        System.out.println("[Packet Handler]"+x);
    }

}
