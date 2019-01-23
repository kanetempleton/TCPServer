/*
    Abstract class for a command

    Commands are entered into the console.
    Add commands into CommandHandler when
    creating new command classes

    Written by Kane Templeton Dec 21, 2018
 */

package server.command;


public abstract class Command {
    
    
    //name: this is what you must type to activate the command
    private String name;
    
    public Command(String n) {
        name=n;
    }
    
    public abstract void run(String[] args);
    
   
}
