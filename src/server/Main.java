/*
    Main class

    creates and runs two threads: one for the server, one for the game

    Written by Kane Templeton Jan 22, 2019
 */

package server;

import server.game.Game;

public class Main {
    
    public static NIOServer server;
    public static Game game;
    
    public static void main(String[] args) {
        
        server = new NIOServer();
        new Thread(server).start();
        
        game = new Game();
        new Thread(game).start();
    }

}
