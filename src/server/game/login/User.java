/*
    keep track of username and password pairs for login requests
    stores a Player object

    Written by Kane Templeton Jan 17, 2019
 */

package server.game.login;

import server.game.entity.player.Player;

/**
 *
 * @author Kane
 */
public class User {
    
    private int fromClientID;
    private String username,password;
    
    private Player plr;
    
    public User(String name, String pass, int from) {
        fromClientID=from;
        username=name;
        password=pass;
        plr=null;
    }
    
    public String getUsername(){return username;}
    public String getPassword(){return password;}
    public int fromClientID() {return fromClientID;}
    public void setEntity(Player P) {plr=P;}
    public Player getPlayer() {return plr;}

}
