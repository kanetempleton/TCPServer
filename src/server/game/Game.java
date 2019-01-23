/*
    The game engine.
    Thread that runs concurrently with the server

    Written by Kane Templeton Jan 7, 2019
 */

package server.game;

import server.game.entity.EntityManager;
import server.game.entity.player.PlayerHandler;


public class Game implements Runnable {
    
    public EntityManager entityManager;
    public PlayerHandler playerHandler;
    

    
    public Game() {
        entityManager = new EntityManager();
        playerHandler = new PlayerHandler();
    }
    
    /* run():
        game loop. called when this thread is started
    */
    public void run() {
        while (true) {
            //if (playerHandler.)
        }
    }
    
    

}
