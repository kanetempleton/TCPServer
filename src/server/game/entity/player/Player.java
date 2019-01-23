/*
    A player of the game.

    Contains all character information and handling.

    Written by Kane Templeton Jan 17, 2019
 */

package server.game.entity.player;

import server.Main;
import server.game.entity.Entity;


public class Player extends Entity {
    
    private String playerName;
    private int serverID;
    private int worldID;
    
    public Player(String name, int sID) {
        super(Main.game.entityManager.getIndex());
        playerName=name;
        serverID = sID;
        loadAccountInformation();
        //super.setCoordinates(x, y);
    }
    
    private void loadAccountInformation() {
        posX=0;
        posY=0;
    }

    
    public int getServerID() {return serverID;}
    public int getGameID() {return worldID;}
    
    public void setGameID(int id) {
        worldID=id;
    }

}
