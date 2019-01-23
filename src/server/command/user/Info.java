/*
   
usage: "info" (no arguments)

    prints status information about the server:
        - number of connected clients


    Written by Kane Templeton Jan 21, 2019
 */

package server.command.user;

import server.Main;
import server.command.Command;

/**
 *
 * @author Kane
 */
public class Info extends Command {
    
    public Info() {
        super("info");
    }
    
    
    public void run(String[] args) {
        
        //int numPlrs = NIOServer.clientHandler.numConnects();

            int numClients = Main.server.clientHandler.numConnects();
            System.out.println("Number of connected clients: "+numClients);


    }

}
