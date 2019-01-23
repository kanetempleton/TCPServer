/*
    
usage: "file <action> <filename> <contents>"
    
    <action>:
        make: create a new file called <filename> with contents <contents>
        append: add <contents> to the end of <filename>



    Written by Kane Templeton Jan 12, 2019
 */

package server.command.root;

import server.command.Command;
import server.command.CommandHandler;
import server.util.FileHandler;


public class File extends Command {
    
    public File() {
        super("file");
    }
    
    public void run(String[] args) {
        if (args.length<2) {
            CommandHandler.argumentLengthError();
            return;
        }
        String action = args[0];
        String fileName = args[1];
        if (action.equalsIgnoreCase("make") || action.equalsIgnoreCase("append")) {
            if (args.length<3) {
                CommandHandler.argumentLengthError();
                return;
            }
            String fileContents = "";
            for (int i=2; i<args.length-1; i++) {
                fileContents += args[i] + " ";
            }
            fileContents += args[args.length-1];
            if (action.equalsIgnoreCase("make")) {
                FileHandler.writeFile(fileName, fileContents);
                System.out.println("[file] "+fileName+" created.");
            }
            else {
                FileHandler.appendToFile(fileName, fileContents);
                System.out.println("[file] "+fileName+" modified.");
            }
            
        }
    }

}
