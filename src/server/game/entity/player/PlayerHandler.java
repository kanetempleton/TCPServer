/*
    Handle actively logged in players
    Written by Kane Templeton Jan 17, 2019
 */

package server.game.entity.player;

/**
 *
 * @author Kane
 */
public class PlayerHandler {
    
    private Player[] players;
    private int curIndex;
    
    public static final int MAX_NUMBER_OF_PLAYERS = 2000;
    
    
    
    public PlayerHandler() {
        players = new Player[MAX_NUMBER_OF_PLAYERS];
    }
    
    public void add(Player P) {
        players[curIndex]=P;
        P.setGameID(curIndex);
        System.out.println("Logging in client "+P.getServerID()+" as player "+P.getGameID());
    }
    
    public int getIndex() {return curIndex;}

}
