/*
    Entity abstract class

    instance of an interactive entity on the game map


    Written by Kane Templeton Jan 17, 2019
 */

package server.game.entity;


public abstract class Entity {
    
    private int entity_id;
    protected int posX,posY;
    
    
    public Entity(int id) {
        entity_id=id;
        //name=n;
        //serverID=server_id;
    }
    
    
    public int getX() {return posX;}
    public int getY() {return posY;}
    public void setCoordinates(int x, int y) {
        posX=x;
        posY=y;
    }
    
    

}
