/*
    Keeps track of server's commands
    Important to add commands to the HashMap in this class
    when creating new command classes
    Written by Kane Templeton Dec 21, 2018
 */

package server.command;

import java.util.Arrays;
import java.util.HashMap;
import server.command.root.File;
import server.command.root.Poll;
import server.command.user.Info;
import server.command.user.Talk;


public class CommandHandler {
    
    private static HashMap<String,Command> commands;
    
    //ADD NEW COMMANDS IN THIS CONSTRUCTOR
    
    public CommandHandler() {
        commands = new HashMap<>();
        Talk talk = new Talk();     commands.put("talk", talk);
        File file = new File();     commands.put("file", file);
        Poll poll = new Poll();     commands.put("poll", poll);
        Info info = new Info();     commands.put("info", info);
    }
    
    /* processText(txt):
        process entered text from the console
        and run if it is a valid command.
    
    */
    public void processText(String txt) {
        String[] words = txt.split(" ");
        Command c = commands.get(words[0]);
        if (c==null)
            return;
        c.run(Arrays.copyOfRange(words, 1, words.length));
    }
    

     public static void argumentLengthError() {
        System.out.println("[Command Handler] Incorrect number of arguments.");
    }


}
