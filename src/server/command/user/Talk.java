/*
usage: "talk <client_id> <text>"
    
    sends a text packet containing the message <text> 
    to client specified by <client_id>


    Written by Kane Templeton Dec 21, 2018
 */

package server.command.user;

import java.io.IOException;
import server.Main;
import server.Packet;
import server.command.Command;

public class Talk extends Command {
    
    public Talk() {
        super("talk");
    }
    
    
    public void run(String[] args) {
        int to = Integer.parseInt(args[0]);
        String msg = "";
        for (int i=1; i<args.length-1; i++)
            msg+=args[i]+" ";
        msg+=args[args.length-1];
        try {
            Main.server.clientHandler.sendMessage(to, Packet.TEXT, msg);
        } catch (IOException ex) {
           // Logger.getLogger(Talk.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
