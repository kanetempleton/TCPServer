/*
    Handle login requests from clients

    Written by Kane Templeton Jan 17, 2019
 */

package server.game.login;

import java.io.File;
import java.io.IOException;
import server.Main;
import server.Packet;
import server.game.entity.player.Player;
import server.util.FileHandler;


public class LoginHandler {
    
    
    public static void check(User U) throws IOException {
        String user = U.getUsername();
        String pass = U.getPassword();
        int from = U.fromClientID();
        File charFile = new File(accountPath(user));
        if (!charFile.exists()) {
            System.out.println("Creating new account for user "+user);
            createAccount(user,pass);
            Main.server.clientHandler.sendMessage(from, Packet.LOGIN_ACCEPT, "New account created.");
            Player P = new Player(user,from);
            login(P);
        }
        else {
            String realPass = FileHandler.loadFileAsString(accountPath(user)).trim();
            if (pass.equals(realPass)) { //password accepted
                Main.server.clientHandler.sendMessage(from, Packet.LOGIN_ACCEPT, "<client data>");
                Player P = new Player(user,from);
                login(P);
            }
            else { //password incorrect
                System.out.println("\""+pass+"\" vs\""+realPass+"\"");
                Main.server.clientHandler.sendMessage(from, Packet.LOGIN_REJECT, "Invalid password.");
            }
        }
    }
    
    public static void createAccount(String user, String pass) {
        FileHandler.writeFile("data/users/"+user+".txt", pass);
    }
    
    private static String accountPath(String user) {
        return "data/users/"+user+".txt";
    }
    
    private static void login(Player P) {
        if (Main.game==null)
            System.out.println("game null");
        if (Main.game.playerHandler==null)
            System.out.println("PH null");
        if (P==null)
            System.out.println("playernul");
        
        Main.game.playerHandler.add(P);
    }

}
