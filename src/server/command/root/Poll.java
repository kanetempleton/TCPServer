/*

usage: "poll <message> <time>"

    creates a poll that must be responded to within <time> milliseconds
    and contains the text <message>


    Written by Kane Templeton Jan 21, 2019
 */

package server.command.root;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.Main;
import server.command.Command;
import server.command.CommandHandler;


public class Poll extends Command {
    
    public Poll() {
        super("poll");
    }
    
    public void run(String[] args) {
        if (args.length<2) {
            CommandHandler.argumentLengthError();
            return;
        }
        String message = args[0];
        long tRespond = Long.parseLong(args[1]);
        
        try {
            
            Main.server.clientHandler.poll(message, tRespond);
            
        } catch (IOException ex) {
            Logger.getLogger(Poll.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
